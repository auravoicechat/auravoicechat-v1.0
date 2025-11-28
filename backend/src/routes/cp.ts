/**
 * CP (Couple Partnership) Routes
 * Developer: Hawkaye Visions LTD — Pakistan
 */

import { Router } from 'express';
import { authenticate } from '../middleware/auth';
import { generalLimiter } from '../middleware/rateLimiter';
import * as cpController from '../controllers/cpController';

const router = Router();

// Apply rate limiting before authentication for security
router.use(generalLimiter);
router.use(authenticate);

// CP Management
router.get('/my', cpController.getMyCP);
router.post('/request', cpController.sendCPRequest);
router.post('/accept/:requestId', cpController.acceptCPRequest);
router.post('/reject/:requestId', cpController.rejectCPRequest);
router.post('/dissolve', cpController.dissolveCP);

// CP Progress
router.get('/level', cpController.getCPLevel);
router.get('/rewards', cpController.getCPRewards);
router.post('/rewards/:rewardId/claim', cpController.claimReward);

// CP Rankings
router.get('/ranking', cpController.getCPRanking);

export default router;
