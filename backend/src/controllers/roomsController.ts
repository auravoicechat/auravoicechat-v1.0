/**
 * Rooms Controller
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Voice/Video rooms with 8/16 seat layouts
 */

import { Request, Response, NextFunction } from 'express';
import { AuthRequest } from '../middleware/auth';
import * as roomsService from '../services/roomsService';

// Get popular rooms
export const getPopularRooms = async (req: Request, res: Response, next: NextFunction) => {
  try {
    const rooms = await roomsService.getPopularRooms();
    
    res.json({
      data: rooms,
      pagination: {
        page: 1,
        pageSize: 20,
        totalItems: rooms.length,
        totalPages: 1
      }
    });
  } catch (error) {
    next(error);
  }
};

// Get my rooms
export const getMyRooms = async (req: AuthRequest, res: Response, next: NextFunction) => {
  try {
    const userId = req.userId!;
    const rooms = await roomsService.getMyRooms(userId);
    
    res.json({
      data: rooms,
      pagination: {
        page: 1,
        pageSize: 20,
        totalItems: rooms.length,
        totalPages: 1
      }
    });
  } catch (error) {
    next(error);
  }
};

// Get room by ID
export const getRoom = async (req: Request, res: Response, next: NextFunction) => {
  try {
    const { roomId } = req.params;
    const room = await roomsService.getRoom(roomId);
    
    res.json(room);
  } catch (error) {
    next(error);
  }
};

// Create room
export const createRoom = async (req: AuthRequest, res: Response, next: NextFunction) => {
  try {
    const userId = req.userId!;
    const { name, type, capacity } = req.body;
    
    const room = await roomsService.createRoom(userId, name, type, capacity);
    
    res.status(201).json(room);
  } catch (error) {
    next(error);
  }
};

// Join room
export const joinRoom = async (req: AuthRequest, res: Response, next: NextFunction) => {
  try {
    const userId = req.userId!;
    const { roomId } = req.params;
    
    await roomsService.joinRoom(roomId, userId);
    
    res.json({ success: true });
  } catch (error) {
    next(error);
  }
};

// Leave room
export const leaveRoom = async (req: AuthRequest, res: Response, next: NextFunction) => {
  try {
    const userId = req.userId!;
    const { roomId } = req.params;
    
    await roomsService.leaveRoom(roomId, userId);
    
    res.json({ success: true });
  } catch (error) {
    next(error);
  }
};

// Add to playlist
export const addToPlaylist = async (req: AuthRequest, res: Response, next: NextFunction) => {
  try {
    const { roomId } = req.params;
    const { url } = req.body;
    
    await roomsService.addToPlaylist(roomId, url);
    
    res.json({ success: true });
  } catch (error) {
    next(error);
  }
};

// Exit video
export const exitVideo = async (req: AuthRequest, res: Response, next: NextFunction) => {
  try {
    const { roomId } = req.params;
    
    await roomsService.exitVideo(roomId);
    
    res.json({ success: true });
  } catch (error) {
    next(error);
  }
};
