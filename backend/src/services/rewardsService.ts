/**
 * Rewards Service
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * 7-day cycle: Day 1 (5K) → Day 7 (50K)
 * VIP multipliers applied
 */

// Daily reward schedule
const DAILY_REWARDS = [5000, 10000, 15000, 20000, 25000, 30000, 50000];
const VIP_MULTIPLIERS: Record<number, number> = {
  0: 1.0, 1: 1.2, 2: 1.4, 3: 1.6, 4: 1.8, 5: 2.0,
  6: 2.2, 7: 2.4, 8: 2.6, 9: 2.8, 10: 3.0
};

interface DailyStatus {
  currentDay: number;
  claimable: boolean;
  streak: number;
  nextResetUtc: string;
  vipTier: number;
}

interface ClaimResult {
  day: number;
  baseCoins: number;
  vipMultiplier: number;
  totalCoins: number;
}

// In-memory storage (use database in production)
const userRewards = new Map<string, { lastClaim: Date; currentDay: number; streak: number }>();

export const getDailyStatus = async (userId: string): Promise<DailyStatus> => {
  const now = new Date();
  const resetHour = 0; // Midnight UTC
  
  const userData = userRewards.get(userId) || {
    lastClaim: new Date(0),
    currentDay: 1,
    streak: 0
  };
  
  const lastClaimDate = userData.lastClaim.toDateString();
  const todayDate = now.toDateString();
  const claimable = lastClaimDate !== todayDate;
  
  // Calculate next reset
  const nextReset = new Date(now);
  nextReset.setUTCHours(resetHour, 0, 0, 0);
  if (nextReset <= now) {
    nextReset.setDate(nextReset.getDate() + 1);
  }
  
  return {
    currentDay: userData.currentDay,
    claimable,
    streak: userData.streak,
    nextResetUtc: nextReset.toISOString(),
    vipTier: 5 // Get from user profile in production
  };
};

export const claimDailyReward = async (userId: string): Promise<ClaimResult> => {
  const status = await getDailyStatus(userId);
  
  if (!status.claimable) {
    throw new Error('Already claimed today');
  }
  
  const baseCoins = DAILY_REWARDS[status.currentDay - 1];
  const vipMultiplier = VIP_MULTIPLIERS[status.vipTier] || 1.0;
  const totalCoins = Math.floor(baseCoins * vipMultiplier);
  
  // Update user rewards
  const nextDay = status.currentDay >= 7 ? 1 : status.currentDay + 1;
  userRewards.set(userId, {
    lastClaim: new Date(),
    currentDay: nextDay,
    streak: status.streak + 1
  });
  
  return {
    day: status.currentDay,
    baseCoins,
    vipMultiplier,
    totalCoins
  };
};
