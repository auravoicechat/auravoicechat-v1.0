/**
 * Daily Rewards Controller
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * 7-day cycle: Day 1 (5K) → Day 7 (50K)
 * VIP multipliers: 1.2x (VIP1) → 3.0x (VIP10)
 */

import { Response, NextFunction } from 'express';
import { AuthRequest } from '../middleware/auth';
import * as rewardsService from '../services/rewardsService';

// Daily reward schedule
const DAILY_REWARDS = [
  { day: 1, coins: 5000 },
  { day: 2, coins: 10000 },
  { day: 3, coins: 15000 },
  { day: 4, coins: 20000 },
  { day: 5, coins: 25000 },
  { day: 6, coins: 30000 },
  { day: 7, base: 35000, bonus: 15000, total: 50000 }
];

// VIP multipliers
const VIP_MULTIPLIERS: Record<number, number> = {
  0: 1.0, 1: 1.2, 2: 1.4, 3: 1.6, 4: 1.8, 5: 2.0,
  6: 2.2, 7: 2.4, 8: 2.6, 9: 2.8, 10: 3.0
};

// Get daily status
export const getDailyStatus = async (req: AuthRequest, res: Response, next: NextFunction) => {
  try {
    const userId = req.userId!;
    const status = await rewardsService.getDailyStatus(userId);
    
    const vipMultiplier = VIP_MULTIPLIERS[status.vipTier] || 1.0;
    
    const cycle = DAILY_REWARDS.map((reward, index) => {
      const day = index + 1;
      let rewardStatus = 'LOCKED';
      if (day < status.currentDay) rewardStatus = 'CLAIMED';
      else if (day === status.currentDay && status.claimable) rewardStatus = 'CLAIMABLE';
      
      return {
        day,
        coins: 'total' in reward ? reward.total : reward.coins,
        ...(reward.bonus && { bonus: reward.bonus }),
        status: rewardStatus
      };
    });
    
    res.json({
      currentDay: status.currentDay,
      claimable: status.claimable,
      cycle,
      streak: status.streak,
      nextResetUtc: status.nextResetUtc,
      vipTier: `VIP${status.vipTier}`,
      vipMultiplier
    });
  } catch (error) {
    next(error);
  }
};

// Claim reward
export const claimDailyReward = async (req: AuthRequest, res: Response, next: NextFunction) => {
  try {
    const userId = req.userId!;
    const result = await rewardsService.claimDailyReward(userId);
    
    res.json({
      success: true,
      day: result.day,
      baseCoins: result.baseCoins,
      vipMultiplier: result.vipMultiplier,
      totalCoins: result.totalCoins
    });
  } catch (error) {
    next(error);
  }
};
