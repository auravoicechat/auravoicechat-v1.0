/**
 * VIP Service
 * Developer: Hawkaye Visions LTD — Pakistan
 */

interface VipData {
  tier: number;
  expiry: string;
}

// In-memory storage (use database in production)
const userVip = new Map<string, { tier: number; expiry: Date }>();

export const getVipTier = async (userId: string): Promise<VipData> => {
  const data = userVip.get(userId);
  
  if (!data || data.expiry < new Date()) {
    return {
      tier: 0,
      expiry: ''
    };
  }
  
  return {
    tier: data.tier,
    expiry: data.expiry.toISOString()
  };
};

export const purchaseVip = async (userId: string, tier: string, duration: string): Promise<void> => {
  const tierNum = parseInt(tier.replace('VIP', ''));
  const days = duration === '1m' ? 30 : duration === '3m' ? 90 : 365;
  
  const expiry = new Date();
  expiry.setDate(expiry.getDate() + days);
  
  userVip.set(userId, { tier: tierNum, expiry });
};
