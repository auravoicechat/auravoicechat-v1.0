#!/bin/bash

# ============================================================================
# Aura Voice Chat - Complete Auto Setup Script
# Developer: Hawkaye Visions LTD — Lahore, Pakistan
# ============================================================================
#
# This script automates the COMPLETE setup of Aura Voice Chat including:
# - Prerequisites check
# - Firebase setup
# - Backend setup
# - Android app configuration
# - APK build
# - Database initialization
#
# Usage: ./scripts/auto-setup.sh [--env=dev|staging|prod]
# ============================================================================

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Configuration
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(dirname "$SCRIPT_DIR")"
ENV="${ENV:-dev}"

# Parse arguments
for arg in "$@"; do
  case $arg in
    --env=*)
      ENV="${arg#*=}"
      shift
      ;;
  esac
done

echo -e "${PURPLE}"
echo "╔══════════════════════════════════════════════════════════════════╗"
echo "║                                                                  ║"
echo "║     🎤 AURA VOICE CHAT - COMPLETE AUTO SETUP                     ║"
echo "║                                                                  ║"
echo "║     Developer: Hawkaye Visions LTD — Lahore, Pakistan           ║"
echo "║                                                                  ║"
echo "╚══════════════════════════════════════════════════════════════════╝"
echo -e "${NC}"
echo ""
echo -e "${CYAN}Environment: ${YELLOW}$ENV${NC}"
echo ""

