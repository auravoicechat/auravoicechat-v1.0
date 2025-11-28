/**
 * Referrals Routes
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Get Coins and Get Cash programs
 */

import { Router } from 'express';
import { authenticate } from '../middleware/auth';
import { generalLimiter, referralLimiter, withdrawalLimiter } from '../middleware/rateLimiter';
import { validateBindReferral, validateWithdrawCash, validatePagination } from '../middleware/validation';
import * as referralsController from '../controllers/referralsController';

const router = Router();

// Bind referral code
router.post('/bind', referralLimiter, authenticate, validateBindReferral, referralsController.bindReferralCode);

// Get Coins - Summary
router.get('/coins/summary', generalLimiter, authenticate, referralsController.getCoinsSummary);

// Get Coins - Withdraw
router.post('/coins/withdraw', withdrawalLimiter, authenticate, referralsController.withdrawCoins);

// Get Coins - Records
router.get('/records', generalLimiter, authenticate, validatePagination, referralsController.getRecords);

// Get Cash - Summary
router.get('/cash/summary', generalLimiter, authenticate, referralsController.getCashSummary);

// Get Cash - Withdraw
router.post('/cash/withdraw', withdrawalLimiter, authenticate, validateWithdrawCash, referralsController.withdrawCash);

// Get Cash - Invite Records
router.get('/cash/invite-record', generalLimiter, authenticate, validatePagination, referralsController.getInviteRecords);

// Get Cash - Ranking
router.get('/cash/ranking', generalLimiter, authenticate, validatePagination, referralsController.getRanking);

export default router;
