/**
 * Owner Panel Economy Controller - Enhanced
 * Complete economy management for owner
 */

import { Request, Response } from 'express';
import { PrismaClient } from '@prisma/client';
import { logger } from '../utils/logger';

const prisma = new PrismaClient();

/**
 * Get owner dashboard statistics
 */
export const getDashboardStats = async (req: Request, res: Response) => {
  try {
    const [
      totalUsers,
      totalAdmins,
      totalGuides,
      activeRooms,
      pendingCashouts,
      pendingGuideApps,
      monthlyRevenue,
      dailyActiveUsers
    ] = await Promise.all([
      prisma.user.count(),
      prisma.admin.count({ where: { isActive: true } }),
      prisma.guideProfile.count({ where: { isActive: true } }),
      prisma.room.count({ where: { isLive: true } }),
      prisma.cashoutRequest.count({ where: { status: 'pending' } }),
      prisma.guideApplication.count({ where: { status: 'pending' } }),
      prisma.transaction.aggregate({
        where: {
          type: 'deposit',
          createdAt: {
            gte: new Date(new Date().setDate(1)) // First day of month
          }
        },
        _sum: { amount: true }
      }),
      prisma.user.count({
        where: {
          lastLoginAt: {
            gte: new Date(Date.now() - 24 * 60 * 60 * 1000) // Last 24 hours
          }
        }
      })
    ]);

    res.json({
      success: true,
      data: {
        totalUsers,
        totalAdmins,
        totalGuides,
        activeRooms,
        pendingCashouts,
        pendingGuideApplications: pendingGuideApps,
        monthlyRevenue: monthlyRevenue._sum.amount || 0,
        dailyActiveUsers
      }
    });
  } catch (error) {
    logger.error('Error fetching dashboard stats:', error);
    res.status(500).json({ success: false, error: 'Failed to fetch stats' });
  }
};

/**
 * Get economy configuration
 */
export const getEconomyConfig = async (req: Request, res: Response) => {
  try {
    const [
      targets,
      guideTargets,
      conversionRates,
      limits
    ] = await Promise.all([
      prisma.earningTarget.findMany({
        where: { targetType: 'user', isActive: true },
        orderBy: { tier: 'asc' }
      }),
      prisma.earningTarget.findMany({
        where: { targetType: 'guide', isActive: true },
        orderBy: { tier: 'asc' }
      }),
      prisma.economyConfig.findUnique({
        where: { key: 'conversion_rates' }
      }),
      prisma.economyConfig.findUnique({
        where: { key: 'system_limits' }
      })
    ]);

    res.json({
      success: true,
      data: {
        userTargets: targets,
        guideTargets,
        conversionRates: {
          diamondToCashRate: conversionRates?.diamondToCashRate || 0.0003, // 100K diamonds = $30
          diamondToCoinRate: conversionRates?.diamondToCoinRate || 30 // 30% conversion
        },
        systemLimits: {
          minCashout: limits?.minCashout || 10,
          maxCashout: limits?.maxCashout || 10000,
          clearanceDays: limits?.clearanceDays || 5,
          dailyDiamondLimit: limits?.dailyDiamondLimit || 1000000
        }
      }
    });
  } catch (error) {
    logger.error('Error fetching economy config:', error);
    res.status(500).json({ success: false, error: 'Failed to fetch config' });
  }
};

/**
 * Update earning targets
 */
export const updateEarningTargets = async (req: Request, res: Response) => {
  try {
    const { targetType, targets } = req.body;

    if (!['user', 'guide'].includes(targetType)) {
      return res.status(400).json({ success: false, error: 'Invalid target type' });
    }

    // Delete existing targets of this type
    await prisma.earningTarget.deleteMany({
      where: { targetType }
    });

    // Create new targets
    const created = await prisma.earningTarget.createMany({
      data: targets.map((t: any) => ({
        tier: t.tier,
        targetType,
        diamondsRequired: BigInt(t.diamondsRequired),
        cashReward: t.cashReward,
        bonusCoins: BigInt(t.bonusCoins || 0),
        bonusBadge: t.bonusBadge,
        isActive: true
      }))
    });

    logger.info(`Earning targets updated for ${targetType}: ${created.count} tiers`);

    res.json({
      success: true,
      data: { count: created.count },
      message: 'Earning targets updated'
    });
  } catch (error) {
    logger.error('Error updating targets:', error);
    res.status(500).json({ success: false, error: 'Failed to update targets' });
  }
};

