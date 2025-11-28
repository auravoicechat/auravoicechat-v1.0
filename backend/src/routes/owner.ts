/**
 * Owner Routes
 * Developer: Hawkaye Visions LTD — Pakistan
 */

import { Router } from 'express';
import { authenticate, requireOwner } from '../middleware/auth';
import { generalLimiter } from '../middleware/rateLimiter';
import * as ownerController from '../controllers/ownerController';

const router = Router();

// Apply rate limiting before authentication for security
router.use(generalLimiter);
router.use(authenticate);
router.use(requireOwner);

// Dashboard
router.get('/dashboard', ownerController.getDashboard);

// Economy
router.get('/economy/stats', ownerController.getEconomyStats);
router.put('/economy/settings', ownerController.updateEconomySettings);
router.get('/economy/presets', ownerController.getEconomyPresets);
router.post('/economy/presets/apply', ownerController.applyEconomyPreset);

// Revenue
router.get('/revenue/dashboard', ownerController.getRevenueDashboard);
router.get('/revenue/reports', ownerController.getRevenueReports);
router.put('/revenue/pricing', ownerController.updatePricing);

// Payouts
router.get('/payouts/queue', ownerController.getPayoutQueue);
router.post('/payouts/:id/approve', ownerController.approvePayout);
router.post('/payouts/:id/reject', ownerController.rejectPayout);
router.post('/payouts/batch', ownerController.batchApprovePayout);

// Features
router.get('/features', ownerController.getFeatures);
router.put('/features/:feature', ownerController.updateFeature);
router.get('/feature-flags', ownerController.getFeatureFlags);
router.put('/feature-flags/:flag', ownerController.updateFeatureFlag);

// Staff Management
router.get('/country-admins', ownerController.getCountryAdmins);
router.post('/country-admins/:userId/assign', ownerController.assignCountryAdmin);
router.post('/country-admins/:userId/revoke', ownerController.revokeCountryAdmin);
router.get('/admins', ownerController.getAllAdmins);
router.post('/admins', ownerController.createAdmin);
router.put('/admins/:id', ownerController.updateAdmin);
router.delete('/admins/:id', ownerController.deleteAdmin);
router.get('/admins/:id/logs', ownerController.getAdminLogs);

// System
router.get('/system/settings', ownerController.getSystemSettings);
router.put('/system/settings', ownerController.updateSystemSettings);
router.post('/system/maintenance', ownerController.toggleMaintenance);

// Versioning
router.get('/versions/:area', ownerController.getVersionHistory);
router.post('/versions/:area/rollback', ownerController.rollback);

// Reseller Management
router.get('/resellers', ownerController.getResellers);
router.post('/resellers/:userId/assign', ownerController.assignReseller);
router.post('/resellers/:userId/revoke', ownerController.revokeReseller);
router.put('/resellers/:userId/tier', ownerController.updateResellerTier);

// Guide Management
router.get('/guide-program', ownerController.getGuideProgram);
router.put('/guide-program', ownerController.updateGuideProgram);

export default router;
