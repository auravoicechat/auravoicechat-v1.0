/**
 * Reseller Routes
 * Developer: Hawkaye Visions LTD — Pakistan
 */

import { Router } from 'express';
import { authenticate, requireSeller } from '../middleware/auth';
import { generalLimiter } from '../middleware/rateLimiter';
import * as resellerController from '../controllers/resellerController';

const router = Router();

// Public routes
router.get('/list', generalLimiter, resellerController.getResellerList);
router.get('/:sellerId/profile', generalLimiter, resellerController.getResellerProfile);

// Authenticated routes
router.use(authenticate);
router.use(generalLimiter);

router.post('/:sellerId/contact', resellerController.contactReseller);
router.post('/:sellerId/rate', resellerController.rateReseller);

// Seller-only routes
router.get('/status', requireSeller, resellerController.getSellerStatus);
router.get('/dashboard', requireSeller, resellerController.getSellerDashboard);
router.get('/inventory', requireSeller, resellerController.getInventory);
router.get('/packages', requireSeller, resellerController.getPackages);
router.post('/purchase', requireSeller, resellerController.purchasePackage);
router.get('/orders', requireSeller, resellerController.getOrders);
router.post('/orders/create', requireSeller, resellerController.createOrder);
router.put('/orders/:orderId/confirm', requireSeller, resellerController.confirmOrder);
router.put('/orders/:orderId/cancel', requireSeller, resellerController.cancelOrder);
router.get('/transactions', requireSeller, resellerController.getTransactions);
router.get('/bonuses', requireSeller, resellerController.getBonuses);

export default router;
