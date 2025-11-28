#!/bin/bash

# Aura Voice Chat - Firebase Setup Script
# Developer: Hawkaye Visions LTD — Pakistan
#
# This script automates complete Firebase project setup including:
# - Firebase CLI installation
# - Project initialization
# - Authentication providers
# - Firestore database
# - Cloud Storage
# - Cloud Functions
# - Remote Config
# - Analytics

set -e

echo "======================================"
echo "Aura Voice Chat - Firebase Setup"
echo "Developer: Hawkaye Visions LTD"
echo "======================================"
echo ""

FIREBASE_DIR="$(dirname "$0")/../firebase"
ANDROID_DIR="$(dirname "$0")/../android"

# Check for Node.js
if ! command -v node &> /dev/null; then
    echo "❌ Node.js is not installed. Please install Node.js 18+ first."
    exit 1
fi

NODE_VERSION=$(node -v | cut -d'v' -f2 | cut -d'.' -f1)
if [ "$NODE_VERSION" -lt 18 ]; then
    echo "❌ Node.js 18+ is required. Current version: $(node -v)"
    exit 1
fi

echo "✅ Node.js $(node -v) detected"

# Install Firebase CLI if not installed
if ! command -v firebase &> /dev/null; then
    echo ""
    echo "📦 Installing Firebase CLI..."
    npm install -g firebase-tools
fi

echo "✅ Firebase CLI $(firebase --version) detected"

# Login to Firebase
echo ""
echo "🔐 Checking Firebase authentication..."
if ! firebase login:list 2>&1 | grep -q "@"; then
    echo "   Please login to Firebase..."
    firebase login
fi
echo "✅ Firebase authenticated"

# Project selection
echo ""
echo "📁 Firebase Project Setup"
echo "   Options:"
echo "   1. Create new project"
echo "   2. Use existing project"
read -p "   Enter choice (1 or 2): " PROJECT_CHOICE

if [ "$PROJECT_CHOICE" == "1" ]; then
    read -p "   Enter project ID (e.g., aura-voice-chat-prod): " PROJECT_ID
    read -p "   Enter project display name: " PROJECT_NAME
    echo ""
    echo "   Creating Firebase project..."
    firebase projects:create "$PROJECT_ID" --display-name "${PROJECT_NAME:-Aura Voice Chat}" || {
        echo "⚠️  Project creation failed (may already exist)"
    }
else
    echo ""
    echo "   Available projects:"
    firebase projects:list
    echo ""
    read -p "   Enter project ID from list above: " PROJECT_ID
fi

# Use the project
echo ""
echo "📌 Selecting project: $PROJECT_ID"
firebase use "$PROJECT_ID"

# Navigate to Firebase directory
cd "$FIREBASE_DIR"

# Initialize Firestore
echo ""
echo "📊 Setting up Firestore..."
firebase firestore:databases:create default --location=us-central 2>/dev/null || {
    echo "   Firestore database already exists"
}

# Deploy Firestore rules
echo "   Deploying Firestore security rules..."
firebase deploy --only firestore:rules --force

# Deploy Storage rules
echo ""
echo "📁 Setting up Cloud Storage..."
firebase deploy --only storage --force

# Setup Cloud Functions
echo ""
echo "⚡ Setting up Cloud Functions..."
cd functions

# Install dependencies
if [ -f "package.json" ]; then
    echo "   Installing function dependencies..."
    npm install
    
    echo "   Building functions..."
    npm run build || echo "   Build step not configured"
fi

cd ..

# Deploy Cloud Functions
echo "   Deploying Cloud Functions..."
firebase deploy --only functions --force 2>/dev/null || {
    echo "   ⚠️  No functions to deploy or deployment failed"
}

