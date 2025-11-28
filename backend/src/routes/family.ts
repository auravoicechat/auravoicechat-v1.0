/**
 * Family Routes
 * Developer: Hawkaye Visions LTD — Pakistan
 */

import { Router } from 'express';
import { authenticate } from '../middleware/auth';
import { generalLimiter } from '../middleware/rateLimiter';
import * as familyController from '../controllers/familyController';

const router = Router();

router.use(authenticate);
router.use(generalLimiter);

// Family CRUD
router.post('/create', familyController.createFamily);
router.get('/my', familyController.getMyFamily);
router.get('/search', familyController.searchFamilies);
router.get('/ranking', familyController.getFamilyRanking);
router.get('/:familyId', familyController.getFamily);
router.put('/:familyId', familyController.updateFamily);
router.delete('/:familyId', familyController.disbandFamily);

// Membership
router.post('/:familyId/join', familyController.joinFamily);
router.post('/:familyId/apply', familyController.applyToFamily);
router.post('/:familyId/leave', familyController.leaveFamily);
router.post('/:familyId/invite', familyController.inviteMember);
router.post('/:familyId/kick/:userId', familyController.kickMember);
router.post('/:familyId/role/:userId', familyController.updateMemberRole);
router.post('/:familyId/transfer', familyController.transferOwnership);

// Members
router.get('/:familyId/members', familyController.getMembers);
router.get('/:familyId/activity', familyController.getActivity);

export default router;
