/**
 * Authentication Routes
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Endpoints:
 * POST /auth/otp/send - Send OTP
 * POST /auth/otp/verify - Verify OTP
 */

import { Router } from 'express';
import { authLimiter, generalLimiter } from '../middleware/rateLimiter';
import { validateSendOtp, validateVerifyOtp } from '../middleware/validation';
import * as authController from '../controllers/authController';

const router = Router();

// Send OTP
router.post('/otp/send', authLimiter, validateSendOtp, authController.sendOtp);

// Verify OTP
router.post('/otp/verify', authLimiter, validateVerifyOtp, authController.verifyOtp);

// Refresh token
router.post('/refresh', generalLimiter, authController.refreshToken);

// Logout
router.post('/logout', generalLimiter, authController.logout);

export default router;