# ============================================================================
# STEP 1: Prerequisites Check
# ============================================================================
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${CYAN}STEP 1: Checking Prerequisites${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"

check_command() {
  if command -v $1 &> /dev/null; then
    echo -e "  ${GREEN}✓${NC} $1 found: $($1 --version 2>/dev/null | head -1)"
    return 0
  else
    echo -e "  ${RED}✗${NC} $1 not found"
    return 1
  fi
}

MISSING_DEPS=0

echo ""
echo "Checking required tools..."

check_command "node" || MISSING_DEPS=1
check_command "npm" || MISSING_DEPS=1
check_command "git" || MISSING_DEPS=1

# Check Node version
if command -v node &> /dev/null; then
  NODE_VERSION=$(node -v | cut -d'v' -f2 | cut -d'.' -f1)
  if [ "$NODE_VERSION" -lt 18 ]; then
    echo -e "  ${RED}✗${NC} Node.js 18+ required (found v$NODE_VERSION)"
    MISSING_DEPS=1
  fi
fi

echo ""
echo "Checking optional tools..."
check_command "firebase" || echo -e "    ${YELLOW}→${NC} Will install Firebase CLI"
check_command "java" || echo -e "    ${YELLOW}→${NC} Java required for Android builds"

if [ $MISSING_DEPS -eq 1 ]; then
  echo ""
  echo -e "${RED}Missing required dependencies. Please install them first.${NC}"
  echo ""
  echo "Install Node.js 18+: https://nodejs.org/"
  echo "Install Git: https://git-scm.com/"
  exit 1
fi

echo ""
echo -e "${GREEN}✓ All prerequisites met${NC}"

# ============================================================================
# STEP 2: Install Dependencies
# ============================================================================
echo ""
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${CYAN}STEP 2: Installing Dependencies${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"

# Install Firebase CLI if not present
if ! command -v firebase &> /dev/null; then
  echo ""
  echo "Installing Firebase CLI..."
  npm install -g firebase-tools
fi

# Backend dependencies
echo ""
echo "Installing backend dependencies..."
cd "$ROOT_DIR/backend"
npm install

# Firebase functions dependencies
echo ""
echo "Installing Firebase functions dependencies..."
cd "$ROOT_DIR/firebase/functions"
npm install

cd "$ROOT_DIR"
echo ""
echo -e "${GREEN}✓ Dependencies installed${NC}"

# ============================================================================
# STEP 3: Environment Configuration
# ============================================================================
echo ""
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${CYAN}STEP 3: Environment Configuration${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"

# Create backend .env if not exists
if [ ! -f "$ROOT_DIR/backend/.env" ]; then
  echo ""
  echo "Creating backend .env file..."
  cat > "$ROOT_DIR/backend/.env" << EOF
# Aura Voice Chat Backend Configuration
# Environment: $ENV
# Generated: $(date)

NODE_ENV=$ENV
PORT=3000

# Database
DATABASE_URL=postgresql://user:password@localhost:5432/aura_$ENV

# Redis
REDIS_URL=redis://localhost:6379

# JWT
JWT_SECRET=$(openssl rand -hex 32)
JWT_EXPIRES_IN=7d

# Firebase
FIREBASE_PROJECT_ID=aura-voice-chat-$ENV

# Twilio (for OTP)
TWILIO_ACCOUNT_SID=your_account_sid
TWILIO_AUTH_TOKEN=your_auth_token
TWILIO_PHONE_NUMBER=+1234567890

# Payment Gateways
EASYPAISA_USERNAME=
EASYPAISA_PASSWORD=
EASYPAISA_STORE_ID=

JAZZCASH_MERCHANT_ID=
JAZZCASH_PASSWORD=
JAZZCASH_SALT=

RAZORPAY_KEY_ID=
RAZORPAY_KEY_SECRET=
RAZORPAY_ACCOUNT_NUMBER=

PAYPAL_ENV=sandbox
PAYPAL_CLIENT_ID=
PAYPAL_CLIENT_SECRET=

# CORS
CORS_ORIGIN=*
EOF
  echo -e "${GREEN}✓ Created backend/.env${NC}"
  echo -e "${YELLOW}  → Please update with your actual credentials${NC}"
else
  echo -e "${GREEN}✓ backend/.env already exists${NC}"
fi

# ============================================================================
# STEP 4: Firebase Setup
# ============================================================================
echo ""
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${CYAN}STEP 4: Firebase Setup${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"

echo ""
echo "Do you want to set up Firebase now? (y/n)"
read -r SETUP_FIREBASE

if [ "$SETUP_FIREBASE" = "y" ] || [ "$SETUP_FIREBASE" = "Y" ]; then
  "$SCRIPT_DIR/setup-firebase.sh"
else
  echo -e "${YELLOW}Skipping Firebase setup. Run ./scripts/setup-firebase.sh later.${NC}"
fi

# ============================================================================
# STEP 5: Database Initialization
# ============================================================================
echo ""
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${CYAN}STEP 5: Database Initialization${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"

echo ""
echo "Do you want to initialize the database? (y/n)"
echo "(Requires PostgreSQL to be running)"
read -r INIT_DB

if [ "$INIT_DB" = "y" ] || [ "$INIT_DB" = "Y" ]; then
  cd "$ROOT_DIR/backend"
  echo "Running database migrations..."
  npm run migrate 2>/dev/null || echo -e "${YELLOW}Migration failed - check DATABASE_URL in .env${NC}"
  echo "Seeding database..."
  npm run seed 2>/dev/null || echo -e "${YELLOW}Seeding failed - check database connection${NC}"
  cd "$ROOT_DIR"
else
  echo -e "${YELLOW}Skipping database initialization.${NC}"
fi

# ============================================================================
# STEP 6: Android Configuration
# ============================================================================
echo ""
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${CYAN}STEP 6: Android Configuration${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"

# Check for google-services.json
if [ ! -f "$ROOT_DIR/android/app/google-services.json" ]; then
  echo ""
  echo -e "${YELLOW}⚠ google-services.json not found in android/app/${NC}"
  echo ""
  echo "To configure Android:"
  echo "1. Go to Firebase Console → Project Settings → General"
  echo "2. Add Android app with package name: com.aura.voicechat"
  echo "3. Download google-services.json"
  echo "4. Place it in android/app/ directory"
else
  echo -e "${GREEN}✓ google-services.json found${NC}"
fi

# Check for local.properties
if [ ! -f "$ROOT_DIR/android/local.properties" ]; then
  echo ""
  echo "Creating android/local.properties..."
  
  # Try to detect Android SDK
  if [ -n "$ANDROID_HOME" ]; then
    echo "sdk.dir=$ANDROID_HOME" > "$ROOT_DIR/android/local.properties"
    echo -e "${GREEN}✓ Created local.properties with ANDROID_HOME${NC}"
  elif [ -d "$HOME/Android/Sdk" ]; then
    echo "sdk.dir=$HOME/Android/Sdk" > "$ROOT_DIR/android/local.properties"
    echo -e "${GREEN}✓ Created local.properties${NC}"
  elif [ -d "$HOME/Library/Android/sdk" ]; then
    echo "sdk.dir=$HOME/Library/Android/sdk" > "$ROOT_DIR/android/local.properties"
    echo -e "${GREEN}✓ Created local.properties${NC}"
  else
    echo -e "${YELLOW}⚠ Could not detect Android SDK. Please create android/local.properties manually.${NC}"
  fi
fi

# ============================================================================
# STEP 7: Build APK
# ============================================================================
echo ""
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${CYAN}STEP 7: Build APK${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"

echo ""
echo "Do you want to build the Android APK now? (y/n)"
read -r BUILD_APK

if [ "$BUILD_APK" = "y" ] || [ "$BUILD_APK" = "Y" ]; then
  "$SCRIPT_DIR/build-apk.sh" --env=$ENV
else
  echo -e "${YELLOW}Skipping APK build. Run ./scripts/build-apk.sh later.${NC}"
fi

# ============================================================================
# COMPLETION
# ============================================================================
echo ""
echo -e "${PURPLE}"
echo "╔══════════════════════════════════════════════════════════════════╗"
echo "║                                                                  ║"
echo "║     🎉 SETUP COMPLETE!                                           ║"
echo "║                                                                  ║"
echo "╚══════════════════════════════════════════════════════════════════╝"
echo -e "${NC}"

echo ""
echo -e "${GREEN}What's been set up:${NC}"
echo "  ✓ Prerequisites verified"
echo "  ✓ Dependencies installed"
echo "  ✓ Environment configuration created"
[ "$SETUP_FIREBASE" = "y" ] && echo "  ✓ Firebase configured"
[ "$INIT_DB" = "y" ] && echo "  ✓ Database initialized"
echo ""

echo -e "${YELLOW}Next steps:${NC}"
echo ""
echo "1. Update configuration files with your credentials:"
echo "   - backend/.env"
echo "   - android/app/google-services.json"
echo ""
echo "2. Start the backend server:"
echo "   cd backend && npm run dev"
echo ""
echo "3. Build the Android app:"
echo "   ./scripts/build-apk.sh"
echo ""
echo "4. Deploy to AWS EC2:"
echo "   See docs/aws-ec2-deployment.md"
echo ""
echo -e "${CYAN}Documentation:${NC}"
echo "  - Complete Guide: COMPREHENSIVE-GUIDE.md"
echo "  - Firebase Setup: docs/firebase-setup.md"
echo "  - Payment Setup: docs/setup/PAYMENT-GATEWAY-SETUP.md"
echo "  - Play Store: docs/play-store-submission.md"
echo ""
echo -e "${PURPLE}Happy coding! 🚀${NC}"
echo ""
