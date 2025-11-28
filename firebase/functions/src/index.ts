/**
 * Aura Voice Chat - Cloud Functions
 * Developer: Hawkaye Visions LTD — Pakistan
 *
 * Firebase Cloud Functions for:
 * - Daily reward reset
 * - Gift transaction processing
 * - Ranking calculations
 * - Notifications
 */

import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';

admin.initializeApp();

const db = admin.firestore();
const messaging = admin.messaging();

// ============================================
// DAILY REWARDS
// ============================================

/**
 * Reset daily reward status at midnight UTC
 * Runs every day at 00:00 UTC
 */
export const resetDailyRewards = functions.pubsub
    .schedule('0 0 * * *')
    .timeZone('UTC')
    .onRun(async () => {
        functions.logger.info('Starting daily rewards reset');

        const batch = db.batch();
        const usersSnapshot = await db.collection('users').get();

        let resetCount = 0;
        usersSnapshot.docs.forEach((doc) => {
            const rewardRef = doc.ref.collection('rewards').doc('daily');
            batch.update(rewardRef, {
                claimedToday: false,
                lastResetDate: admin.firestore.FieldValue.serverTimestamp()
            });
            resetCount++;
        });

        await batch.commit();
        functions.logger.info(`Reset daily rewards for ${resetCount} users`);
        return null;
    });

/**
 * Process daily reward claim
 * Triggered when a user claims their daily reward
 */
export const onDailyRewardClaim = functions.firestore
    .document('users/{userId}/rewards/daily')
    .onUpdate(async (change, context) => {
        const before = change.before.data();
        const after = change.after.data();

        // Only process if claiming (claimedToday changed from false to true)
        if (!before.claimedToday && after.claimedToday) {
            const userId = context.params.userId;
            const currentDay = after.currentDay || 1;

            // Calculate reward based on day and VIP tier
            const userDoc = await db.collection('users').doc(userId).get();
            const userData = userDoc.data();
            const vipTier = userData?.vipTier || 0;

            // Base rewards per day (5K → 50K coins)
            const baseRewards = [5000, 8000, 10000, 15000, 20000, 30000, 50000];
            const baseReward = baseRewards[Math.min(currentDay - 1, 6)];

            // VIP multipliers (1.0x → 3.0x)
            const vipMultipliers = [1.0, 1.2, 1.4, 1.6, 1.8, 2.0, 2.2, 2.4, 2.6, 2.8, 3.0];
            const multiplier = vipMultipliers[Math.min(vipTier, 10)];

            const finalReward = Math.floor(baseReward * multiplier);

            // Update user's wallet
            await db.collection('users').doc(userId).collection('wallet').doc('main').update({
                coins: admin.firestore.FieldValue.increment(finalReward)
            });

            // Update streak
            const nextDay = currentDay >= 7 ? 1 : currentDay + 1;
            await change.after.ref.update({
                currentDay: nextDay,
                totalClaimed: admin.firestore.FieldValue.increment(finalReward),
                claimStreak: admin.firestore.FieldValue.increment(1),
                lastClaimDate: admin.firestore.FieldValue.serverTimestamp()
            });

            functions.logger.info(`User ${userId} claimed ${finalReward} coins (Day ${currentDay}, VIP ${vipTier})`);
        }
        return null;
    });

// ============================================
// GIFT TRANSACTIONS
// ============================================

/**
 * Process gift transaction
 * Handles coin deduction, diamond addition, and history recording
 */
