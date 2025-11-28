/**
 * Medals Controller
 * Developer: Hawkaye Visions LTD — Pakistan
 */

import { Request, Response, NextFunction } from 'express';
import { AuthRequest } from '../middleware/auth';
import * as medalsService from '../services/medalsService';

// Get user's medals
export const getUserMedals = async (req: AuthRequest, res: Response, next: NextFunction) => {
  try {
    const userId = req.userId!;
    const medals = await medalsService.getUserMedals(userId);
    
    res.json({
      medals: medals.medals,
      displaySettings: medals.displaySettings
    });
  } catch (error) {
    next(error);
  }
};

// Update medal display order
export const updateMedalDisplay = async (req: AuthRequest, res: Response, next: NextFunction) => {
  try {
    const userId = req.userId!;
    const { displayedMedals, hiddenMedals } = req.body;
    
    await medalsService.updateMedalDisplay(userId, displayedMedals, hiddenMedals);
    
    res.json({ success: true });
  } catch (error) {
    next(error);
  }
};

// Get other user's medals
export const getOtherUserMedals = async (req: Request, res: Response, next: NextFunction) => {
  try {
    const { userId } = req.params;
    const medals = await medalsService.getUserMedals(userId);
    
    // Filter to only show displayed medals
    const displayedMedals = medals.medals.filter(m => m.isDisplayed);
    
    res.json({
      medals: displayedMedals
    });
  } catch (error) {
    next(error);
  }
};