/**
 * Update conversion rates
 */
export const updateConversionRates = async (req: Request, res: Response) => {
  try {
    const { diamondToCashRate, diamondToCoinRate } = req.body;

    await prisma.economyConfig.upsert({
      where: { key: 'conversion_rates' },
      update: {
        diamondToCashRate,
        diamondToCoinRate
      },
      create: {
        key: 'conversion_rates',
        diamondToCashRate,
        diamondToCoinRate
      }
    });

    logger.info(`Conversion rates updated: D→C=${diamondToCashRate}, D→Coin=${diamondToCoinRate}`);

    res.json({
      success: true,
      message: 'Conversion rates updated'
    });
  } catch (error) {
    logger.error('Error updating conversion rates:', error);
    res.status(500).json({ success: false, error: 'Failed to update rates' });
  }
};

/**
 * Update system limits
 */
export const updateSystemLimits = async (req: Request, res: Response) => {
  try {
    const { minCashout, maxCashout, clearanceDays, dailyDiamondLimit } = req.body;

    await prisma.economyConfig.upsert({
      where: { key: 'system_limits' },
      update: {
        minCashout,
        maxCashout,
        clearanceDays,
        dailyDiamondLimit: dailyDiamondLimit ? BigInt(dailyDiamondLimit) : undefined
      },
      create: {
        key: 'system_limits',
        minCashout,
        maxCashout,
        clearanceDays,
        dailyDiamondLimit: dailyDiamondLimit ? BigInt(dailyDiamondLimit) : null
      }
    });

    logger.info(`System limits updated: min=${minCashout}, max=${maxCashout}, clearance=${clearanceDays}d`);

    res.json({
      success: true,
      message: 'System limits updated'
    });
  } catch (error) {
    logger.error('Error updating system limits:', error);
    res.status(500).json({ success: false, error: 'Failed to update limits' });
  }
};

/**
 * Get pending cashout requests (owner approval)
 */
export const getPendingCashouts = async (req: Request, res: Response) => {
  try {
    const { status = 'pending' } = req.query;

    const requests = await prisma.cashoutRequest.findMany({
      where: { status: status as string },
      include: {
        // Would need to join with User model
      },
      orderBy: { requestedAt: 'desc' }
    });

    res.json({
      success: true,
      data: requests
    });
  } catch (error) {
    logger.error('Error fetching cashouts:', error);
    res.status(500).json({ success: false, error: 'Failed to fetch cashouts' });
  }
};

/**
 * Approve cashout request (owner only)
 */
export const approveCashout = async (req: Request, res: Response) => {
  try {
    const { id } = req.params;
    const userId = req.user?.id;

    const request = await prisma.cashoutRequest.findUnique({
      where: { id }
    });

    if (!request) {
      return res.status(404).json({ success: false, error: 'Cashout request not found' });
    }

    if (request.status !== 'pending') {
      return res.status(400).json({ success: false, error: 'Request already processed' });
    }

    // Check if clearance period has passed
    if (new Date() < request.clearanceDate) {
      return res.status(400).json({ 
        success: false, 
        error: 'Clearance period not yet passed' 
      });
    }

    const updated = await prisma.cashoutRequest.update({
      where: { id },
      data: {
        status: 'approved',
        reviewedBy: userId,
        reviewedAt: new Date()
      }
    });

    logger.info(`Cashout approved: ${id} by owner ${userId}`);

    res.json({
      success: true,
      data: updated,
      message: 'Cashout request approved'
    });
  } catch (error) {
    logger.error('Error approving cashout:', error);
    res.status(500).json({ success: false, error: 'Failed to approve cashout' });
  }
};

/**
 * Reject cashout request (owner only)
 */
export const rejectCashout = async (req: Request, res: Response) => {
  try {
    const { id } = req.params;
    const { reason } = req.body;
    const userId = req.user?.id;

    const updated = await prisma.cashoutRequest.update({
      where: { id },
      data: {
        status: 'rejected',
        reviewedBy: userId,
        reviewedAt: new Date(),
        rejectionReason: reason
      }
    });

    logger.info(`Cashout rejected: ${id} by owner ${userId}`);

    res.json({
      success: true,
      data: updated,
      message: 'Cashout request rejected'
    });
  } catch (error) {
    logger.error('Error rejecting cashout:', error);
    res.status(500).json({ success: false, error: 'Failed to reject cashout' });
  }
};
