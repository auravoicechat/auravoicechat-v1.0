/**
 * Guide System Controller - Enhanced
 * Guide application and management
 */

import { Request, Response } from 'express';
import { PrismaClient } from '@prisma/client';
import { logger } from '../utils/logger';

const prisma = new PrismaClient();

/**
 * Submit guide application
 */
export const submitApplication = async (req: Request, res: Response) => {
  try {
    const userId = req.user?.id;
    const { fullName, age, country, languages, experience, motivation, hoursPerWeek } = req.body;

    if (!userId) {
      return res.status(401).json({ success: false, error: 'Unauthorized' });
    }

    // Check if user is female (girls only)
    const user = await prisma.user.findUnique({
      where: { id: userId },
      select: { gender: true }
    });

    if (user?.gender !== 'female') {
      return res.status(403).json({ 
        success: false, 
        error: 'Guide applications are only available for female users' 
      });
    }

    // Check if already applied
    const existing = await prisma.guideApplication.findUnique({
      where: { userId }
    });

    if (existing) {
      return res.status(400).json({ 
        success: false, 
        error: 'You have already submitted an application' 
      });
    }

    const application = await prisma.guideApplication.create({
      data: {
        userId,
        fullName,
        age,
        country,
        languages,
        experience,
        motivation,
        hoursPerWeek,
        status: 'pending'
      }
    });

    logger.info(`Guide application submitted by user ${userId}`);

    res.status(201).json({
      success: true,
      data: application,
      message: 'Application submitted successfully'
    });
  } catch (error) {
    logger.error('Error submitting guide application:', error);
    res.status(500).json({ success: false, error: 'Failed to submit application' });
  }
};

/**
 * Get user's guide application
 */
export const getMyApplication = async (req: Request, res: Response) => {
  try {
    const userId = req.user?.id;

    if (!userId) {
      return res.status(401).json({ success: false, error: 'Unauthorized' });
    }

    const application = await prisma.guideApplication.findUnique({
      where: { userId }
    });

    if (!application) {
      return res.status(404).json({ success: false, error: 'No application found' });
    }

    res.json({
      success: true,
      data: application
    });
  } catch (error) {
    logger.error('Error fetching application:', error);
    res.status(500).json({ success: false, error: 'Failed to fetch application' });
  }
};

/**
 * Get all guide applications (admin only)
 */
export const getAllApplications = async (req: Request, res: Response) => {
  try {
    const { status } = req.query;

    const where: any = {};
    if (status) {
      where.status = status;
    }

    const applications = await prisma.guideApplication.findMany({
      where,
      orderBy: { createdAt: 'desc' }
    });

    res.json({
      success: true,
      data: applications
    });
  } catch (error) {
    logger.error('Error fetching applications:', error);
    res.status(500).json({ success: false, error: 'Failed to fetch applications' });
  }
};

/**
 * Approve guide application (admin only)
 */
export const approveApplication = async (req: Request, res: Response) => {
  try {
    const { id } = req.params;
    const adminId = req.user?.id;

    if (!adminId) {
      return res.status(401).json({ success: false, error: 'Unauthorized' });
    }

    const application = await prisma.guideApplication.findUnique({
      where: { id }
    });

    if (!application) {
      return res.status(404).json({ success: false, error: 'Application not found' });
    }

    if (application.status !== 'pending') {
      return res.status(400).json({ success: false, error: 'Application already processed' });
    }

    // Update application
    const updated = await prisma.guideApplication.update({
      where: { id },
      data: {
        status: 'approved',
        reviewedBy: adminId,
        reviewedAt: new Date()
      }
    });

    // Create guide profile
    await prisma.guideProfile.create({
      data: {
        userId: application.userId,
        level: 1,
        isActive: true
      }
    });

    // Update user role
    await prisma.user.update({
      where: { id: application.userId },
      data: { role: 'guide' }
    });

    logger.info(`Guide application approved: ${id} by admin ${adminId}`);

    res.json({
      success: true,
      data: updated,
      message: 'Application approved'
    });
  } catch (error) {
    logger.error('Error approving application:', error);
    res.status(500).json({ success: false, error: 'Failed to approve application' });
  }
};

/**
 * Reject guide application (admin only)
 */
export const rejectApplication = async (req: Request, res: Response) => {
  try {
    const { id } = req.params;
    const { reason } = req.body;
    const adminId = req.user?.id;

    if (!adminId) {
      return res.status(401).json({ success: false, error: 'Unauthorized' });
    }

    const updated = await prisma.guideApplication.update({
      where: { id },
      data: {
        status: 'rejected',
        reviewedBy: adminId,
        reviewedAt: new Date(),
        rejectionReason: reason
      }
    });

    logger.info(`Guide application rejected: ${id} by admin ${adminId}`);

    res.json({
      success: true,
      data: updated,
      message: 'Application rejected'
    });
  } catch (error) {
    logger.error('Error rejecting application:', error);
    res.status(500).json({ success: false, error: 'Failed to reject application' });
  }
};

/**
 * Get guide profile
 */
export const getMyGuideProfile = async (req: Request, res: Response) => {
  try {
    const userId = req.user?.id;

    if (!userId) {
      return res.status(401).json({ success: false, error: 'Unauthorized' });
    }

    const profile = await prisma.guideProfile.findUnique({
      where: { userId }
    });

    if (!profile) {
      return res.status(404).json({ success: false, error: 'Guide profile not found' });
    }

    res.json({
      success: true,
      data: profile
    });
  } catch (error) {
    logger.error('Error fetching guide profile:', error);
    res.status(500).json({ success: false, error: 'Failed to fetch profile' });
  }
};

/**
 * Get guide earning targets
 */
export const getGuideTargets = async (req: Request, res: Response) => {
  try {
    const targets = await prisma.earningTarget.findMany({
      where: {
        targetType: 'guide',
        isActive: true
      },
      orderBy: { tier: 'asc' }
    });

    res.json({
      success: true,
      data: targets
    });
  } catch (error) {
    logger.error('Error fetching guide targets:', error);
    res.status(500).json({ success: false, error: 'Failed to fetch targets' });
  }
};
