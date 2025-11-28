/**
 * Authentication Controller
 * Developer: Hawkaye Visions LTD — Pakistan
 */

import { Request, Response, NextFunction } from 'express';
import jwt from 'jsonwebtoken';
import { config } from '../config';
import { AppError } from '../middleware/errorHandler';
import * as authService from '../services/authService';

// Send OTP
export const sendOtp = async (req: Request, res: Response, next: NextFunction) => {
  try {
    const { phone } = req.body;
    const result = await authService.sendOtp(phone);
    
    res.json({
      success: true,
      cooldownSeconds: result.cooldownSeconds,
      attemptsRemaining: result.attemptsRemaining
    });
  } catch (error) {
    next(error);
  }
};

// Verify OTP
export const verifyOtp = async (req: Request, res: Response, next: NextFunction) => {
  try {
    const { phone, otp } = req.body;
    const result = await authService.verifyOtp(phone, otp);
    
    if (!result.success) {
      throw new AppError('Invalid OTP', 401, 'INVALID_OTP');
    }
    
    // Generate JWT
    const token = jwt.sign(
      { userId: result.userId, phone },
      config.jwtSecret,
      { expiresIn: config.jwtExpiresIn }
    );
    
    const refreshToken = jwt.sign(
      { userId: result.userId },
      config.jwtRefreshSecret,
      { expiresIn: config.jwtRefreshExpiresIn }
    );
    
    res.json({
      success: true,
      token,
      refreshToken,
      user: {
        id: result.userId,
        name: result.userName,
        isNewUser: result.isNewUser
      }
    });
  } catch (error) {
    next(error);
  }
};

// Refresh token
export const refreshToken = async (req: Request, res: Response, next: NextFunction) => {
  try {
    const { refreshToken } = req.body;
    
    if (!refreshToken) {
      throw new AppError('Refresh token required', 400, 'REFRESH_TOKEN_REQUIRED');
    }
    
    const decoded = jwt.verify(refreshToken, config.jwtRefreshSecret) as { userId: string };
    
    const token = jwt.sign(
      { userId: decoded.userId },
      config.jwtSecret,
      { expiresIn: config.jwtExpiresIn }
    );
    
    res.json({ token });
  } catch (error) {
    next(new AppError('Invalid refresh token', 401, 'INVALID_REFRESH_TOKEN'));
  }
};

// Logout
export const logout = async (req: Request, res: Response, next: NextFunction) => {
  try {
    // In a real implementation, you would invalidate the token
    res.json({ success: true });
  } catch (error) {
    next(error);
  }
};
