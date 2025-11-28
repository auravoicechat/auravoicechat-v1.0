/**
 * Wallet Controller
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Exchange rate: 30% (100,000 diamonds → 30,000 coins)
 */

import { Response, NextFunction } from 'express';
import { AuthRequest } from '../middleware/auth';
import { AppError } from '../middleware/errorHandler';
import * as walletService from '../services/walletService';

const EXCHANGE_RATE = 0.30; // 30%

// Get balances
export const getBalances = async (req: AuthRequest, res: Response, next: NextFunction) => {
  try {
    const userId = req.userId!;
    const balances = await walletService.getBalances(userId);
    
    res.json({
      coins: balances.coins,
      diamonds: balances.diamonds,
      lastUpdated: new Date().toISOString()
    });
  } catch (error) {
    next(error);
  }
};

// Exchange diamonds to coins
export const exchangeDiamondsToCoins = async (req: AuthRequest, res: Response, next: NextFunction) => {
  try {
    const userId = req.userId!;
    const { diamonds } = req.body;
    
    // Validate balance
    const balances = await walletService.getBalances(userId);
    if (balances.diamonds < diamonds) {
      throw new AppError('Insufficient diamonds', 400, 'INSUFFICIENT_BALANCE');
    }
    
    const coinsReceived = Math.floor(diamonds * EXCHANGE_RATE);
    const result = await walletService.exchangeDiamondsToCoins(userId, diamonds, coinsReceived);
    
    res.json({
      success: true,
      diamondsUsed: diamonds,
      coinsReceived,
      newBalance: {
        coins: result.newCoins,
        diamonds: result.newDiamonds
      }
    });
  } catch (error) {
    next(error);
  }
};

// Get transaction history
export const getTransactions = async (req: AuthRequest, res: Response, next: NextFunction) => {
  try {
    const userId = req.userId!;
    const page = parseInt(req.query.page as string) || 1;
    const pageSize = parseInt(req.query.pageSize as string) || 20;
    
    const result = await walletService.getTransactions(userId, page, pageSize);
    
    res.json({
      data: result.transactions,
      pagination: {
        page,
        pageSize,
        totalItems: result.totalCount,
        totalPages: Math.ceil(result.totalCount / pageSize)
      }
    });
  } catch (error) {
    next(error);
  }
};

// Transfer coins
export const transferCoins = async (req: AuthRequest, res: Response, next: NextFunction) => {
  try {
    const userId = req.userId!;
    const { toUserId, amount } = req.body;
    
    await walletService.transferCoins(userId, toUserId, amount);
    
    res.json({ success: true });
  } catch (error) {
    next(error);
  }
};
