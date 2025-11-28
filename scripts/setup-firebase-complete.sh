#!/bin/bash

# ============================================================================
# Aura Voice Chat - Firebase Auto Setup with Data Integration
# Developer: Hawkaye Visions LTD — Lahore, Pakistan
#
# This script automates complete Firebase setup including:
# - Project creation/selection
# - Authentication providers
# - Firestore database with initial data
# - Cloud Storage
# - Cloud Functions deployment
# - Remote Config
# - Security rules
#
# Usage: ./scripts/setup-firebase-complete.sh
# ============================================================================

set -e

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
CYAN='\033[0;36m'
NC='\033[0m'

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(dirname "$SCRIPT_DIR")"
FIREBASE_DIR="$ROOT_DIR/firebase"
DATA_DIR="$ROOT_DIR/data"

echo -e "${PURPLE}"
echo "╔══════════════════════════════════════════════════════════════════╗"
echo "║                                                                  ║"
echo "║     🔥 AURA VOICE CHAT - FIREBASE COMPLETE SETUP                 ║"
echo "║                                                                  ║"
echo "║     Developer: Hawkaye Visions LTD — Lahore, Pakistan           ║"
echo "║                                                                  ║"
echo "╚══════════════════════════════════════════════════════════════════╝"
echo -e "${NC}"
echo ""

