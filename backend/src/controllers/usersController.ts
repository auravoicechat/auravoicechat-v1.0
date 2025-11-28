/**
 * Users Controller
 * Developer: Hawkaye Visions LTD — Pakistan
 */

import { Request, Response, NextFunction } from 'express';
import { AuthRequest } from '../middleware/auth';
import * as usersService from '../services/usersService';

// Get user profile
export const getUser = async (req: Request, res: Response, next: NextFunction) => {
  try {
    const { userId } = req.params;
    const user = await usersService.getUser(userId);
    
    res.json(user);
  } catch (error) {
    next(error);
  }
};

// Update profile
export const updateProfile = async (req: AuthRequest, res: Response, next: NextFunction) => {
  try {
    const userId = req.userId!;
    const { name, bio, avatar } = req.body;
    
    await usersService.updateProfile(userId, { name, bio, avatar });
    
    res.json({ success: true });
  } catch (error) {
    next(error);
  }
};

// Follow user
export const followUser = async (req: AuthRequest, res: Response, next: NextFunction) => {
  try {
    const userId = req.userId!;
    const { userId: targetUserId } = req.params;
    
    await usersService.followUser(userId, targetUserId);
    
    res.json({ success: true });
  } catch (error) {
    next(error);
  }
};

// Unfollow user
export const unfollowUser = async (req: AuthRequest, res: Response, next: NextFunction) => {
  try {
    const userId = req.userId!;
    const { userId: targetUserId } = req.params;
    
    await usersService.unfollowUser(userId, targetUserId);
    
    res.json({ success: true });
  } catch (error) {
    next(error);
  }
};
