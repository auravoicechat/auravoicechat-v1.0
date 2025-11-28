/**
 * KYC Controller
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Only ID Card (front/back) + Selfie - NO utility bills
 */

import { Response, NextFunction } from 'express';
import { AuthRequest } from '../middleware/auth';
import * as kycService from '../services/kycService';

// Get KYC status
export const getKycStatus = async (req: AuthRequest, res: Response, next: NextFunction) => {
  try {
    const userId = req.userId!;
    const status = await kycService.getKycStatus(userId);
    
    res.json(status);
  } catch (error) {
    next(error);
  }
};

// Submit KYC
export const submitKyc = async (req: AuthRequest, res: Response, next: NextFunction) => {
  try {
    const userId = req.userId!;
    const { idCardFrontUri, idCardBackUri, selfieUri, livenessCheckPassed } = req.body;
    
    await kycService.submitKyc(userId, {
      idCardFrontUri,
      idCardBackUri,
      selfieUri,
      livenessCheckPassed
    });
    
    res.json({ success: true });
  } catch (error) {
    next(error);
  }
};

// Get upload URL for ID card front
export const getIdFrontUploadUrl = async (req: AuthRequest, res: Response, next: NextFunction) => {
  try {
    const userId = req.userId!;
    const url = await kycService.getUploadUrl(userId, 'id-front');
    
    res.json({ uploadUrl: url });
  } catch (error) {
    next(error);
  }
};

// Get upload URL for ID card back
export const getIdBackUploadUrl = async (req: AuthRequest, res: Response, next: NextFunction) => {
  try {
    const userId = req.userId!;
    const url = await kycService.getUploadUrl(userId, 'id-back');
    
    res.json({ uploadUrl: url });
  } catch (error) {
    next(error);
  }
};

// Get upload URL for selfie
export const getSelfieUploadUrl = async (req: AuthRequest, res: Response, next: NextFunction) => {
  try {
    const userId = req.userId!;
    const url = await kycService.getUploadUrl(userId, 'selfie');
    
    res.json({ uploadUrl: url });
  } catch (error) {
    next(error);
  }
};
