/**
 * Authentication Service
 * Developer: Hawkaye Visions LTD — Pakistan
 */

import { v4 as uuidv4 } from 'uuid';
import { config } from '../config';
import { logger } from '../utils/logger';

interface OtpResult {
  success: boolean;
  cooldownSeconds: number;
  attemptsRemaining: number;
}

interface VerifyResult {
  success: boolean;
  userId: string;
  userName: string;
  isNewUser: boolean;
}

// In-memory OTP storage (use Redis in production)
const otpStore = new Map<string, { otp: string; expires: number }>();

// Generate random OTP
const generateOtp = (): string => {
  return Math.floor(100000 + Math.random() * 900000).toString();
};

export const sendOtp = async (phone: string): Promise<OtpResult> => {
  const otp = generateOtp();
  const expires = Date.now() + 5 * 60 * 1000; // 5 minutes
  
  otpStore.set(phone, { otp, expires });
  
  // In production, send OTP via Twilio
  // Development only: log OTP for testing (never in production)
  if (config.nodeEnv === 'development') {
    logger.debug(`[DEV ONLY] OTP for testing: ${phone}`);
  }
  
  // TODO: Implement Twilio SMS sending
  // await twilioClient.messages.create({
  //   body: `Your Aura Voice Chat verification code is: ${otp}`,
  //   from: config.twilio.phoneNumber,
  //   to: phone
  // });
  
  return {
    success: true,
    cooldownSeconds: 60,
    attemptsRemaining: 4
  };
};

export const verifyOtp = async (phone: string, otp: string): Promise<VerifyResult> => {
  const stored = otpStore.get(phone);
  
  if (!stored || stored.expires < Date.now()) {
    return {
      success: false,
      userId: '',
      userName: '',
      isNewUser: false
    };
  }
  
  if (stored.otp !== otp) {
    return {
      success: false,
      userId: '',
      userName: '',
      isNewUser: false
    };
  }
  
  // Clear OTP after successful verification
  otpStore.delete(phone);
  
  // Create or get user
  const userId = uuidv4();
  const isNewUser = true; // Check database in production
  
  return {
    success: true,
    userId,
    userName: `User_${userId.slice(0, 6)}`,
    isNewUser
  };
};
