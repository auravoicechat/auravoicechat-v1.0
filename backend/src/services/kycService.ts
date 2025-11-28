/**
 * KYC Service
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Only ID Card (front/back) + Selfie - NO utility bills
 */

interface KycStatus {
  userId: string;
  status: string;
  idCardFront: string | null;
  idCardBack: string | null;
  selfie: string | null;
  livenessScore: number | null;
  submittedAt: number | null;
  reviewedAt: number | null;
  rejectionReason: string | null;
}

interface KycSubmission {
  idCardFrontUri: string;
  idCardBackUri: string;
  selfieUri: string;
  livenessCheckPassed: boolean;
}

// In-memory storage (use database in production)
const userKyc = new Map<string, KycStatus>();

export const getKycStatus = async (userId: string): Promise<KycStatus> => {
  return userKyc.get(userId) || {
    userId,
    status: 'NOT_STARTED',
    idCardFront: null,
    idCardBack: null,
    selfie: null,
    livenessScore: null,
    submittedAt: null,
    reviewedAt: null,
    rejectionReason: null
  };
};

export const submitKyc = async (userId: string, submission: KycSubmission): Promise<void> => {
  userKyc.set(userId, {
    userId,
    status: 'PENDING_REVIEW',
    idCardFront: submission.idCardFrontUri,
    idCardBack: submission.idCardBackUri,
    selfie: submission.selfieUri,
    livenessScore: submission.livenessCheckPassed ? 0.95 : 0.5,
    submittedAt: Date.now(),
    reviewedAt: null,
    rejectionReason: null
  });
};

export const getUploadUrl = async (userId: string, type: string): Promise<string> => {
  // Generate signed upload URL for Firebase Storage
  return `https://storage.auravoice.chat/kyc/${userId}/${type}_${Date.now()}.jpg`;
};
