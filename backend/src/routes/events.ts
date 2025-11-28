/**
 * Events Routes
 * Developer: Hawkaye Visions LTD — Pakistan
 */

import { Router } from 'express';
import { authenticate } from '../middleware/auth';
import { generalLimiter } from '../middleware/rateLimiter';
import * as eventsController from '../controllers/eventsController';

const router = Router();

router.use(generalLimiter);

// Public event info
router.get('/', eventsController.getEvents);
router.get('/active', eventsController.getActiveEvents);
router.get('/:eventId', eventsController.getEvent);
router.get('/:eventId/leaderboard', eventsController.getEventLeaderboard);

// Authenticated routes
router.use(authenticate);

router.post('/:eventId/join', eventsController.joinEvent);
router.get('/:eventId/progress', eventsController.getEventProgress);
router.post('/:eventId/claim/:milestoneId', eventsController.claimMilestone);
router.get('/:eventId/leaderboard/around-me', eventsController.getLeaderboardAroundMe);

// Lucky events
router.post('/lucky-bag/open', eventsController.openLuckyBag);
router.post('/lucky-draw/spin', eventsController.spinLuckyDraw);

export default router;
