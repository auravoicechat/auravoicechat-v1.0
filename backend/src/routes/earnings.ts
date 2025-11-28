/**
 * Earning System Routes
 * Developer: Hawkaye Visions LTD — Pakistan
 */

import { Router } from 'express';
import { authenticate } from '../middleware/auth';
import { generalLimiter } from '../middleware/rateLimiter';
import * as earningsController from '../controllers/earningsController';

const router = Router();

// Apply rate limiting before authentication for security
router.use(generalLimiter);
router.use(authenticate);

// Earning Targets
router.get('/targets', earningsController.getTargets);
router.get('/targets/active', earningsController.getActiveTargets);
router.get('/targets/:targetId/progress', earningsController.getTargetProgress);
router.post('/targets/:targetId/activate', earningsController.activateTarget);

// Earning Wallet
router.get('/wallet', earningsController.getEarningWallet);
router.get('/history', earningsController.getEarningHistory);
router.get('/pending', earningsController.getPendingEarnings);

// Withdrawals
router.post('/withdraw', earningsController.requestWithdrawal);
router.get('/withdraw/methods', earningsController.getWithdrawalMethods);
router.get('/withdraw/history', earningsController.getWithdrawalHistory);
router.get('/withdraw/:withdrawalId/status', earningsController.getWithdrawalStatus);

// Payment Methods
router.get('/payment-methods', earningsController.getPaymentMethods);
router.post('/payment-methods', earningsController.addPaymentMethod);
router.put('/payment-methods/:methodId', earningsController.updatePaymentMethod);
router.delete('/payment-methods/:methodId', earningsController.deletePaymentMethod);

export default router;
