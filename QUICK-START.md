# 🚀 Quick Start Guide

Get Aura Voice Chat up and running in minutes!

**Developer:** Hawkaye Visions LTD — Lahore, Pakistan

---

## Prerequisites

- **Node.js 18+** — [Download](https://nodejs.org/)
- **Git** — [Download](https://git-scm.com/)
- **Android Studio** (for Android builds) — [Download](https://developer.android.com/studio)
- **Firebase Account** — [Create](https://console.firebase.google.com/)

---

## 🏃‍♂️ Quick Setup (5 minutes)

### 1. Clone & Setup

```bash
# Clone repository
git clone https://github.com/venomvex/auravoicechatdoc.git
cd auravoicechatdoc

# Run auto-setup
./scripts/auto-setup.sh
```

The auto-setup script will:
- ✅ Check prerequisites
- ✅ Install dependencies
- ✅ Create environment files
- ✅ Guide you through Firebase setup
- ✅ Initialize database
- ✅ Build APK (optional)

### 2. Start Backend

```bash
cd backend
npm run dev
```

Backend runs at: `http://localhost:3000`

### 3. Build Android App

```bash
./scripts/build-apk.sh
```

Output: `android/build-output/`

---

## 🔥 Firebase Setup

### Auto Setup

```bash
./scripts/setup-firebase-complete.sh
```

### Manual Setup

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create new project
3. Enable Authentication (Phone, Google)
4. Create Firestore database
5. Download `google-services.json` to `android/app/`
6. Generate service account for backend

---

## ☁️ Deploy to AWS EC2

### Auto Deploy

SSH into your EC2 instance and run:

```bash
curl -sSL https://raw.githubusercontent.com/venomvex/auravoicechatdoc/main/scripts/deploy-ec2.sh | bash
```

### Manual Deploy

See [AWS EC2 Deployment Guide](docs/aws-ec2-deployment.md)

---

## 📱 Build APK for Play Store

```bash
# Debug APK
./scripts/build-apk.sh --debug

# Release AAB (for Play Store)
./scripts/build-apk.sh --release
```

See [Play Store Submission Guide](docs/play-store-submission.md)

---

## 📁 Project Structure

```
auravoicechatdoc/
├── android/          # Android app (Kotlin/Jetpack Compose)
├── backend/          # Node.js/Express API
├── firebase/         # Firebase configs & functions
├── data/             # JSON config files
├── docs/             # Documentation
├── scripts/          # Automation scripts
└── COMPREHENSIVE-GUIDE.md  # Complete documentation
```

---

## 🔧 Configuration Files

| File | Purpose |
|------|---------|
| `backend/.env` | Backend environment variables |
| `android/app/google-services.json` | Firebase config for Android |
| `firebase/firestore.rules` | Firestore security rules |
| `data/*.json` | App configuration data |

---

## 📖 Documentation

| Document | Description |
|----------|-------------|
| [COMPREHENSIVE-GUIDE.md](COMPREHENSIVE-GUIDE.md) | Complete app guide |
| [docs/firebase-setup.md](docs/firebase-setup.md) | Firebase setup |
| [docs/aws-ec2-deployment.md](docs/aws-ec2-deployment.md) | AWS deployment |
| [docs/play-store-submission.md](docs/play-store-submission.md) | Play Store guide |
| [docs/setup/PAYMENT-GATEWAY-SETUP.md](docs/setup/PAYMENT-GATEWAY-SETUP.md) | Payment integration |

---

## 🆘 Troubleshooting

### Backend won't start

```bash
# Check logs
npm run dev

# Verify .env file exists
cat .env

# Check database connection
```

### Firebase errors

```bash
# Login to Firebase
firebase login

# Check project
firebase projects:list
```

### Android build fails

```bash
# Check Android SDK
echo $ANDROID_HOME

# Clean build
cd android && ./gradlew clean
```

---

## 📞 Support

- **Documentation:** [COMPREHENSIVE-GUIDE.md](COMPREHENSIVE-GUIDE.md)
- **Issues:** Open a GitHub issue
- **Email:** support@auravoice.chat

---

**Happy coding! 🎤**

*Hawkaye Visions LTD — Lahore, Pakistan*
