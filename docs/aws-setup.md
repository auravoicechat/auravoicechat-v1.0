# AWS Setup Guide

**Developer:** Hawkaye Visions LTD — Lahore, Pakistan

This guide covers the complete AWS infrastructure setup for Aura Voice Chat.

---

## Prerequisites

- AWS Account with appropriate permissions
- AWS CLI installed and configured
- Node.js 18+ installed
- Git installed

---

## Quick Start with CloudFormation

The easiest way to set up the AWS infrastructure is using the provided CloudFormation templates.

### 1. Configure AWS CLI

```bash
aws configure
# Enter your AWS Access Key ID
# Enter your AWS Secret Access Key
# Enter your default region (e.g., us-east-1)
# Enter output format (json)
```

### 2. Create an EC2 Key Pair

```bash
aws ec2 create-key-pair --key-name aura-voice-chat-key --query 'KeyMaterial' --output text > aura-voice-chat-key.pem
chmod 400 aura-voice-chat-key.pem
```

### 3. Deploy the CloudFormation Stack

```bash
cd scripts
./aws-setup.sh
```

Or manually:

```bash
aws cloudformation create-stack \
    --stack-name aura-voice-chat-production \
    --template-body file://aws/cloudformation/main.yaml \
    --parameters \
        ParameterKey=Environment,ParameterValue=production \
        ParameterKey=DBPassword,ParameterValue=YOUR_DB_PASSWORD \
        ParameterKey=AdminEmail,ParameterValue=admin@example.com \
        ParameterKey=KeyPairName,ParameterValue=aura-voice-chat-key \
    --capabilities CAPABILITY_IAM CAPABILITY_NAMED_IAM
```

### 4. Wait for Stack Creation

```bash
aws cloudformation wait stack-create-complete --stack-name aura-voice-chat-production
```

### 5. Get Stack Outputs

```bash
aws cloudformation describe-stacks \
    --stack-name aura-voice-chat-production \
    --query 'Stacks[0].Outputs' \
    --output table
```

---

## Manual Setup

If you prefer to set up resources manually:

### VPC and Networking

1. Create VPC with CIDR 10.0.0.0/16
2. Create Internet Gateway and attach to VPC
3. Create public subnets (10.0.1.0/24, 10.0.2.0/24)
4. Create private subnets (10.0.10.0/24, 10.0.11.0/24)
5. Create route tables and associate with subnets
6. Create security groups for EC2 and RDS

### RDS PostgreSQL

1. Create DB subnet group with private subnets
2. Create RDS PostgreSQL instance:
   - Engine: PostgreSQL 15
   - Instance class: db.t3.micro
   - Storage: 20GB gp3
   - Enable encryption

### S3 Bucket

1. Create bucket with unique name
2. Enable server-side encryption (AES-256)
3. Block all public access
4. Configure CORS for web uploads

### Cognito User Pool

1. Create User Pool with email/phone sign-in
2. Configure password policy
3. Create app client with client secret
4. Create Identity Pool for AWS credentials

### EC2 Instance

1. Launch EC2 instance:
   - AMI: Ubuntu 22.04 LTS
   - Instance type: t3.micro or t3.small
   - Security group: Allow SSH, HTTP, HTTPS, port 3000
2. Assign Elastic IP
3. Configure IAM role for S3, Cognito, SNS access

---

## Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                       Internet                               │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                    CloudFront (CDN)                          │
│              (Optional - for static assets)                  │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      VPC (10.0.0.0/16)                       │
│  ┌─────────────────────────────────────────────────────┐    │
│  │              Public Subnet (10.0.1.0/24)             │    │
│  │                                                       │    │
│  │  ┌──────────────┐                                    │    │
│  │  │    EC2       │  ← Node.js + Socket.io             │    │
│  │  │   (Nginx)    │                                    │    │
│  │  └──────────────┘                                    │    │
│  │         │                                            │    │
│  └─────────┼────────────────────────────────────────────┘    │
│            │                                                  │
│  ┌─────────┼────────────────────────────────────────────┐    │
│  │         ▼        Private Subnet (10.0.10.0/24)        │    │
│  │                                                       │    │
│  │  ┌──────────────┐    ┌──────────────┐               │    │
│  │  │     RDS      │    │    Redis     │               │    │
│  │  │  PostgreSQL  │    │   (ElastiC.) │               │    │
│  │  └──────────────┘    └──────────────┘               │    │
│  │                                                       │    │
│  └───────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
          │                    │                    │
          ▼                    ▼                    ▼
    ┌──────────┐        ┌──────────┐        ┌──────────┐
    │    S3    │        │ Cognito  │        │   SNS    │
    │ Storage  │        │   Auth   │        │  Push    │
    └──────────┘        └──────────┘        └──────────┘
```

---

## Security Best Practices

1. **Use IAM Roles**: EC2 instances use IAM roles instead of access keys
2. **Enable Encryption**: RDS and S3 encryption enabled by default
3. **Security Groups**: Minimal required ports open
4. **Private Subnets**: Database in private subnet, not publicly accessible
5. **HTTPS**: Use SSL/TLS for all connections

---

## Cost Estimation

For a small deployment (development/staging):

| Service | Instance/Size | Monthly Cost |
|---------|---------------|--------------|
| EC2 | t3.micro | ~$8 |
| RDS | db.t3.micro | ~$15 |
| S3 | 10GB storage | ~$0.23 |
| NAT Gateway | (if needed) | ~$32 |
| Elastic IP | 1 | ~$3.60 |
| Cognito | 50k MAU free | $0 |
| **Total** | | **~$59/month** |

---

## Next Steps

1. [Configure Cognito](cognito-setup.md)
2. [Set up RDS PostgreSQL](rds-setup.md)
3. [Configure S3 Storage](s3-setup.md)
4. Deploy the backend application

---

## Troubleshooting

### Stack Creation Failed

```bash
# Check stack events for errors
aws cloudformation describe-stack-events --stack-name aura-voice-chat-production
```

### Cannot Connect to RDS

1. Verify security group allows connection from EC2
2. Check that EC2 and RDS are in the same VPC
3. Verify database credentials

### EC2 Instance Not Accessible

1. Check security group rules
2. Verify Elastic IP is associated
3. Confirm instance is in public subnet

---

## Related Documentation

- [Cognito Setup](cognito-setup.md)
- [RDS Setup](rds-setup.md)
- [S3 Setup](s3-setup.md)
- [Deployment Guide](../deployment.md)
