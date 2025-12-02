# Aura Voice Chat — Developer Guide

**Developed by:** Hawkaye Visions LTD — Lahore, Pakistan  
**Last Updated:** November 2025

---

## 📖 Table of Contents

1. [Project Overview](#project-overview)
2. [Repository Structure](#repository-structure)
3. [Prerequisites](#prerequisites)
4. [Quick Start](#quick-start)
5. [Android App Setup](#android-app-setup)
6. [Backend Setup](#backend-setup)
7. [AWS Configuration](#aws-configuration)
8. [Building the APK](#building-the-apk)
9. [Deployment Guide](#deployment-guide)
10. [Configuration Reference](#configuration-reference)
11. [Troubleshooting](#troubleshooting)

---

## Project Overview

Aura Voice Chat is a mobile-first social voice and video chat application that enables users to:

- **Connect globally** through live voice and video chat rooms
- **Build relationships** via CP (Couple Partnership), Family, and Friend systems
- **Earn rewards** through daily logins, games, events, and referrals
- **Express themselves** with gifts, frames, vehicles, and cosmetic items
- **Monetize activity** through the earning and reseller systems

### Tech Stack

| Component | Technology | Version |
|-----------|------------|---------|
| **Android App** | Kotlin, Jetpack Compose | Kotlin 2.0.21, Compose BOM 2024.11 |
| **Backend** | Node.js, Express, TypeScript | Node 18+, TypeScript 5.3+ |
| **Database** | PostgreSQL, Redis | PostgreSQL 15+, Redis 7+ |
| **ORM** | Prisma | 5.7+ |
| **Authentication** | AWS Cognito | AWS Amplify 2.22+ |
| **Storage** | AWS S3 | AWS SDK Kotlin 1.3+ |
| **Build System** | Gradle | 8.11.1 with Version Catalog |

### Theme & Branding

- **Primary Color:** Purple (#c9a8f1) → White gradient
- **Accent Colors:** Magenta (#d958ff), Cyan (#35e8ff)
- **Dark Canvas:** #12141a
- **Minimum Android:** 9+ (API 28)
- **Target Android:** 15 (API 35)

---

## Repository Structure

```
auravoicechatdoc/
├── android/                     # 📱 Android Application
│   ├── app/
│   │   ├── src/
│   │   │   └── main/
│   │   │       ├── java/        # Kotlin source code
│   │   │       ├── res/         # Resources (layouts, drawables, etc.)
│   │   │       │   └── raw/     # AWS configuration files
│   │   │       └── AndroidManifest.xml
│   │   ├── build.gradle.kts     # App-level build configuration
│   │   └── proguard-rules.pro   # ProGuard rules for release builds
│   ├── gradle/
│   │   ├── libs.versions.toml   # 📌 Version Catalog (all dependencies)
│   │   └── wrapper/
│   │       └── gradle-wrapper.properties
│   ├── build.gradle             # Project-level build configuration
│   ├── settings.gradle          # Project settings
│   └── gradle.properties        # Gradle configuration
│
├── backend/                     # 🖥️ Node.js Backend API
│   ├── src/
│   │   ├── controllers/         # API controllers
│   │   ├── routes/              # Express routes
│   │   ├── services/            # Business logic
│   │   ├── middleware/          # Express middleware
│   │   ├── models/              # Data models
│   │   └── index.ts             # Entry point
│   ├── prisma/
│   │   └── schema.prisma        # Database schema
│   ├── package.json             # Node.js dependencies
│   ├── tsconfig.json            # TypeScript configuration
│   ├── .env.example             # Environment template
│   └── ecosystem.config.js      # PM2 configuration
│
├── aws/                         # ☁️ AWS Infrastructure
│   ├── cloudformation/
│   │   └── main.yaml            # CloudFormation template
│   ├── policies/                # IAM policies
│   └── scripts/                 # AWS deployment scripts
│
├── data/                        # 📊 Game & App Data (JSON)
│   ├── animations.json
│   ├── cosmetics.json
│   ├── games.json
│   ├── events.json
│   ├── level-rewards.json
│   ├── medals.json
│   └── ...
│
├── docs/                        # 📚 Documentation
│   ├── features/                # Feature specifications
│   ├── setup/                   # Setup guides
│   ├── ui/                      # UI/UX documentation
│   └── design/                  # Design system
│
├── setup.sh                     # 🚀 Master Setup Script
├── DEVELOPER-GUIDE.md           # This file
├── README.md                    # Product specification
├── COMPREHENSIVE-GUIDE.md       # Complete implementation guide
├── api.md                       # API reference
├── architecture.md              # System architecture
└── deployment.md                # Deployment guide
```

---

## Prerequisites

### Development Machine

| Requirement | Minimum Version | Installation |
|-------------|-----------------|--------------|
| **Node.js** | 18.x | [nodejs.org](https://nodejs.org/) |
| **JDK** | 17 | [adoptium.net](https://adoptium.net/) |
| **Android Studio** | Hedgehog (2023.1.1)+ | [developer.android.com](https://developer.android.com/studio) |
| **Git** | 2.x | [git-scm.com](https://git-scm.com/) |

### Optional Tools

| Tool | Purpose | Installation |
|------|---------|--------------|
| **AWS CLI** | AWS deployment | [aws.amazon.com/cli](https://aws.amazon.com/cli/) |
| **PostgreSQL** | Local database | [postgresql.org](https://postgresql.org/) |
| **Redis** | Local caching | [redis.io](https://redis.io/) |
| **PM2** | Process management | `npm install -g pm2` |

### Verify Installation

```bash
# Check Node.js
node -v
# Expected: v18.x.x or higher

# Check npm
npm -v
# Expected: 9.x.x or higher

# Check Java
java -version
# Expected: openjdk version "17.x.x" or higher

# Check Git
git --version
# Expected: git version 2.x.x
```

---

## Quick Start

### Option 1: Using Master Setup Script (Recommended)

```bash
# Clone the repository
git clone https://github.com/venomvex/auravoicechatdoc.git
cd auravoicechatdoc

# Run the master setup script
./setup.sh

# Or run specific tasks:
./setup.sh --backend    # Backend only
./setup.sh --android    # Android build only
./setup.sh --aws        # AWS infrastructure only
./setup.sh --help       # Show all options
```

### Option 2: Manual Setup

```bash
# Clone the repository
git clone https://github.com/venomvex/auravoicechatdoc.git
cd auravoicechatdoc

# Backend setup
cd backend
npm install
cp .env.example .env
# Edit .env with your credentials
npm run build
npm run dev

# Android setup (in new terminal)
cd android
./gradlew assembleDevDebug
```

---

## Android App Setup

### Step 1: Open in Android Studio

1. Open Android Studio
2. Select "Open an Existing Project"
3. Navigate to the `android/` directory
4. Wait for Gradle sync to complete

### Step 2: Configure AWS

Create `android/app/src/main/res/raw/awsconfiguration.json`:

```json
{
    "Version": "1.0",
    "CredentialsProvider": {
        "CognitoIdentity": {
            "Default": {
                "PoolId": "YOUR_IDENTITY_POOL_ID",
                "Region": "us-east-1"
            }
        }
    },
    "CognitoUserPool": {
        "Default": {
            "PoolId": "YOUR_USER_POOL_ID",
            "AppClientId": "YOUR_APP_CLIENT_ID",
            "Region": "us-east-1"
        }
    },
    "S3TransferUtility": {
        "Default": {
            "Bucket": "YOUR_S3_BUCKET",
            "Region": "us-east-1"
        }
    },
    "PinpointAnalytics": {
        "Default": {
            "AppId": "YOUR_PINPOINT_APP_ID",
            "Region": "us-east-1"
        }
    }
}
```

### Step 3: Configure local.properties

Create `android/local.properties`:

```properties
sdk.dir=/path/to/your/Android/Sdk
```

Common paths:
- **Linux:** `/home/username/Android/Sdk`
- **macOS:** `/Users/username/Library/Android/sdk`
- **Windows:** `C:\\Users\\username\\AppData\\Local\\Android\\Sdk`

### Step 4: Sync Gradle

Click "Sync Now" in Android Studio or run:

```bash
cd android
./gradlew --refresh-dependencies
```

---

## Backend Setup

### Step 1: Install Dependencies

```bash
cd backend
npm install
```

### Step 2: Configure Environment

```bash
cp .env.example .env
```

Edit `.env` with your configuration:

```env
# Server Configuration
NODE_ENV=development
PORT=3000
HOST=0.0.0.0

# Database
DATABASE_URL=postgresql://postgres:password@localhost:5432/auravoicechat
DB_HOST=localhost
DB_PORT=5432
DB_NAME=auravoicechat
DB_USER=postgres
DB_PASSWORD=your_password

# Redis (optional for local development)
REDIS_URL=redis://localhost:6379

# JWT Secrets (generate secure random strings)
JWT_SECRET=your-super-secret-jwt-key-min-32-chars
JWT_EXPIRES_IN=7d
JWT_REFRESH_SECRET=your-super-secret-refresh-key
JWT_REFRESH_EXPIRES_IN=30d

# AWS Configuration
AWS_REGION=us-east-1
AWS_ACCESS_KEY_ID=your-access-key
AWS_SECRET_ACCESS_KEY=your-secret-key

# Cognito
COGNITO_USER_POOL_ID=us-east-1_XXXXXXXXX
COGNITO_CLIENT_ID=your-client-id
COGNITO_CLIENT_SECRET=your-client-secret

# S3
S3_BUCKET_NAME=your-bucket-name

# Twilio (for OTP)
TWILIO_ACCOUNT_SID=your-account-sid
TWILIO_AUTH_TOKEN=your-auth-token
TWILIO_PHONE_NUMBER=+1234567890
```

### Step 3: Setup Database

```bash
# Using Prisma
npx prisma generate
npx prisma db push

# Or run migrations
npx prisma migrate dev
```

### Step 4: Start Development Server

```bash
npm run dev
```

The API will be available at `http://localhost:3000`

### API Endpoints

| Endpoint | Description |
|----------|-------------|
| `GET /health` | Health check |
| `POST /api/v1/auth/register` | User registration |
| `POST /api/v1/auth/login` | User login |
| `GET /api/v1/users/me` | Get current user |
| See `api.md` | Full API documentation |

---

## AWS Configuration

### Option 1: CloudFormation (Recommended)

```bash
# Using the setup script
./setup.sh --aws

# Or manually
cd aws/scripts
./create-stack.sh production us-east-1
```

### Option 2: Manual Setup

1. **Create Cognito User Pool**
   - Enable phone and email sign-in
   - Configure app client with auth flows

2. **Create S3 Bucket**
   - Enable CORS for your domains
   - Configure lifecycle rules for old uploads

3. **Create RDS PostgreSQL**
   - Use db.t3.micro for development
   - Configure security groups for backend access

4. **Create SNS Topic**
   - For push notifications

See `docs/aws-setup.md` for detailed instructions.

---

## Building the APK

### Debug Build

```bash
cd android

# Build debug APK for development flavor
./gradlew assembleDevDebug

# Output: app/build/outputs/apk/dev/debug/app-dev-debug.apk
```

### Release Build

#### Step 1: Create Keystore

```bash
keytool -genkey -v -keystore release.keystore \
  -alias aura-release-key \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000 \
  -storepass your-store-password \
  -keypass your-key-password \
  -dname "CN=Aura Voice Chat, OU=Mobile, O=Hawkaye Visions LTD, L=Lahore, ST=Punjab, C=PK"
```

⚠️ **IMPORTANT:** Keep your keystore safe! You cannot update the app without it.

#### Step 2: Configure Signing

Create `android/keystore.properties`:

```properties
storeFile=../release.keystore
storePassword=your-store-password
keyAlias=aura-release-key
keyPassword=your-key-password
```

#### Step 3: Build Release APK

```bash
cd android

# Build release APK
./gradlew assembleProdRelease

# Output: app/build/outputs/apk/prod/release/app-prod-release.apk

# Build AAB for Play Store
./gradlew bundleProdRelease

# Output: app/build/outputs/bundle/prodRelease/app-prod-release.aab
```

### Build Variants

| Variant | API URL | Use Case |
|---------|---------|----------|
| devDebug | api-dev.auravoice.chat | Development |
| stagingDebug | api-staging.auravoice.chat | Testing |
| prodDebug | api.auravoice.chat | Production testing |
| prodRelease | api.auravoice.chat | Play Store |

### Build Commands Reference

```bash
# Clean build
./gradlew clean

# Build all debug variants
./gradlew assembleDebug

# Build all release variants
./gradlew assembleRelease

# Build specific variant
./gradlew assembleDevDebug
./gradlew assembleStagingRelease
./gradlew assembleProdRelease

# Build AAB for Play Store
./gradlew bundleProdRelease

# Run lint checks
./gradlew lint

# Run tests
./gradlew test
```

---

## Deployment Guide

### Backend Deployment to EC2

#### Using Setup Script

```bash
./setup.sh --deploy
```

#### Manual Deployment

1. **Launch EC2 Instance**
   - Ubuntu 22.04 LTS
   - t3.medium or larger
   - Security group: ports 22, 80, 443, 3000

2. **SSH and Install Dependencies**

```bash
ssh -i your-key.pem ubuntu@your-ec2-ip

# Update system
sudo apt update && sudo apt upgrade -y

# Install Node.js
curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -
sudo apt install -y nodejs

# Install PM2
sudo npm install -g pm2

# Install Nginx
sudo apt install -y nginx

# Install PostgreSQL (if not using RDS)
sudo apt install -y postgresql postgresql-contrib
```

3. **Clone and Setup**

```bash
# Clone repository
git clone https://github.com/venomvex/auravoicechatdoc.git
cd auravoicechatdoc/backend

# Install dependencies
npm install

# Build
npm run build

# Create .env (edit with production values)
cp .env.example .env
nano .env

# Run migrations
npx prisma migrate deploy

# Start with PM2
pm2 start dist/index.js --name aura-backend
pm2 save
pm2 startup
```

4. **Configure Nginx**

```nginx
# /etc/nginx/sites-available/aura-voice-chat
server {
    listen 80;
    server_name api.yourdomain.com;

    location / {
        proxy_pass http://127.0.0.1:3000;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

5. **Enable HTTPS with Certbot**

```bash
sudo apt install certbot python3-certbot-nginx
sudo certbot --nginx -d api.yourdomain.com
```

### Play Store Submission

See `docs/play-store-submission.md` for complete guide.

**Checklist:**
- [ ] Google Play Developer account ($25)
- [ ] Signed release AAB
- [ ] Privacy Policy URL
- [ ] App icon (512x512)
- [ ] Feature graphic (1024x500)
- [ ] Screenshots
- [ ] Content rating completed
- [ ] Data safety form completed

---

## Configuration Reference

### Gradle Version Catalog

All dependencies are centralized in `android/gradle/libs.versions.toml`:

```toml
[versions]
agp = "8.7.3"
kotlin = "2.0.21"
composeBom = "2024.11.00"
# ... see file for all versions

[libraries]
androidx-core-ktx = { group = "androidx.core", name = "core-ktx", version.ref = "coreKtx" }
# ... see file for all libraries

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
# ... see file for all plugins
```

### Environment Variables

| Variable | Description | Required |
|----------|-------------|----------|
| `NODE_ENV` | Environment (development/production) | Yes |
| `PORT` | Server port | Yes |
| `DATABASE_URL` | PostgreSQL connection string | Yes |
| `JWT_SECRET` | JWT signing key | Yes |
| `AWS_REGION` | AWS region | Yes |
| `COGNITO_USER_POOL_ID` | Cognito User Pool ID | Yes |
| `S3_BUCKET_NAME` | S3 bucket name | Yes |
| `REDIS_URL` | Redis connection string | No |
| `TWILIO_ACCOUNT_SID` | Twilio credentials | For OTP |

---

## Troubleshooting

### Common Issues

#### Gradle Sync Failed

```bash
# Clear Gradle cache
cd android
./gradlew clean
rm -rf ~/.gradle/caches
./gradlew --refresh-dependencies
```

#### Node modules issues

```bash
cd backend
rm -rf node_modules package-lock.json
npm install
```

#### Prisma client errors

```bash
npx prisma generate
```

#### AWS credentials not working

```bash
# Verify credentials
aws sts get-caller-identity

# Re-configure
aws configure
```

#### Android SDK not found

Create or update `android/local.properties`:
```properties
sdk.dir=/path/to/Android/Sdk
```

#### Memory issues during build

Update `android/gradle.properties`:
```properties
org.gradle.jvmargs=-Xmx4g -XX:+UseParallelGC
```

#### BlockHound Integration Warning

**Issue:** `Unexpected reference to missing service class: META-INF/services/reactor.blockhound.integration.BlockHoundIntegration`

**Cause:** This warning occurs due to the reactor-blockhound integration library reference being present in dependencies (like AWS SDK v2) but the actual BlockHound library not being included.

**Resolution:** This is handled in two ways:
1. In `build.gradle.kts` by excluding the service file in the packaging section:
```kotlin
packaging {
    resources {
        excludes += "/META-INF/services/reactor.blockhound.integration.BlockHoundIntegration"
    }
}
```
2. In `proguard-rules.pro` by adding dontwarn rules for R8:
```proguard
-dontwarn reactor.blockhound.**
-dontwarn io.netty.util.internal.Hidden$NettyBlockHoundIntegration
```

#### R8/ProGuard Missing Classes Errors

**Issue:** During release builds, R8 may report missing classes:
- `javax.naming.*` classes (JNDI/LDAP)
- `software.amazon.awssdk.crt.*` classes (AWS CRT)
- `reactor.blockhound.integration.BlockHoundIntegration`

**Cause:** These classes are referenced by dependencies (AWS SDK v2, Apache HTTP Client) but are either not available on Android or are optional native components.

**Resolution:** Add dontwarn rules in `proguard-rules.pro`:
```proguard
# AWS SDK v2 and CRT
-dontwarn software.amazon.awssdk.**
-dontwarn software.amazon.awssdk.crt.**

# JNDI classes (not available on Android)
-dontwarn javax.naming.**

# Apache HTTP hostname verifier
-dontwarn org.apache.http.conn.ssl.DefaultHostnameVerifier
```

#### Deprecation Warnings During Compilation

During compilation, you may see deprecation warnings for:

1. **Material Icons:** Icons like `Icons.Filled.ArrowBack`, `Icons.Filled.Send`, etc. are deprecated in favor of AutoMirrored versions (e.g., `Icons.AutoMirrored.Filled.ArrowBack`). These warnings do not affect functionality.

2. **Google Sign-In API:** `GoogleSignIn` and `GoogleSignInOptions` classes are deprecated. Consider migrating to the new Credential Manager API in future versions.

3. **Compose Components:** Some components like `Divider` are renamed (now `HorizontalDivider`).

4. **Window APIs:** `statusBarColor` on Window is deprecated in newer Android versions.

These are warnings, not errors, and do not prevent successful builds. They indicate APIs that may be removed in future library versions.

### Getting Help

1. Check the documentation in `docs/`
2. Review `troubleshooting.md`
3. Check GitHub Issues
4. Contact the development team

---

## Version Information

| Component | Version |
|-----------|---------|
| Android Gradle Plugin | 8.7.3 |
| Kotlin | 2.0.21 |
| Compose BOM | 2024.11.00 |
| Gradle | 8.11.1 |
| Node.js | 18+ |
| TypeScript | 5.3+ |

---

**Happy Coding! 🚀**

*Hawkaye Visions LTD — Lahore, Pakistan*