# Authentication setup instructions
echo ""
echo "======================================"
echo "🔐 AUTHENTICATION SETUP"
echo "======================================"
echo ""
echo "Please enable the following auth providers in Firebase Console:"
echo ""
echo "1. PHONE AUTHENTICATION"
echo "   - Go to: https://console.firebase.google.com/project/$PROJECT_ID/authentication/providers"
echo "   - Click 'Add new provider' → 'Phone'"
echo "   - Enable phone authentication"
echo "   - Add test phone numbers for development:"
echo "     +1 650-555-1234 → 123456"
echo "     +1 650-555-5678 → 654321"
echo ""
echo "2. GOOGLE SIGN-IN"
echo "   - Click 'Add new provider' → 'Google'"
echo "   - Enter support email"
echo "   - Download updated google-services.json"
echo ""
echo "3. FACEBOOK LOGIN"
echo "   - Create Facebook App at developers.facebook.com"
echo "   - Click 'Add new provider' → 'Facebook'"
echo "   - Enter Facebook App ID and Secret"
echo "   - Copy OAuth redirect URI to Facebook App"
echo ""
read -p "Press Enter when auth providers are configured..."

# Remote Config setup instructions
echo ""
echo "======================================"
echo "⚙️  REMOTE CONFIG SETUP"
echo "======================================"
echo ""
echo "Configure Remote Config at:"
echo "https://console.firebase.google.com/project/$PROJECT_ID/config"
echo ""
echo "Add these parameters:"
echo "  ┌─────────────────────────┬──────────┬──────────────────────┐"
echo "  │ Parameter               │ Type     │ Default Value        │"
echo "  ├─────────────────────────┼──────────┼──────────────────────┤"
echo "  │ feature_daily_rewards   │ Boolean  │ true                 │"
echo "  │ feature_kyc_enabled     │ Boolean  │ true                 │"
echo "  │ feature_cp_enabled      │ Boolean  │ true                 │"
echo "  │ feature_video_mode      │ Boolean  │ true                 │"
echo "  │ min_app_version         │ String   │ 1.0.0                │"
echo "  │ maintenance_mode        │ Boolean  │ false                │"
echo "  │ daily_reward_base       │ Number   │ 5000                 │"
echo "  │ exchange_rate           │ Number   │ 0.3                  │"
echo "  └─────────────────────────┴──────────┴──────────────────────┘"
echo ""
read -p "Press Enter when Remote Config is configured..."

# Android configuration
echo ""
echo "======================================"
echo "📱 ANDROID CONFIGURATION"
echo "======================================"
echo ""
echo "1. Add Android app in Firebase Console:"
echo "   - Go to Project Settings → General"
echo "   - Click 'Add app' → Android"
echo "   - Package name: com.aura.voicechat"
echo "   - App nickname: Aura Voice Chat"
echo ""
echo "2. Download google-services.json"
echo "   - Place in: $ANDROID_DIR/app/"
echo ""
echo "3. Add SHA-1 fingerprint for Google Sign-In:"
echo "   - Run: keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android"
echo "   - Add SHA-1 to Firebase Console"
echo ""
read -p "Press Enter when Android configuration is complete..."

# Check for google-services.json
if [ -f "$ANDROID_DIR/app/google-services.json" ]; then
    echo "✅ google-services.json found"
else
    echo "⚠️  google-services.json not found in $ANDROID_DIR/app/"
    echo "   Please download and place it before building the app"
fi

# Summary
echo ""
echo "======================================"
echo "✅ Firebase Setup Complete!"
echo "======================================"
echo ""
echo "Project ID: $PROJECT_ID"
echo ""
echo "What's configured:"
echo "  ✓ Firestore Database"
echo "  ✓ Firestore Security Rules"
echo "  ✓ Cloud Storage"
echo "  ✓ Storage Security Rules"
echo "  ✓ Cloud Functions (if available)"
echo ""
echo "What needs manual setup:"
echo "  • Authentication providers (Phone, Google, Facebook)"
echo "  • Remote Config parameters"
echo "  • Android google-services.json"
echo "  • SHA-1 fingerprints"
echo ""
echo "Firebase Console: https://console.firebase.google.com/project/$PROJECT_ID"
echo ""
echo "Next steps:"
echo "  1. Complete manual setup items above"
echo "  2. Download google-services.json to android/app/"
echo "  3. Build and test the Android app"
echo ""
