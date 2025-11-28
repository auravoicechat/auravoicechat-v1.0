/**
 * Levels Routes
 * Developer: Hawkaye Visions LTD — Pakistan
 */

import { Router } from 'express';
import { authenticate } from '../middleware/auth';
import { generalLimiter } from '../middleware/rateLimiter';
import * as levelsController from '../controllers/levelsController';

const router = Router();

router.use(generalLimiter);

// Public level info
router.get('/rewards/:level', levelsController.getLevelRewards);
router.get('/leaderboard', levelsController.getLevelLeaderboard);

// Authenticated routes
router.use(authenticate);

router.get('/me', levelsController.getMyLevel);
router.get('/exp/history', levelsController.getExpHistory);
router.get('/exp/today', levelsController.getTodayExp);
router.post('/rewards/:level/claim', levelsController.claimLevelReward);
router.get('/leaderboard/friends', levelsController.getFriendsLeaderboard);

export default router;
