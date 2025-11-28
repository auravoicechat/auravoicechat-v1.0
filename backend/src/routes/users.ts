/**
 * Users Routes
 * Developer: Hawkaye Visions LTD — Pakistan
 */

import { Router } from 'express';
import { authenticate, optionalAuth } from '../middleware/auth';
import { generalLimiter } from '../middleware/rateLimiter';
import * as usersController from '../controllers/usersController';
import * as medalsController from '../controllers/medalsController';

const router = Router();

// Get user profile
router.get('/:userId', generalLimiter, optionalAuth, usersController.getUser);

// Update profile
router.put('/me', generalLimiter, authenticate, usersController.updateProfile);

// Get user's medals
router.get('/:userId/medals', generalLimiter, optionalAuth, medalsController.getOtherUserMedals);

// Follow user
router.post('/:userId/follow', generalLimiter, authenticate, usersController.followUser);

// Unfollow user
router.delete('/:userId/follow', generalLimiter, authenticate, usersController.unfollowUser);

export default router;