export const processGiftTransaction = functions.firestore
    .document('transactions/{transactionId}')
    .onCreate(async (snap, context) => {
        const transaction = snap.data();
        const { senderId, receiverId, giftId, quantity, coinAmount, diamondValue, type } = transaction;

        if (type !== 'gift') {
            return null;
        }

        const batch = db.batch();

        // Deduct coins from sender
        const senderWalletRef = db.collection('users').doc(senderId).collection('wallet').doc('main');
        batch.update(senderWalletRef, {
            coins: admin.firestore.FieldValue.increment(-coinAmount)
        });

        // Add diamonds to receiver (90% of diamond value)
        const receiverDiamonds = Math.floor(diamondValue * 0.9);
        const receiverWalletRef = db.collection('users').doc(receiverId).collection('wallet').doc('main');
        batch.update(receiverWalletRef, {
            diamonds: admin.firestore.FieldValue.increment(receiverDiamonds)
        });

        // Update gift statistics
        const senderStatsRef = db.collection('users').doc(senderId).collection('stats').doc('gifts');
        batch.set(senderStatsRef, {
            totalSent: admin.firestore.FieldValue.increment(quantity),
            totalSpent: admin.firestore.FieldValue.increment(coinAmount)
        }, { merge: true });

        const receiverStatsRef = db.collection('users').doc(receiverId).collection('stats').doc('gifts');
        batch.set(receiverStatsRef, {
            totalReceived: admin.firestore.FieldValue.increment(quantity),
            totalEarned: admin.firestore.FieldValue.increment(receiverDiamonds)
        }, { merge: true });

        // Mark transaction as processed
        batch.update(snap.ref, {
            status: 'completed',
            processedAt: admin.firestore.FieldValue.serverTimestamp()
        });

        await batch.commit();

        // Send notification to receiver
        const senderDoc = await db.collection('users').doc(senderId).get();
        const senderName = senderDoc.data()?.displayName || 'Someone';

        const giftDoc = await db.collection('gifts').doc(giftId).get();
        const giftName = giftDoc.data()?.name || 'a gift';

        await sendNotification(receiverId, {
            title: 'Gift Received! 🎁',
            body: `${senderName} sent you ${quantity}x ${giftName}`,
            data: { type: 'gift', transactionId: context.params.transactionId }
        });

        functions.logger.info(`Gift transaction processed: ${senderId} → ${receiverId}, ${quantity}x ${giftId}`);
        return null;
    });

// ============================================
// RANKINGS
// ============================================

/**
 * Calculate hourly rankings
 * Runs every hour to update leaderboards
 */
export const calculateRankings = functions.pubsub
    .schedule('0 * * * *')
    .timeZone('UTC')
    .onRun(async () => {
        functions.logger.info('Calculating rankings');

        // Get time ranges
        const now = new Date();
        const startOfDay = new Date(now.getFullYear(), now.getMonth(), now.getDate());
        const startOfWeek = new Date(startOfDay);
        startOfWeek.setDate(startOfDay.getDate() - startOfDay.getDay());
        const startOfMonth = new Date(now.getFullYear(), now.getMonth(), 1);

        // Calculate different ranking types
        await calculateGiftRankings('daily', startOfDay);
        await calculateGiftRankings('weekly', startOfWeek);
        await calculateGiftRankings('monthly', startOfMonth);

        functions.logger.info('Rankings calculation complete');
        return null;
    });

async function calculateGiftRankings(period: string, startDate: Date): Promise<void> {
    // Get gift transactions in period
    const transactions = await db.collection('transactions')
        .where('type', '==', 'gift')
        .where('status', '==', 'completed')
        .where('createdAt', '>=', startDate)
        .get();

    // Aggregate by sender (gifters) and receiver (receivers)
    const gifters = new Map<string, number>();
    const receivers = new Map<string, number>();

    transactions.docs.forEach((doc) => {
        const data = doc.data();
        gifters.set(data.senderId, (gifters.get(data.senderId) || 0) + data.coinAmount);
        receivers.set(data.receiverId, (receivers.get(data.receiverId) || 0) + data.diamondValue);
    });

    // Sort and get top 100
    const topGifters = [...gifters.entries()]
        .sort((a, b) => b[1] - a[1])
        .slice(0, 100)
        .map(([userId, amount], index) => ({ userId, amount, rank: index + 1 }));

    const topReceivers = [...receivers.entries()]
        .sort((a, b) => b[1] - a[1])
        .slice(0, 100)
        .map(([userId, amount], index) => ({ userId, amount, rank: index + 1 }));

    // Save rankings
    await db.collection('rankings').doc(`gifters_${period}`).set({
        rankings: topGifters,
        updatedAt: admin.firestore.FieldValue.serverTimestamp()
    });

    await db.collection('rankings').doc(`receivers_${period}`).set({
        rankings: topReceivers,
        updatedAt: admin.firestore.FieldValue.serverTimestamp()
    });
}

// ============================================
// NOTIFICATIONS
// ============================================

/**
 * Send push notification to a user
 */
