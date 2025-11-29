# 🚀 Quick Start Guide

Get Aura Voice Chat up and running in minutes!

**Developer:** Hawkaye Visions LTD — Lahore, Pakistan

---

## Prerequisites

- **Node.js 18+** — [Download](https://nodejs.org/)
- **Git** — [Download](https://git-scm.com/)
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

# Run auto-setup
./scripts/auto-setup.sh
```

The auto-setup script will:
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
./scripts/build-apk.sh
```

Output: `android/build-output/`

---

## ☁️ AWS Infrastructure Setup

### Auto Setup

```bash
./scripts/aws-setup.sh
```

### Manual Setup

See [AWS Setup Guide](docs/aws-setup.md) for detailed instructions.

### CloudFormation Deployment

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
   psql -h YOUR_RDS_ENDPOINT -U aura_admin -d auravoicechat -f backend/src/database/schema.sql
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

### Auto Deploy

SSH into your EC2 instance and run:

```bash
curl -sSL https://raw.githubusercontent.com/venomvex/auravoicechatdoc/main/scripts/deploy-ec2.sh | bash
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
├── aws/              # AWS CloudFormation & scripts
│   ├── cloudformation/
│   ├── scripts/
│   └── policies/
├── backend/          # Node.js/Express API
│   ├── src/
│   │   ├── config/       # AWS & app configuration
│   │   ├── database/     # PostgreSQL schema
│   │   ├── services/     # Cognito, S3, SNS services
│   │   └── sockets/      # Socket.io handlers
│   ├── Dockerfile
│   └── docker-compose.yml
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
| `android/app/src/main/res/raw/amplifyconfiguration.json` | AWS Amplify config |
| `android/app/src/main/res/raw/awsconfiguration.json` | AWS services config |
| `aws/cloudformation/main.yaml` | Infrastructure template |
| `data/*.json` | App configuration data |

---

## 📖 Documentation

| Document | Description |
|----------|-------------|
| [COMPREHENSIVE-GUIDE.md](COMPREHENSIVE-GUIDE.md) | Complete app guide |
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
cat .env

# Check database connection
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
