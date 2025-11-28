/**
 * Referrals Controller
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Get Coins and Get Cash programs
 */

import { Response, NextFunction } from 'express';
import { AuthRequest } from '../middleware/auth';
import * as referralsService from '../services/referralsService';

// Bind referral code
export const bindReferralCode = async (req: AuthRequest, res: Response, next: NextFunction) => {
  try {
    const userId = req.userId!;
    const { code } = req.body;
    
    await referralsService.bindReferralCode(userId, code);
    
    res.json({ success: true });
  } catch (error) {
    next(error);
  }
};

// Get Coins - Summary
export const getCoinsSummary = async (req: AuthRequest, res: Response, next: NextFunction) => {
  try {
    const userId = req.userId!;
    const summary = await referralsService.getCoinsSummary(userId);
    
    res.json({
      invitationsCount: summary.invitationsCount,
      totalCoinsRewarded: summary.totalCoinsRewarded,
      withdrawableCoins: summary.withdrawableCoins,
      withdrawMin: 100,
      cooldownSeconds: summary.cooldownSeconds
    });
  } catch (error) {
    next(error);
  }
};

// Get Coins - Withdraw
export const withdrawCoins = async (req: AuthRequest, res: Response, next: NextFunction) => {
  try {
    const userId = req.userId!;
    const { amount } = req.body;
    
    await referralsService.withdrawCoins(userId, amount);
    
    res.json({ success: true });
  } catch (error) {
    next(error);
  }
};

// Get Coins - Records
export const getRecords = async (req: AuthRequest, res: Response, next: NextFunction) => {
  try {
    const userId = req.userId!;
    const page = parseInt(req.query.page as string) || 1;
    const pageSize = parseInt(req.query.pageSize as string) || 10;
    
    const result = await referralsService.getRecords(userId, page, pageSize);
    
    res.json({
      data: result.records,
      pagination: {
        page,
        pageSize,
        totalItems: result.totalCount,
        totalPages: Math.ceil(result.totalCount / pageSize)
      }
    });
  } catch (error) {
    next(error);
  }
};

// Get Cash - Summary
export const getCashSummary = async (req: AuthRequest, res: Response, next: NextFunction) => {
  try {
    const userId = req.userId!;
    const summary = await referralsService.getCashSummary(userId);
    
    res.json({
      balanceUsd: summary.balanceUsd,
      minWithdrawalUsd: 1.00,
      walletCooldownSeconds: 30,
      externalAllowedMinUsd: 10.00,
      externalClearanceDays: 5
    });
  } catch (error) {
    next(error);
  }
};

// Get Cash - Withdraw
export const withdrawCash = async (req: AuthRequest, res: Response, next: NextFunction) => {
  try {
    const userId = req.userId!;
    const { destination } = req.body;
    
    await referralsService.withdrawCash(userId, destination);
    
    res.json({ success: true });
  } catch (error) {
    next(error);
  }
};

// Get Cash - Invite Records
export const getInviteRecords = async (req: AuthRequest, res: Response, next: NextFunction) => {
  try {
    const userId = req.userId!;
    const weekStart = req.query.weekStart as string;
    const page = parseInt(req.query.page as string) || 1;
    
    const result = await referralsService.getInviteRecords(userId, weekStart, page);
    
    res.json({
      data: result.records,
      pagination: {
        page,
        pageSize: 20,
        totalItems: result.totalCount,
        totalPages: Math.ceil(result.totalCount / 20)
      }
    });
  } catch (error) {
    next(error);
  }
};

// Get Cash - Ranking
export const getRanking = async (req: AuthRequest, res: Response, next: NextFunction) => {
  try {
    const page = parseInt(req.query.page as string) || 1;
    
    const result = await referralsService.getRanking(page);
    
    res.json({
      data: result.rankings,
      pagination: {
        page,
        pageSize: 20,
        totalItems: result.totalCount,
        totalPages: Math.ceil(result.totalCount / 20)
      }
    });
  } catch (error) {
    next(error);
  }
};