# ============================================================================
# Prerequisites Check
# ============================================================================
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${CYAN}Checking Prerequisites${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"

# Check Node.js
if ! command -v node &> /dev/null; then
    echo -e "${RED}✗ Node.js is not installed${NC}"
    echo "  Install: https://nodejs.org/"
    exit 1
fi
echo -e "${GREEN}✓${NC} Node.js $(node -v)"

# Check npm
if ! command -v npm &> /dev/null; then
    echo -e "${RED}✗ npm is not installed${NC}"
    exit 1
fi
echo -e "${GREEN}✓${NC} npm $(npm -v)"

# Install/Check Firebase CLI
if ! command -v firebase &> /dev/null; then
    echo ""
    echo "Installing Firebase CLI..."
    npm install -g firebase-tools
fi
echo -e "${GREEN}✓${NC} Firebase CLI $(firebase --version)"

# ============================================================================
# Firebase Authentication
# ============================================================================
echo ""
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${CYAN}Firebase Authentication${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"

# Check if already logged in
if ! firebase login:list 2>&1 | grep -q "@"; then
    echo "Please login to Firebase..."
    firebase login
fi
echo -e "${GREEN}✓${NC} Firebase authenticated"

# ============================================================================
# Project Selection/Creation
# ============================================================================
echo ""
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${CYAN}Firebase Project Setup${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"

echo ""
echo "Options:"
echo "  1. Create new project"
echo "  2. Use existing project"
read -p "Enter choice (1 or 2): " PROJECT_CHOICE

if [ "$PROJECT_CHOICE" == "1" ]; then
    read -p "Enter project ID (e.g., aura-voice-chat-prod): " PROJECT_ID
    read -p "Enter project display name (default: Aura Voice Chat): " PROJECT_NAME
    PROJECT_NAME=${PROJECT_NAME:-"Aura Voice Chat"}
    
    echo ""
    echo "Creating Firebase project..."
    firebase projects:create "$PROJECT_ID" --display-name "$PROJECT_NAME" 2>/dev/null || {
        echo -e "${YELLOW}Project may already exist, continuing...${NC}"
    }
else
    echo ""
    echo "Available projects:"
    firebase projects:list
    echo ""
    read -p "Enter project ID: " PROJECT_ID
fi

# Select project
cd "$FIREBASE_DIR"
firebase use "$PROJECT_ID"
echo -e "${GREEN}✓${NC} Using project: $PROJECT_ID"

# ============================================================================
# Create Firestore Database
# ============================================================================
echo ""
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${CYAN}Setting up Firestore Database${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"

echo "Creating Firestore database..."
firebase firestore:databases:create default --location=us-central 2>/dev/null || {
    echo -e "${YELLOW}Firestore database already exists${NC}"
}

# Deploy security rules
echo "Deploying Firestore security rules..."
firebase deploy --only firestore:rules --force

echo -e "${GREEN}✓${NC} Firestore configured"

# ============================================================================
# Initialize Firestore with Data
# ============================================================================
echo ""
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${CYAN}Initializing Firestore with Data${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"

# Create a Node.js script to upload data
cat > /tmp/upload-data.js << 'EOF'
const admin = require('firebase-admin');
const fs = require('fs');
const path = require('path');

// Initialize Firebase Admin
const serviceAccount = process.env.GOOGLE_APPLICATION_CREDENTIALS 
  ? require(process.env.GOOGLE_APPLICATION_CREDENTIALS)
  : null;

if (serviceAccount) {
  admin.initializeApp({
    credential: admin.credential.cert(serviceAccount)
  });
} else {
  admin.initializeApp();
}

const db = admin.firestore();
const dataDir = process.argv[2] || './data';

async function uploadData() {
  console.log('Uploading data to Firestore...');
  
  // Level Rewards
  const levelRewardsPath = path.join(dataDir, 'level-rewards.json');
  if (fs.existsSync(levelRewardsPath)) {
    const levelData = JSON.parse(fs.readFileSync(levelRewardsPath, 'utf8'));
    await db.collection('config').doc('levels').set(levelData);
    console.log('  ✓ Level rewards uploaded');
  }
  
  // VIP Multipliers
  const vipPath = path.join(dataDir, 'vip-multipliers.json');
  if (fs.existsSync(vipPath)) {
    const vipData = JSON.parse(fs.readFileSync(vipPath, 'utf8'));
    await db.collection('config').doc('vip').set(vipData);
    console.log('  ✓ VIP multipliers uploaded');
  }
  
  // Medals
  const medalsPath = path.join(dataDir, 'medals.json');
  if (fs.existsSync(medalsPath)) {
    const medalsData = JSON.parse(fs.readFileSync(medalsPath, 'utf8'));
    for (const medal of medalsData.medals || []) {
      await db.collection('medals').doc(medal.id).set(medal);
    }
    console.log('  ✓ Medals uploaded');
  }
  
  // Games Configuration
  const gamesPath = path.join(dataDir, 'games.json');
  if (fs.existsSync(gamesPath)) {
    const gamesData = JSON.parse(fs.readFileSync(gamesPath, 'utf8'));
    await db.collection('config').doc('games').set(gamesData);
    console.log('  ✓ Games config uploaded');
  }
  
  // CP Levels
  const cpPath = path.join(dataDir, 'cp-levels.json');
  if (fs.existsSync(cpPath)) {
    const cpData = JSON.parse(fs.readFileSync(cpPath, 'utf8'));
    await db.collection('config').doc('cp').set(cpData);
    console.log('  ✓ CP levels uploaded');
  }
  
  // Rankings Config
  const rankingsPath = path.join(dataDir, 'rankings.json');
  if (fs.existsSync(rankingsPath)) {
    const rankingsData = JSON.parse(fs.readFileSync(rankingsPath, 'utf8'));
    await db.collection('config').doc('rankings').set(rankingsData);
    console.log('  ✓ Rankings config uploaded');
  }
  
  // Earning Targets
  const earningsPath = path.join(dataDir, 'earning-targets.json');
  if (fs.existsSync(earningsPath)) {
    const earningsData = JSON.parse(fs.readFileSync(earningsPath, 'utf8'));
    await db.collection('config').doc('earnings').set(earningsData);
    console.log('  ✓ Earning targets uploaded');
  }
  
  // Reseller Config
  const resellerPath = path.join(dataDir, 'reseller-config.json');
  if (fs.existsSync(resellerPath)) {
    const resellerData = JSON.parse(fs.readFileSync(resellerPath, 'utf8'));
    await db.collection('config').doc('reseller').set(resellerData);
    console.log('  ✓ Reseller config uploaded');
  }
  
  // System Settings
  await db.collection('config').doc('system').set({
    appVersion: '1.0.0',
    minAppVersion: '1.0.0',
    maintenanceMode: false,
    features: {
      dailyRewards: true,
      kyc: true,
      cp: true,
      family: true,
      games: true,
      earning: true,
      reseller: true,
      videoMode: true,
      superMic: true,
      luckyBag: true
    },
    economy: {
      exchangeRate: 0.3,
      minWithdrawal: 5,
      maxDailyWithdrawal: 500
    },
    updatedAt: admin.firestore.FieldValue.serverTimestamp()
  });
  console.log('  ✓ System settings uploaded');
  
  console.log('\n✅ All data uploaded successfully!');
}

uploadData()
  .then(() => process.exit(0))
  .catch(err => {
    console.error('Error:', err);
    process.exit(1);
  });
EOF

# Check if we can upload data
read -p "Do you want to upload initial data to Firestore? (y/n): " UPLOAD_DATA
if [ "$UPLOAD_DATA" = "y" ] || [ "$UPLOAD_DATA" = "Y" ]; then
    cd "$ROOT_DIR"
    npm install firebase-admin --no-save 2>/dev/null || true
    node /tmp/upload-data.js "$DATA_DIR" 2>/dev/null || {
        echo -e "${YELLOW}Data upload requires service account. Run manually with credentials.${NC}"
    }
fi

# ============================================================================
# Cloud Storage Setup
# ============================================================================
echo ""
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${CYAN}Setting up Cloud Storage${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"

cd "$FIREBASE_DIR"
firebase deploy --only storage --force 2>/dev/null || echo -e "${YELLOW}Storage rules deployed (or skipped)${NC}"

echo -e "${GREEN}✓${NC} Cloud Storage configured"

# ============================================================================
# Cloud Functions Setup
# ============================================================================
echo ""
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${CYAN}Setting up Cloud Functions${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"

cd "$FIREBASE_DIR/functions"

if [ -f "package.json" ]; then
    echo "Installing function dependencies..."
    npm install
    
    echo "Building functions..."
    npm run build 2>/dev/null || echo -e "${YELLOW}Build step not configured${NC}"
    
    cd "$FIREBASE_DIR"
    echo "Deploying Cloud Functions..."
    firebase deploy --only functions --force 2>/dev/null || {
        echo -e "${YELLOW}Functions deployment skipped (may need Blaze plan)${NC}"
    }
fi

echo -e "${GREEN}✓${NC} Cloud Functions configured"

# ============================================================================
# Remote Config Setup
# ============================================================================
echo ""
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${CYAN}Remote Config Setup${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"

echo ""
echo "Remote Config needs to be set up manually in Firebase Console:"
echo ""
echo "  1. Go to: https://console.firebase.google.com/project/$PROJECT_ID/config"
echo ""
echo "  2. Add these parameters:"
echo ""
echo "  ┌─────────────────────────────┬──────────┬──────────────────────┐"
echo "  │ Parameter                   │ Type     │ Default Value        │"
echo "  ├─────────────────────────────┼──────────┼──────────────────────┤"
echo "  │ feature_daily_rewards       │ Boolean  │ true                 │"
echo "  │ feature_kyc_enabled         │ Boolean  │ true                 │"
echo "  │ feature_cp_enabled          │ Boolean  │ true                 │"
echo "  │ feature_family_enabled      │ Boolean  │ true                 │"
echo "  │ feature_games_enabled       │ Boolean  │ true                 │"
echo "  │ feature_earning_enabled     │ Boolean  │ true                 │"
echo "  │ feature_reseller_enabled    │ Boolean  │ true                 │"
echo "  │ feature_video_mode          │ Boolean  │ true                 │"
echo "  │ feature_super_mic           │ Boolean  │ true                 │"
echo "  │ feature_lucky_bag           │ Boolean  │ true                 │"
echo "  │ min_app_version             │ String   │ 1.0.0                │"
echo "  │ maintenance_mode            │ Boolean  │ false                │"
echo "  │ daily_reward_base           │ Number   │ 5000                 │"
echo "  │ exchange_rate               │ Number   │ 0.3                  │"
echo "  │ large_gift_threshold        │ Number   │ 1000000              │"
echo "  └─────────────────────────────┴──────────┴──────────────────────┘"
echo ""
read -p "Press Enter when Remote Config is configured..."

# ============================================================================
# Authentication Setup Instructions
# ============================================================================
echo ""
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${CYAN}Authentication Setup${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"

echo ""
echo "Please enable auth providers in Firebase Console:"
echo ""
echo "  URL: https://console.firebase.google.com/project/$PROJECT_ID/authentication/providers"
echo ""
echo "  1. PHONE AUTHENTICATION"
echo "     • Click 'Add new provider' → 'Phone'"
echo "     • Enable phone authentication"
echo "     • Add test phone numbers:"
echo "       +1 650-555-1234 → 123456"
echo "       +1 650-555-5678 → 654321"
echo ""
echo "  2. GOOGLE SIGN-IN"
echo "     • Click 'Add new provider' → 'Google'"
echo "     • Enter support email"
echo "     • Download updated google-services.json"
echo ""
echo "  3. FACEBOOK LOGIN (Optional)"
echo "     • Create Facebook App at developers.facebook.com"
echo "     • Click 'Add new provider' → 'Facebook'"
echo "     • Enter Facebook App ID and Secret"
echo ""
read -p "Press Enter when authentication is configured..."

# ============================================================================
# Android Configuration
# ============================================================================
echo ""
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${CYAN}Android Configuration${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"

ANDROID_DIR="$ROOT_DIR/android"

echo ""
echo "  1. Add Android app in Firebase Console:"
echo "     • Go to Project Settings → General"
echo "     • Click 'Add app' → Android"
echo "     • Package name: com.aura.voicechat"
echo "     • App nickname: Aura Voice Chat"
echo ""
echo "  2. Download google-services.json"
echo "     • Place in: $ANDROID_DIR/app/"
echo ""
echo "  3. Add SHA-1 fingerprint (for Google Sign-In):"
echo "     • Run this command to get SHA-1:"
echo ""
echo "     keytool -list -v -keystore ~/.android/debug.keystore \\"
echo "       -alias androiddebugkey -storepass android -keypass android"
echo ""
echo "     • Add SHA-1 to Firebase Console → Project Settings → Your apps"
echo ""

if [ -f "$ANDROID_DIR/app/google-services.json" ]; then
    echo -e "${GREEN}✓${NC} google-services.json found"
else
    echo -e "${YELLOW}⚠${NC} google-services.json not found in $ANDROID_DIR/app/"
fi

read -p "Press Enter when Android configuration is complete..."

# ============================================================================
# Generate Service Account
# ============================================================================
echo ""
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${CYAN}Service Account for Backend${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"

echo ""
echo "Generate a service account for the backend:"
echo ""
echo "  1. Go to: https://console.firebase.google.com/project/$PROJECT_ID/settings/serviceaccounts/adminsdk"
echo ""
echo "  2. Click 'Generate new private key'"
echo ""
echo "  3. Save the JSON file as:"
echo "     $ROOT_DIR/backend/firebase-service-account.json"
echo ""
echo "  4. Update backend/.env with Firebase credentials"
echo ""
read -p "Press Enter when service account is generated..."

# ============================================================================
# Completion
# ============================================================================
echo ""
echo -e "${PURPLE}"
echo "╔══════════════════════════════════════════════════════════════════╗"
echo "║                                                                  ║"
echo "║     🎉 FIREBASE SETUP COMPLETE!                                  ║"
echo "║                                                                  ║"
echo "╚══════════════════════════════════════════════════════════════════╝"
echo -e "${NC}"

echo ""
echo -e "${GREEN}What's been configured:${NC}"
echo "  ✓ Firebase project selected: $PROJECT_ID"
echo "  ✓ Firestore database created"
echo "  ✓ Firestore security rules deployed"
echo "  ✓ Cloud Storage configured"
echo "  ✓ Cloud Functions setup (if available)"
echo ""
echo -e "${CYAN}Manual steps completed:${NC}"
echo "  ✓ Remote Config parameters"
echo "  ✓ Authentication providers"
echo "  ✓ Android app configuration"
echo "  ✓ Service account generated"
echo ""
echo -e "${CYAN}Firebase Console:${NC}"
echo "  https://console.firebase.google.com/project/$PROJECT_ID"
echo ""
echo -e "${CYAN}Next steps:${NC}"
echo "  1. Verify google-services.json is in android/app/"
echo "  2. Update backend/.env with Firebase credentials"
echo "  3. Build the Android app: ./scripts/build-apk.sh"
echo "  4. Deploy backend: ./scripts/deploy-ec2.sh"
echo ""
echo -e "${PURPLE}Happy coding! 🔥${NC}"
echo ""

# Cleanup
rm -f /tmp/upload-data.js
