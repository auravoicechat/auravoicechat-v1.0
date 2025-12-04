/**
 * Earning Targets & Cashout Controller - Enhanced
 * Target-based earning system with owner approval
 */

import { Request, Response } from 'express';
import { PrismaClient } from '@prisma/client';
import { logger } from '../utils/logger';

const prisma = new PrismaClient();

/**
 * Get user earning targets
 */
export const getUserTargets = async (req: Request, res: Response) => {
  try {
    const userId = req.user?.id;

    if (!userId) {
      return res.status(401).json({ success: false, error: 'Unauthorized' });
    }

    // Get user's current diamonds
    const user = await prisma.user.findUnique({
      where: { id: userId },
      select: { diamonds: true, role: true }
    });

    const targetType = user?.role === 'guide' ? 'guide' : 'user';

    const targets = await prisma.earningTarget.findMany({
      where: {
        targetType,
        isActive: true
      },
      orderBy: { tier: 'asc' }
    });

    const userDiamonds = user?.diamonds || BigInt(0);

    // Calculate progress for each target
    const targetsWithProgress = targets.map(target => ({
      ...target,
      progress: Number(userDiamonds) / Number(target.diamondsRequired),
      isCompleted: userDiamonds >= target.diamondsRequired,
      diamondsNeeded: Math.max(0, Number(target.diamondsRequired) - Number(userDiamonds))
    }));

    res.json({
      success: true,
      data: {
        currentDiamonds: userDiamonds.toString(),
        targets: targetsWithProgress
      }
    });
  } catch (error) {
    logger.error('Error fetching user targets:', error);
    res.status(500).json({ success: false, error: 'Failed to fetch targets' });
  }
};

/**
 * Request cashout
 */
export const requestCashout = async (req: Request, res: Response) => {
  try {
    const userId = req.user?.id;
    const { amount, paymentMethod, paymentDetails } = req.body;

    if (!userId) {
      return res.status(401).json({ success: false, error: 'Unauthorized' });
    }

    // Get user and validate
    const user = await prisma.user.findUnique({
      where: { id: userId },
      select: { diamonds: true }
    });

    if (!user) {
      return res.status(404).json({ success: false, error: 'User not found' });
    }

    // Get conversion rate and limits
    const [ratesConfig, limitsConfig] = await Promise.all([
      prisma.economyConfig.findUnique({ where: { key: 'conversion_rates' } }),
      prisma.economyConfig.findUnique({ where: { key: 'system_limits' } })
    ]);

    const minCashout = limitsConfig?.minCashout || 10;
    const maxCashout = limitsConfig?.maxCashout || 10000;
    const clearanceDays = limitsConfig?.clearanceDays || 5;
    const diamondToCashRate = ratesConfig?.diamondToCashRate || 0.0003;

    // Validate amount
    if (amount < minCashout || amount > maxCashout) {
      return res.status(400).json({ 
        success: false, 
        error: `Cashout amount must be between $${minCashout} and $${maxCashout}` 
      });
    }

    // Calculate diamonds needed
    const diamondsNeeded = BigInt(Math.ceil(amount / Number(diamondToCashRate)));

    if (user.diamonds < diamondsNeeded) {
      return res.status(400).json({ 
        success: false, 
        error: 'Insufficient diamonds for this cashout amount' 
      });
    }

    // Calculate clearance date
    const clearanceDate = new Date();
    clearanceDate.setDate(clearanceDate.getDate() + clearanceDays);

    // Create cashout request
    const request = await prisma.cashoutRequest.create({
      data: {
        userId,
        amount,
        diamondsUsed: diamondsNeeded,
        paymentMethod,
        paymentDetails,
        status: 'pending',
        clearanceDate
      }
    });

    // Deduct diamonds from user
    await prisma.user.update({
      where: { id: userId },
      data: {
        diamonds: {
          decrement: diamondsNeeded
        }
      }
    });

    // Create transaction record
    await prisma.transaction.create({
      data: {
        userId,
        type: 'withdrawal',
        status: 'pending',
        currency: 'usd',
        amount,
        description: `Cashout request - ${diamondsNeeded.toString()} diamonds`,
        reference: request.id
      }
    });

    logger.info(`Cashout requested: ${amount} USD by user ${userId}`);

    res.status(201).json({
      success: true,
      data: request,
      message: `Cashout request created. Clearance period: ${clearanceDays} days`
    });
  } catch (error) {
    logger.error('Error requesting cashout:', error);
    res.status(500).json({ success: false, error: 'Failed to request cashout' });
  }
};

/**
 * Get user's cashout requests
 */
export const getMyCashouts = async (req: Request, res: Response) => {
  try {
    const userId = req.user?.id;

    if (!userId) {
      return res.status(401).json({ success: false, error: 'Unauthorized' });
    }

    const requests = await prisma.cashoutRequest.findMany({
      where: { userId },
      orderBy: { requestedAt: 'desc' }
    });

    res.json({
      success: true,
      data: requests
    });
  } catch (error) {
    logger.error('Error fetching cashouts:', error);
    res.status(500).json({ success: false, error: 'Failed to fetch cashouts' });
  }
};

