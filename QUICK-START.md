# 🚀 Quick Start Guide

Get Aura Voice Chat up and running in minutes!

**Developer:** Hawkaye Visions LTD — Lahore, Pakistan

---

## Prerequisites

- **Node.js 18+** — [Download](https://nodejs.org/)
- **Git** — [Download](https://git-scm.com/)
- **JDK 17+** (for Android builds) — [Download](https://adoptium.net/)
- **Android Studio** (for Android builds) — [Download](https://developer.android.com/studio)
- **AWS Account** — [Create](https://aws.amazon.com/)
- **AWS CLI** — [Install](https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html)

---

## 🏃‍♂️ Quick Setup (10 minutes)

### 1. Clone & Setup

```bash
# Clone repository
git clone https://github.com/venomvex/auravoicechatdoc.git
cd auravoicechatdoc

# Run master setup script
./setup.sh
```

The setup script will:
- ✅ Check prerequisites
- ✅ Install dependencies
- ✅ Create environment files
- ✅ Guide you through AWS setup
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
./setup.sh --android
```

Output: `android/app/build/outputs/apk/`

---

## ☁️ AWS Infrastructure Setup

### Using Setup Script

```bash
./setup.sh --aws
```

### Manual CloudFormation Deployment

```bash
aws cloudformation create-stack \
    --stack-name aura-voice-chat-production \
    --template-body file://aws/cloudformation/main.yaml \
    --parameters \
        ParameterKey=DBPassword,ParameterValue=YOUR_PASSWORD \
        ParameterKey=AdminEmail,ParameterValue=admin@example.com \
        ParameterKey=KeyPairName,ParameterValue=your-key-pair \
    --capabilities CAPABILITY_IAM CAPABILITY_NAMED_IAM
```

See [AWS Setup Guide](docs/aws-setup.md) for detailed instructions.

---

## 🔐 AWS Cognito Setup

Cognito handles user authentication:

1. Created automatically with CloudFormation
2. Get credentials:
   ```bash
   aws cloudformation describe-stacks \
       --stack-name aura-voice-chat-production \
       --query 'Stacks[0].Outputs'
   ```
3. Update `backend/.env` with Cognito IDs

See [Cognito Setup Guide](docs/cognito-setup.md)

---

## 💾 Database Setup

PostgreSQL database on AWS RDS:

1. Created automatically with CloudFormation
2. Initialize schema:
   ```bash
   cd backend
   npx prisma db push
   ```

See [RDS Setup Guide](docs/rds-setup.md)

---

## 📁 File Storage (S3)

S3 bucket for file storage:

1. Created automatically with CloudFormation
2. Configure Android app with bucket name
3. Use presigned URLs for uploads

See [S3 Setup Guide](docs/s3-setup.md)

---

## ☁️ Deploy to AWS EC2

### Using Setup Script

```bash
./setup.sh --deploy
```

### Using Docker

```bash
cd backend
docker-compose up -d
```

### Manual Deploy

See [AWS EC2 Deployment Guide](docs/aws-ec2-deployment.md)

---

## 📱 Build APK for Play Store

```bash
# Using setup script
./setup.sh --android

# Or manually
cd android
./gradlew assembleDevDebug      # Debug APK
./gradlew bundleProdRelease     # Release AAB for Play Store
```

See [Play Store Submission Guide](docs/play-store-submission.md)

---

## 📁 Project Structure

```
auravoicechatdoc/
├── android/          # Android app (Kotlin/Jetpack Compose)
│   ├── app/          # App module
│   │   └── build.gradle.kts    # App dependencies (Kotlin DSL)
│   ├── gradle/
│   │   └── libs.versions.toml  # Version catalog
│   └── build.gradle  # Project configuration
├── aws/              # AWS CloudFormation & scripts
│   ├── cloudformation/
│   ├── scripts/
│   └── policies/
├── backend/          # Node.js/Express API
│   ├── src/
│   │   ├── config/       # AWS & app configuration
│   │   ├── services/     # Cognito, S3, SNS services
│   │   └── routes/       # API routes
│   ├── prisma/           # Database schema
│   ├── Dockerfile
│   └── docker-compose.yml
├── data/             # JSON config files
├── docs/             # Documentation
├── setup.sh          # Master setup script
├── DEVELOPER-GUIDE.md    # Complete developer guide
└── COMPREHENSIVE-GUIDE.md  # Product specification
```

---

## 🔧 Configuration Files

| File | Purpose |
|------|---------|
| `backend/.env` | Backend environment variables |
| `android/gradle/libs.versions.toml` | Dependency versions |
| `android/app/src/main/res/raw/awsconfiguration.json` | AWS services config |
| `aws/cloudformation/main.yaml` | Infrastructure template |
| `data/*.json` | App configuration data |

---

## 📖 Documentation

| Document | Description |
|----------|-------------|
| [DEVELOPER-GUIDE.md](DEVELOPER-GUIDE.md) | Complete developer guide |
| [COMPREHENSIVE-GUIDE.md](COMPREHENSIVE-GUIDE.md) | Product specification |
| [docs/aws-setup.md](docs/aws-setup.md) | AWS infrastructure setup |
| [docs/cognito-setup.md](docs/cognito-setup.md) | Cognito authentication |
| [docs/rds-setup.md](docs/rds-setup.md) | PostgreSQL database |
| [docs/s3-setup.md](docs/s3-setup.md) | S3 file storage |
| [docs/aws-ec2-deployment.md](docs/aws-ec2-deployment.md) | AWS deployment |
| [docs/play-store-submission.md](docs/play-store-submission.md) | Play Store guide |

---

## 🆘 Troubleshooting

### Backend won't start

```bash
# Check logs
npm run dev

# Verify .env file exists
cat backend/.env

# Check database connection
cd backend && npx prisma db push
```

### AWS errors

```bash
# Check AWS credentials
aws sts get-caller-identity

# Check CloudFormation stack
aws cloudformation describe-stacks --stack-name aura-voice-chat-production
```

### Android build fails

```bash
# Check Java version
java -version

# Check Android SDK
echo $ANDROID_HOME

# Clean build
cd android && ./gradlew clean
```

---

## 📞 Support

- **Documentation:** [DEVELOPER-GUIDE.md](DEVELOPER-GUIDE.md)
- **Issues:** Open a GitHub issue
- **Email:** support@auravoice.chat

---

**Happy coding! 🎤**

*Hawkaye Visions LTD — Lahore, Pakistan*
