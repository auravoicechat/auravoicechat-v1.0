/**
 * VIP Routes
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Endpoints:
 * GET /vip/tier - Get VIP tier
 * POST /vip/purchase - Purchase VIP
 */

import { Router } from 'express';
import { authenticate } from '../middleware/auth';
import { generalLimiter } from '../middleware/rateLimiter';
import * as vipController from '../controllers/vipController';

const router = Router();

// Get VIP tier
router.get('/tier', generalLimiter, authenticate, vipController.getVipTier);

// Purchase VIP
router.post('/purchase', generalLimiter, authenticate, vipController.purchaseVip);

// Get VIP benefits
router.get('/benefits', generalLimiter, vipController.getVipBenefits);

export default router;
