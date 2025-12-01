/**
 * Rate Limiting Middleware
 * Developer: Hawkaye Visions LTD — Pakistan
 */

import rateLimit from 'express-rate-limit';
import { config } from '../config';

// General rate limiter
export const generalLimiter = rateLimit({
  windowMs: config.rateLimit.windowMs,
  max: config.rateLimit.maxRequests,
  message: {
    error: {
      code: 'RATE_LIMIT_EXCEEDED',
      message: 'Too many requests. Please try again later.',
      retryAfter: Math.ceil(config.rateLimit.windowMs / 1000)
    }
  },
  standardHeaders: true,
  legacyHeaders: false
});

// Auth rate limiter - stricter for OTP
export const authLimiter = rateLimit({
  windowMs: 24 * 60 * 60 * 1000, // 24 hours
  max: 5, // 5 OTP requests per day
  message: {
    error: {
      code: 'OTP_LIMIT_EXCEEDED',
      message: 'Too many OTP requests. Please try again tomorrow.',
      retryAfter: 86400
    }
  },
  standardHeaders: true,
  legacyHeaders: false
});

// Referral rate limiter
export const referralLimiter = rateLimit({
  windowMs: 60 * 1000, // 1 minute
  max: 10, // 10 binds per minute
  message: {
    error: {
      code: 'RATE_LIMIT_EXCEEDED',
      message: 'Too many requests. Please try again later.',
      retryAfter: 60
    }
  },
  standardHeaders: true,
  legacyHeaders: false
});

// Withdrawal rate limiter
export const withdrawalLimiter = rateLimit({
  windowMs: 60 * 1000, // 1 minute
  max: 5, // 5 withdrawals per minute
  message: {
    error: {
      code: 'RATE_LIMIT_EXCEEDED',
      message: 'Too many withdrawal requests. Please try again later.',
      retryAfter: 60
    }
  },
  standardHeaders: true,
  legacyHeaders: false
});

// Gift sending rate limiter
export const giftLimiter = rateLimit({
  windowMs: 60 * 1000, // 1 minute
  max: 30, // 30 gift sends per minute
  message: {
    error: {
      code: 'RATE_LIMIT_EXCEEDED',
      message: 'Too many gift sends. Please try again later.',
      retryAfter: 60
    }
  },
  standardHeaders: true,
  legacyHeaders: false
});
