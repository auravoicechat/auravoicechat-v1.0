/**
 * Admin Routes
 * Developer: Hawkaye Visions LTD — Pakistan
 */

import { Router } from 'express';
import { authenticate, requireAdmin, requireCountryAdmin, requireOwner } from '../middleware/auth';
import { generalLimiter } from '../middleware/rateLimiter';
import * as adminController from '../controllers/adminController';

const router = Router();

// Apply rate limiting before authentication for security
router.use(generalLimiter);
router.use(authenticate);

// Dashboard (Admin+)
router.get('/dashboard', requireAdmin, adminController.getDashboard);
router.get('/stats', requireAdmin, adminController.getStats);

// User Management (Admin+)
router.get('/users/search', requireAdmin, adminController.searchUsers);
router.get('/users/:userId', requireAdmin, adminController.getUser);
router.post('/users/:userId/warn', requireAdmin, adminController.warnUser);
router.post('/users/:userId/mute', requireAdmin, adminController.muteUser);
router.post('/users/:userId/coins', requireAdmin, adminController.adjustCoins);

// Bans (Country Admin+)
router.post('/users/:userId/ban', requireCountryAdmin, adminController.banUser);
router.post('/users/:userId/unban', requireCountryAdmin, adminController.unbanUser);

// Reports (Admin+)
router.get('/reports', requireAdmin, adminController.getReports);
router.get('/reports/:reportId', requireAdmin, adminController.getReport);
router.post('/reports/:reportId/action', requireAdmin, adminController.actOnReport);

// Tickets (Admin+)
router.get('/tickets', requireAdmin, adminController.getTickets);
router.get('/tickets/:ticketId', requireAdmin, adminController.getTicket);
router.post('/tickets/:ticketId/respond', requireAdmin, adminController.respondToTicket);
router.post('/tickets/:ticketId/close', requireAdmin, adminController.closeTicket);

// Rooms (Admin+)
router.get('/rooms/search', requireAdmin, adminController.searchRooms);
router.get('/rooms/:roomId', requireAdmin, adminController.getRoom);
router.post('/rooms/:roomId/close', requireCountryAdmin, adminController.closeRoom);

// Announcements (Admin+)
router.get('/announcements', requireAdmin, adminController.getAnnouncements);
router.post('/announcements', requireAdmin, adminController.createAnnouncement);
router.put('/announcements/:id', requireAdmin, adminController.updateAnnouncement);
router.delete('/announcements/:id', requireAdmin, adminController.deleteAnnouncement);

// Logs (Admin+)
router.get('/logs', requireAdmin, adminController.getLogs);

// Staff Management (Country Admin+)
router.get('/guides', requireCountryAdmin, adminController.getGuides);
router.post('/guides/:userId/assign', requireCountryAdmin, adminController.assignGuide);
router.post('/guides/:userId/revoke', requireCountryAdmin, adminController.revokeGuide);

// Admin Management (Country Admin+)
router.get('/admins', requireCountryAdmin, adminController.getAdmins);
router.post('/admins/:userId/assign', requireCountryAdmin, adminController.assignAdmin);
router.post('/admins/:userId/revoke', requireCountryAdmin, adminController.revokeAdmin);

export default router;