async function sendNotification(
    userId: string,
    notification: { title: string; body: string; data?: Record<string, string> }
): Promise<void> {
    const userDoc = await db.collection('users').doc(userId).get();
    const fcmTokens = userDoc.data()?.fcmTokens || [];

    if (fcmTokens.length === 0) {
        functions.logger.info(`No FCM tokens for user ${userId}`);
        return;
    }

    const message: admin.messaging.MulticastMessage = {
        notification: {
            title: notification.title,
            body: notification.body
        },
        data: notification.data || {},
        tokens: fcmTokens
    };

    try {
        const response = await messaging.sendEachForMulticast(message);
        functions.logger.info(`Notification sent to ${userId}: ${response.successCount} success, ${response.failureCount} failed`);

        // Remove invalid tokens
        const invalidTokens: string[] = [];
        response.responses.forEach((resp, idx) => {
            if (!resp.success && resp.error?.code === 'messaging/unregistered') {
                invalidTokens.push(fcmTokens[idx]);
            }
        });

        if (invalidTokens.length > 0) {
            await db.collection('users').doc(userId).update({
                fcmTokens: admin.firestore.FieldValue.arrayRemove(...invalidTokens)
            });
        }
    } catch (error) {
        functions.logger.error(`Failed to send notification to ${userId}:`, error);
    }
}

// ============================================
// KYC VERIFICATION (ID Card + Selfie only)
// ============================================

/**
 * Notify admin when KYC is submitted
 */
export const onKycSubmission = functions.firestore
    .document('kyc/{userId}')
    .onCreate(async (snap, context) => {
        const kycData = snap.data();
        const userId = context.params.userId;

        functions.logger.info(`KYC submitted by user ${userId}`);
        functions.logger.info(`Documents: ID Card Front, ID Card Back, Selfie`);
        functions.logger.info(`Liveness Score: ${kycData.livenessScore || 'N/A'}`);

        // Note: KYC only includes ID Card (front/back) + Selfie
        // NO utility bills as per requirements

        // Send notification to user
        await sendNotification(userId, {
            title: 'KYC Submitted',
            body: 'Your identity verification documents are being reviewed.',
            data: { type: 'kyc', status: 'submitted' }
        });

        return null;
    });

/**
 * Notify user when KYC status changes
 */
export const onKycStatusChange = functions.firestore
    .document('kyc/{userId}')
    .onUpdate(async (change, context) => {
        const before = change.before.data();
        const after = change.after.data();
        const userId = context.params.userId;

        if (before.status !== after.status) {
            let message = '';
            if (after.status === 'approved') {
                message = 'Your identity has been verified successfully!';
            } else if (after.status === 'rejected') {
                message = `Verification failed: ${after.rejectionReason || 'Please try again.'}`;
            }

            if (message) {
                await sendNotification(userId, {
                    title: 'KYC Update',
                    body: message,
                    data: { type: 'kyc', status: after.status }
                });
            }
        }

        return null;
    });

// ============================================
// CP (COUPLE PARTNERSHIP)
// ============================================

/**
 * Update CP level when EXP changes
 */
export const onCpExpChange = functions.firestore
    .document('cp/{cpId}')
    .onUpdate(async (change, context) => {
        const before = change.before.data();
        const after = change.after.data();

        // Check if EXP changed
        if (before.exp !== after.exp) {
            // CP Level thresholds
            const levelThresholds = [0, 100, 500, 1500, 3500, 7000, 12000, 20000, 32000, 50000];
            
            let newLevel = 1;
            for (let i = levelThresholds.length - 1; i >= 0; i--) {
                if (after.exp >= levelThresholds[i]) {
                    newLevel = i + 1;
                    break;
                }
            }

            if (newLevel !== after.level) {
                await change.after.ref.update({ level: newLevel });

                // Notify both partners
                const partner1Id = after.partner1Id;
                const partner2Id = after.partner2Id;

                const notification = {
                    title: 'CP Level Up! 💕',
                    body: `Your CP reached Level ${newLevel}!`,
                    data: { type: 'cp', cpId: context.params.cpId }
                };

                await sendNotification(partner1Id, notification);
                await sendNotification(partner2Id, notification);
            }
        }

        return null;
    });
