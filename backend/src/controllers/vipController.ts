/**
 * VIP Controller
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * VIP tiers 1-10 with multipliers 1.2x-3.0x
 */

import { Response, NextFunction } from 'express';
import { AuthRequest } from '../middleware/auth';
import * as vipService from '../services/vipService';

// VIP tier configuration
const VIP_TIERS = [
  { tier: 1, name: 'VIP 1', rechargeRequired: 10, multiplier: 1.2, expBoost: 0.10, features: ['vip_badge', 'priority_support'], monthlyCoins: 10000 },
  { tier: 2, name: 'VIP 2', rechargeRequired: 50, multiplier: 1.4, expBoost: 0.15, features: ['vip_badge', 'priority_support', 'exclusive_frame_7d'], monthlyCoins: 25000 },
  { tier: 3, name: 'VIP 3', rechargeRequired: 100, multiplier: 1.6, expBoost: 0.20, features: ['vip_badge', 'priority_support', 'exclusive_frame_7d', 'custom_id_discount'], monthlyCoins: 50000 },
  { tier: 4, name: 'VIP 4', rechargeRequired: 250, multiplier: 1.8, expBoost: 0.25, features: ['vip_badge', 'priority_support', 'exclusive_frame_14d', 'custom_id_discount', 'vip_vehicle_7d'], monthlyCoins: 100000 },
  { tier: 5, name: 'VIP 5', rechargeRequired: 500, multiplier: 2.0, expBoost: 0.30, features: ['vip_badge', 'priority_support', 'exclusive_frame_30d', 'custom_id_discount', 'vip_vehicle_14d', 'super_mic_access'], monthlyCoins: 200000 },
  { tier: 6, name: 'VIP 6', rechargeRequired: 1000, multiplier: 2.2, expBoost: 0.35, features: ['vip_badge', 'priority_support', 'exclusive_frame_30d', 'custom_id_free', 'vip_vehicle_30d', 'super_mic_access', 'vip_theme_14d'], monthlyCoins: 350000 },
  { tier: 7, name: 'VIP 7', rechargeRequired: 2500, multiplier: 2.4, expBoost: 0.40, features: ['vip_badge', 'priority_support', 'exclusive_frame_permanent', 'custom_id_free', 'vip_vehicle_30d', 'super_mic_access', 'vip_theme_30d', 'exclusive_games'], monthlyCoins: 500000 },
  { tier: 8, name: 'VIP 8', rechargeRequired: 5000, multiplier: 2.6, expBoost: 0.45, features: ['vip_badge', 'priority_support', 'exclusive_frame_permanent', 'custom_id_free', 'vip_vehicle_permanent', 'super_mic_access', 'vip_theme_30d', 'exclusive_games', 'vip_seat_effect'], monthlyCoins: 750000 },
  { tier: 9, name: 'VIP 9', rechargeRequired: 10000, multiplier: 2.8, expBoost: 0.50, features: ['svip_badge', 'priority_support', 'exclusive_frame_permanent', 'custom_id_free', 'vip_vehicle_permanent', 'super_mic_access', 'vip_theme_permanent', 'exclusive_games', 'vip_seat_effect', 'personal_manager'], monthlyCoins: 1000000 },
  { tier: 10, name: 'VIP 10 (SVIP)', rechargeRequired: 25000, multiplier: 3.0, expBoost: 0.60, features: ['svip_badge', 'priority_support', 'exclusive_frame_permanent', 'custom_id_free', 'svip_vehicle_permanent', 'super_mic_access', 'svip_theme_permanent', 'exclusive_games', 'svip_seat_effect', 'personal_manager', 'legendary_set'], monthlyCoins: 2000000 }
];

// Get VIP tier
export const getVipTier = async (req: AuthRequest, res: Response, next: NextFunction) => {
  try {
    const userId = req.userId!;
    const vipData = await vipService.getVipTier(userId);
    
    const tierConfig = VIP_TIERS.find(t => t.tier === vipData.tier) || VIP_TIERS[0];
    
    res.json({
      tier: `VIP${vipData.tier}`,
      multiplier: tierConfig.multiplier,
      expBoost: tierConfig.expBoost,
      expiry: vipData.expiry,
      benefits: tierConfig.features
    });
  } catch (error) {
    next(error);
  }
};

// Purchase VIP
export const purchaseVip = async (req: AuthRequest, res: Response, next: NextFunction) => {
  try {
    const userId = req.userId!;
    const { tier, duration } = req.body;
    
    await vipService.purchaseVip(userId, tier, duration);
    
    res.json({ success: true });
  } catch (error) {
    next(error);
  }
};

// Get VIP benefits
export const getVipBenefits = async (req: AuthRequest, res: Response, next: NextFunction) => {
  try {
    res.json({ tiers: VIP_TIERS });
  } catch (error) {
    next(error);
  }
};
