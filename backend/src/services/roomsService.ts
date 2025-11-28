/**
 * Rooms Service
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Voice/Video rooms with 8/16 seat layouts
 */

import { v4 as uuidv4 } from 'uuid';

interface RoomCard {
  id: string;
  name: string;
  coverImage: string | null;
  ownerName: string;
  ownerAvatar: string | null;
  type: string;
  userCount: number;
  capacity: number;
  isLive: boolean;
  tags: string[];
}

interface Room {
  id: string;
  name: string;
  coverImage: string | null;
  ownerId: string;
  ownerName: string;
  ownerAvatar: string | null;
  type: string;
  mode: string;
  capacity: number;
  currentUsers: number;
  isLocked: boolean;
  tags: string[];
  seats: Seat[];
  createdAt: number;
}

interface Seat {
  position: number;
  userId: string | null;
  userName: string | null;
  userAvatar: string | null;
  userLevel: number | null;
  userVip: number | null;
  isMuted: boolean;
  isLocked: boolean;
}

// Mock rooms data
const mockRooms: RoomCard[] = [
  {
    id: 'room_1',
    name: 'Music Lounge 🎵',
    coverImage: null,
    ownerName: 'DJ Mike',
    ownerAvatar: null,
    type: 'MUSIC',
    userCount: 45,
    capacity: 100,
    isLive: true,
    tags: ['Music', 'Chill', 'English']
  },
  {
    id: 'room_2',
    name: 'Late Night Talk',
    coverImage: null,
    ownerName: 'Sarah',
    ownerAvatar: null,
    type: 'VOICE',
    userCount: 23,
    capacity: 50,
    isLive: true,
    tags: ['Talk', 'Dating']
  },
  {
    id: 'room_3',
    name: 'Gaming Zone 🎮',
    coverImage: null,
    ownerName: 'GamerPro',
    ownerAvatar: null,
    type: 'VIDEO',
    userCount: 67,
    capacity: 100,
    isLive: true,
    tags: ['Gaming', 'Fun']
  }
];

export const getPopularRooms = async (): Promise<RoomCard[]> => {
  return mockRooms;
};

export const getMyRooms = async (userId: string): Promise<RoomCard[]> => {
  return [];
};

export const getRoom = async (roomId: string): Promise<Room> => {
  const seats: Seat[] = Array(8).fill(null).map((_, i) => ({
    position: i,
    userId: i < 2 ? `user_${i}` : null,
    userName: i < 2 ? ['Alice', 'Bob'][i] : null,
    userAvatar: null,
    userLevel: i < 2 ? 25 - i * 7 : null,
    userVip: i === 0 ? 3 : null,
    isMuted: i === 1,
    isLocked: i === 5
  }));
  
  return {
    id: roomId,
    name: 'Music Lounge 🎵',
    coverImage: null,
    ownerId: 'owner_1',
    ownerName: 'DJ Mike',
    ownerAvatar: null,
    type: 'MUSIC',
    mode: 'FREE',
    capacity: 8,
    currentUsers: 2,
    isLocked: false,
    tags: ['Music', 'Chill'],
    seats,
    createdAt: Date.now()
  };
};

export const createRoom = async (
  userId: string,
  name: string,
  type: string,
  capacity: number
): Promise<Room> => {
  const roomId = uuidv4();
  return getRoom(roomId);
};

export const joinRoom = async (roomId: string, userId: string): Promise<void> => {
  // Add user to room
};

export const leaveRoom = async (roomId: string, userId: string): Promise<void> => {
  // Remove user from room
};

export const addToPlaylist = async (roomId: string, url: string): Promise<void> => {
  // Add video to playlist
};

export const exitVideo = async (roomId: string): Promise<void> => {
  // Exit video mode
};
