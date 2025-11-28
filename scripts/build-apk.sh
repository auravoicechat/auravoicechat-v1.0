#!/bin/bash

# Aura Voice Chat - APK Build Script
# Developer: Hawkaye Visions LTD — Pakistan
#
# This script automates building signed APK and AAB files

set -e

echo "======================================"
echo "Aura Voice Chat - Build Script"
echo "Developer: Hawkaye Visions LTD"
echo "======================================"
echo ""

# Configuration
BUILD_TYPE=${1:-release}
FLAVOR=${2:-prod}
OUTPUT_DIR="./build-output"
ANDROID_DIR="$(dirname "$0")/../android"

# Navigate to android directory
cd "$ANDROID_DIR"

# Check for required files
if [ ! -f "app/google-services.json" ]; then
    echo "❌ Error: google-services.json not found in app/"
    echo "   Please download it from Firebase Console"
    exit 1
fi

# Check for keystore (release builds only)
if [ "$BUILD_TYPE" == "release" ]; then
    if [ ! -f "keystore.properties" ] && [ -z "$KEYSTORE_PATH" ]; then
        echo "⚠️  Warning: No keystore configuration found"
        echo "   Create keystore.properties or set environment variables:"
        echo "   - KEYSTORE_PATH"
        echo "   - KEYSTORE_PASSWORD"
        echo "   - KEY_ALIAS"
        echo "   - KEY_PASSWORD"
        echo ""
        read -p "Continue with debug signing? (y/n): " CONTINUE
        if [ "$CONTINUE" != "y" ]; then
            exit 1
        fi
        BUILD_TYPE="debug"
    fi
fi

# Clean previous builds
echo "🧹 Cleaning previous builds..."
./gradlew clean

# Capitalize first letter for Gradle task
FLAVOR_CAP="$(tr '[:lower:]' '[:upper:]' <<< ${FLAVOR:0:1})${FLAVOR:1}"
BUILD_TYPE_CAP="$(tr '[:lower:]' '[:upper:]' <<< ${BUILD_TYPE:0:1})${BUILD_TYPE:1}"

# Build
echo "🔨 Building ${FLAVOR}${BUILD_TYPE_CAP}..."

if [ "$BUILD_TYPE" == "release" ]; then
    ./gradlew assemble${FLAVOR_CAP}Release bundle${FLAVOR_CAP}Release
else
    ./gradlew assemble${FLAVOR_CAP}Debug
fi

# Create output directory
mkdir -p "$OUTPUT_DIR"

# Copy outputs
echo "📦 Copying build outputs..."

if [ "$BUILD_TYPE" == "release" ]; then
    cp app/build/outputs/apk/${FLAVOR}/release/*.apk "$OUTPUT_DIR/" 2>/dev/null || true
    cp app/build/outputs/bundle/${FLAVOR}Release/*.aab "$OUTPUT_DIR/" 2>/dev/null || true
    cp app/build/outputs/mapping/${FLAVOR}Release/mapping.txt "$OUTPUT_DIR/" 2>/dev/null || true
else
    cp app/build/outputs/apk/${FLAVOR}/debug/*.apk "$OUTPUT_DIR/" 2>/dev/null || true
fi

# Print results
echo ""
echo "✅ Build complete!"
echo "📁 Output directory: $OUTPUT_DIR"
echo ""
ls -la "$OUTPUT_DIR" 2>/dev/null || echo "No output files found"

# Calculate APK size
APK_FILES=$(ls "$OUTPUT_DIR"/*.apk 2>/dev/null)
if [ -n "$APK_FILES" ]; then
    echo ""
    echo "📊 APK Details:"
    for apk in $APK_FILES; do
        SIZE=$(ls -lh "$apk" | awk '{print $5}')
        NAME=$(basename "$apk")
        echo "   $NAME: $SIZE"
    done
fi

# Verify signature (release only)
if [ "$BUILD_TYPE" == "release" ]; then
    echo ""
    echo "🔐 Verifying APK signature..."
    for apk in "$OUTPUT_DIR"/*.apk; do
        if command -v apksigner &> /dev/null; then
            apksigner verify --verbose "$apk" 2>/dev/null || true
        else
            echo "   (apksigner not found, skipping verification)"
        fi
        break
    done
fi

echo ""
echo "======================================"
echo "Build Summary"
echo "======================================"
echo "Build Type: $BUILD_TYPE"
echo "Flavor: $FLAVOR"
echo "Output: $OUTPUT_DIR"
echo ""
echo "Next steps:"
echo "  1. Test the APK on a device"
echo "  2. Upload AAB to Play Console (for release)"
echo "  3. Keep mapping.txt for crash reports"
echo ""