/**
 * Get earning history
 */
export const getEarningHistory = async (req: Request, res: Response) => {
  try {
    const userId = req.user?.id;
    const { page = 1, limit = 20 } = req.query;

    if (!userId) {
      return res.status(401).json({ success: false, error: 'Unauthorized' });
    }

    const offset = (Number(page) - 1) * Number(limit);

    const [transactions, total] = await Promise.all([
      prisma.transaction.findMany({
        where: {
          userId,
          type: { in: ['reward', 'gift', 'withdrawal'] }
        },
        orderBy: { createdAt: 'desc' },
        take: Number(limit),
        skip: offset
      }),
      prisma.transaction.count({
        where: {
          userId,
          type: { in: ['reward', 'gift', 'withdrawal'] }
        }
      })
    ]);

    res.json({
      success: true,
      data: {
        transactions,
        pagination: {
          page: Number(page),
          limit: Number(limit),
          total,
          pages: Math.ceil(total / Number(limit))
        }
      }
    });
  } catch (error) {
    logger.error('Error fetching earning history:', error);
    res.status(500).json({ success: false, error: 'Failed to fetch history' });
  }
};

/**
 * Get earning statistics
 */
export const getEarningStats = async (req: Request, res: Response) => {
  try {
    const userId = req.user?.id;

    if (!userId) {
      return res.status(401).json({ success: false, error: 'Unauthorized' });
    }

    const user = await prisma.user.findUnique({
      where: { id: userId },
      select: {
        diamonds: true,
        giftsReceived: true
      }
    });

    // Get transaction stats
    const [todayEarnings, weekEarnings, monthEarnings, totalEarnings] = await Promise.all([
      prisma.transaction.aggregate({
        where: {
          userId,
          type: { in: ['reward', 'gift'] },
          createdAt: {
            gte: new Date(new Date().setHours(0, 0, 0, 0))
          }
        },
        _sum: { amount: true }
      }),
      prisma.transaction.aggregate({
        where: {
          userId,
          type: { in: ['reward', 'gift'] },
          createdAt: {
            gte: new Date(Date.now() - 7 * 24 * 60 * 60 * 1000)
          }
        },
        _sum: { amount: true }
      }),
      prisma.transaction.aggregate({
        where: {
          userId,
          type: { in: ['reward', 'gift'] },
          createdAt: {
            gte: new Date(new Date().setDate(1))
          }
        },
        _sum: { amount: true }
      }),
      prisma.transaction.aggregate({
        where: {
          userId,
          type: { in: ['reward', 'gift'] }
        },
        _sum: { amount: true }
      })
    ]);

    res.json({
      success: true,
      data: {
        currentDiamonds: user?.diamonds.toString() || '0',
        totalGiftsReceived: user?.giftsReceived.toString() || '0',
        today: todayEarnings._sum.amount || 0,
        thisWeek: weekEarnings._sum.amount || 0,
        thisMonth: monthEarnings._sum.amount || 0,
        allTime: totalEarnings._sum.amount || 0
      }
    });
  } catch (error) {
    logger.error('Error fetching earning stats:', error);
    res.status(500).json({ success: false, error: 'Failed to fetch stats' });
  }
};

/**
 * Exchange diamonds to coins
 */
export const exchangeDiamondsToCoins = async (req: Request, res: Response) => {
  try {
    const userId = req.user?.id;
    const { diamonds } = req.body;

    if (!userId) {
      return res.status(401).json({ success: false, error: 'Unauthorized' });
    }

    const user = await prisma.user.findUnique({
      where: { id: userId },
      select: { diamonds: true }
    });

    if (!user || user.diamonds < BigInt(diamonds)) {
      return res.status(400).json({ success: false, error: 'Insufficient diamonds' });
    }

    // Get conversion rate
    const config = await prisma.economyConfig.findUnique({
      where: { key: 'conversion_rates' }
    });

    const conversionRate = config?.diamondToCoinRate || 30; // 30% by default
    const coinsToAdd = BigInt(Math.floor(Number(diamonds) * (Number(conversionRate) / 100)));

    // Update user balances
    await prisma.user.update({
      where: { id: userId },
      data: {
        diamonds: {
          decrement: BigInt(diamonds)
        },
        coins: {
          increment: coinsToAdd
        }
      }
    });

    // Create transaction record
    await prisma.transaction.create({
      data: {
        userId,
        type: 'exchange',
        status: 'completed',
        currency: 'diamonds',
        amount: diamonds,
        description: `Exchanged ${diamonds} diamonds to ${coinsToAdd.toString()} coins`
      }
    });

    logger.info(`Diamonds exchanged: ${diamonds} → ${coinsToAdd.toString()} coins by user ${userId}`);

    res.json({
      success: true,
      data: {
        diamondsExchanged: diamonds,
        coinsReceived: coinsToAdd.toString()
      },
      message: 'Diamonds exchanged successfully'
    });
  } catch (error) {
    logger.error('Error exchanging diamonds:', error);
    res.status(500).json({ success: false, error: 'Failed to exchange diamonds' });
  }
};
