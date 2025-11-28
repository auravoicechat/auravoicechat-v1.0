#!/bin/bash

# Aura Voice Chat - Firebase Setup Script
# Developer: Hawkaye Visions LTD — Pakistan
#
# This script automates Firebase project setup including:
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
    echo "📦 Installing Firebase CLI..."
    npm install -g firebase-tools
fi

echo "✅ Firebase CLI $(firebase --version) detected"

# Login to Firebase
echo ""
echo "🔐 Logging in to Firebase..."
firebase login

# Create new project or use existing
echo ""
echo "📁 Select or create Firebase project..."
echo "   Options:"
echo "   1. Create new project"
echo "   2. Use existing project"
read -p "   Enter choice (1 or 2): " PROJECT_CHOICE

if [ "$PROJECT_CHOICE" == "1" ]; then
    read -p "   Enter project ID (e.g., aura-voice-chat-prod): " PROJECT_ID
    firebase projects:create "$PROJECT_ID" --display-name "Aura Voice Chat"
else
    firebase projects:list
    read -p "   Enter project ID from list above: " PROJECT_ID
fi

# Use the project
firebase use "$PROJECT_ID"

echo ""
echo "🔧 Initializing Firebase features..."

# Initialize Firestore
echo "  📊 Setting up Firestore..."
firebase firestore:databases:create default --location=us-central

# Deploy Firestore rules
echo "  📜 Deploying Firestore security rules..."
firebase deploy --only firestore:rules

# Initialize Storage
echo "  📁 Setting up Cloud Storage..."
firebase deploy --only storage

# Initialize Authentication
echo "  🔐 Setting up Authentication..."
echo ""
echo "⚠️  MANUAL STEP REQUIRED:"
echo "   Please enable the following auth providers in Firebase Console:"
echo "   1. Phone (for OTP login)"
echo "   2. Google (for Google login)"
echo "   3. Facebook (for Facebook login)"
echo ""
echo "   Go to: https://console.firebase.google.com/project/$PROJECT_ID/authentication/providers"
echo ""
read -p "   Press Enter when done..."

# Initialize Cloud Functions
echo "  ⚡ Setting up Cloud Functions..."
cd functions
npm install
cd ..

# Deploy Cloud Functions
firebase deploy --only functions

# Initialize Remote Config
echo "  ⚙️  Setting up Remote Config..."
echo ""
echo "⚠️  MANUAL STEP REQUIRED:"
echo "   Please configure Remote Config in Firebase Console:"
echo "   - feature_daily_rewards: true"
echo "   - feature_kyc_enabled: true"
echo "   - min_app_version: 1.0.0"
echo "   - maintenance_mode: false"
echo ""
echo "   Go to: https://console.firebase.google.com/project/$PROJECT_ID/config"
echo ""
read -p "   Press Enter when done..."

# Enable Analytics
echo "  📈 Analytics is automatically enabled."

# Generate google-services.json
echo ""
echo "📱 Generating Android configuration..."
echo ""
echo "⚠️  MANUAL STEP REQUIRED:"
echo "   Please download google-services.json from Firebase Console:"
echo ""
echo "   1. Go to Project Settings → General"
echo "   2. Under 'Your apps', click 'Add app' → Android"
echo "   3. Enter package name: com.aura.voicechat"
echo "   4. Download google-services.json"
echo "   5. Place it in android/app/ directory"
echo ""
echo "   Go to: https://console.firebase.google.com/project/$PROJECT_ID/settings/general"
echo ""

# Summary
echo ""
echo "======================================"
echo "✅ Firebase Setup Complete!"
echo "======================================"
echo ""
echo "Project ID: $PROJECT_ID"
echo ""
echo "Next steps:"
echo "  1. Download google-services.json"
echo "  2. Enable auth providers in Console"
echo "  3. Configure Remote Config"
echo "  4. Set up Facebook App ID"
echo "  5. Configure Twilio for OTP"
echo ""
echo "Firebase Console: https://console.firebase.google.com/project/$PROJECT_ID"
echo ""
