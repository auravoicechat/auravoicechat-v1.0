/**
 * KYC Routes
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Only ID Card (front/back) + Selfie - NO utility bills
 */

import { Router } from 'express';
import { authenticate } from '../middleware/auth';
import { generalLimiter } from '../middleware/rateLimiter';
import * as kycController from '../controllers/kycController';

const router = Router();

// Get KYC status
router.get('/status', generalLimiter, authenticate, kycController.getKycStatus);

// Submit KYC
router.post('/submit', generalLimiter, authenticate, kycController.submitKyc);

// Get upload URL for ID card front
router.post('/upload/id-front', generalLimiter, authenticate, kycController.getIdFrontUploadUrl);

// Get upload URL for ID card back
router.post('/upload/id-back', generalLimiter, authenticate, kycController.getIdBackUploadUrl);

// Get upload URL for selfie
router.post('/upload/selfie', generalLimiter, authenticate, kycController.getSelfieUploadUrl);

export default router;
