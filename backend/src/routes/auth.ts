/**
 * Authentication Routes
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Endpoints:
 * POST /auth/otp/send - Send OTP
 * POST /auth/otp/verify - Verify OTP
 * POST /auth/refresh - Refresh JWT token
 * POST /auth/logout - Logout user
 * POST /auth/google - Google Sign-In
 * POST /auth/facebook - Facebook Sign-In
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

// Social Sign-In - Google
router.post('/google', authLimiter, authController.signInWithGoogle);

// Social Sign-In - Facebook
router.post('/facebook', authLimiter, authController.signInWithFacebook);

export default router;
