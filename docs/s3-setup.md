# AWS S3 Setup Guide

**Developer:** Hawkaye Visions LTD — Lahore, Pakistan

This guide covers configuring AWS S3 for file storage in Aura Voice Chat.

---

## Overview

S3 is used for storing:
- User avatars
- Room cover images
- KYC documents
- Chat attachments
- Gift animations
- Store item assets

---

## Quick Start (CloudFormation)

If you used the CloudFormation template, S3 is already configured. Get the bucket name:

```bash
aws cloudformation describe-stacks \
    --stack-name aura-voice-chat-production \
    --query "Stacks[0].Outputs[?OutputKey=='S3BucketName'].OutputValue" \
    --output text
```

---

## Manual Setup

### 1. Create Bucket

```bash
# For us-east-1
aws s3api create-bucket \
    --bucket aura-voice-chat-files-$(aws sts get-caller-identity --query Account --output text)

# For other regions
aws s3api create-bucket \
    --bucket aura-voice-chat-files-$(aws sts get-caller-identity --query Account --output text) \
    --create-bucket-configuration LocationConstraint=us-west-2
```

### 2. Enable Encryption

```bash
aws s3api put-bucket-encryption \
    --bucket YOUR_BUCKET_NAME \
    --server-side-encryption-configuration '{
        "Rules": [{
            "ApplyServerSideEncryptionByDefault": {
                "SSEAlgorithm": "AES256"
            }
        }]
    }'
```

### 3. Block Public Access

```bash
aws s3api put-public-access-block \
    --bucket YOUR_BUCKET_NAME \
    --public-access-block-configuration '{
        "BlockPublicAcls": true,
        "IgnorePublicAcls": true,
        "BlockPublicPolicy": true,
        "RestrictPublicBuckets": true
    }'
```

### 4. Configure CORS

```bash
aws s3api put-bucket-cors \
    --bucket YOUR_BUCKET_NAME \
    --cors-configuration '{
        "CORSRules": [{
            "AllowedHeaders": ["*"],
            "AllowedMethods": ["GET", "PUT", "POST", "DELETE", "HEAD"],
            "AllowedOrigins": ["*"],
            "ExposeHeaders": ["ETag"],
            "MaxAgeSeconds": 3600
        }]
    }'
```

---

## Folder Structure

```
bucket/
├── avatars/
│   └── {userId}/
│       └── {uuid}-{timestamp}.{ext}
├── rooms/
│   └── {roomId}/
│       └── cover/
│           └── {uuid}-{timestamp}.{ext}
├── kyc/
│   └── {userId}/
│       ├── id-front/
│       ├── id-back/
│       └── selfie/
├── chat/
│   └── {roomId}/
│       └── {messageId}/
│           └── {uuid}-{timestamp}.{ext}
├── gifts/
│   └── {giftId}/
│       └── animation.json
├── store/
│   └── {itemId}/
│       └── thumbnail.{ext}
└── public/
    └── assets/
```

---

## Upload Methods

### 1. Presigned URL (Recommended for Client Uploads)

Backend generates a presigned URL for the client to upload directly to S3:

```typescript
// Backend
import { S3Client, PutObjectCommand } from '@aws-sdk/client-s3';
import { getSignedUrl } from '@aws-sdk/s3-request-presigner';

const s3Client = new S3Client({ region: 'us-east-1' });

const command = new PutObjectCommand({
  Bucket: 'YOUR_BUCKET_NAME',
  Key: `avatars/${userId}/${uuid}.jpg`,
  ContentType: 'image/jpeg',
});

const presignedUrl = await getSignedUrl(s3Client, command, {
  expiresIn: 3600, // 1 hour
});

// Return presignedUrl to client
```

```kotlin
// Android client uploads to presigned URL
val url = URL(presignedUrl)
val connection = url.openConnection() as HttpURLConnection
connection.requestMethod = "PUT"
connection.setRequestProperty("Content-Type", "image/jpeg")
connection.doOutput = true
connection.outputStream.use { imageData.copyTo(it) }
```

### 2. Direct Server Upload

For server-side uploads (e.g., processing images):

```typescript
import { S3Client, PutObjectCommand } from '@aws-sdk/client-s3';

const s3Client = new S3Client({ region: 'us-east-1' });

await s3Client.send(new PutObjectCommand({
  Bucket: 'YOUR_BUCKET_NAME',
  Key: `avatars/${userId}/${uuid}.jpg`,
  Body: fileBuffer,
  ContentType: 'image/jpeg',
}));
```

### 3. AWS Amplify (Android)

```kotlin
Amplify.Storage.uploadFile(
    "avatars/${userId}/${uuid}.jpg",
    localFile,
    StorageUploadFileOptions.defaultInstance(),
    { result -> /* Handle success */ },
    { error -> /* Handle error */ }
)
```

---

## Download/Access

### Public Assets

For public assets (gift animations, store items), use CloudFront or direct S3 URLs:

```
https://YOUR_BUCKET.s3.us-east-1.amazonaws.com/public/assets/logo.png
```

### Private Assets

Use presigned download URLs:

```typescript
import { GetObjectCommand } from '@aws-sdk/client-s3';
import { getSignedUrl } from '@aws-sdk/s3-request-presigner';

const command = new GetObjectCommand({
  Bucket: 'YOUR_BUCKET_NAME',
  Key: `kyc/${userId}/id-front/document.jpg`,
});

const downloadUrl = await getSignedUrl(s3Client, command, {
  expiresIn: 3600,
});
```

---

## Lifecycle Rules

Configure lifecycle rules to manage storage costs:

```bash
aws s3api put-bucket-lifecycle-configuration \
    --bucket YOUR_BUCKET_NAME \
    --lifecycle-configuration '{
        "Rules": [
            {
                "ID": "DeleteOldVersions",
                "Status": "Enabled",
                "NoncurrentVersionExpiration": {
                    "NoncurrentDays": 30
                }
            },
            {
                "ID": "TransitionToIA",
                "Status": "Enabled",
                "Filter": {
                    "Prefix": "kyc/"
                },
                "Transitions": [
                    {
                        "Days": 90,
                        "StorageClass": "STANDARD_IA"
                    }
                ]
            }
        ]
    }'
```

---

## Security

### Bucket Policy

```json
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "AllowPublicRead",
            "Effect": "Allow",
            "Principal": "*",
            "Action": "s3:GetObject",
            "Resource": "arn:aws:s3:::BUCKET_NAME/public/*"
        },
        {
            "Sid": "DenyUnencryptedConnections",
            "Effect": "Deny",
            "Principal": "*",
            "Action": "s3:*",
            "Resource": [
                "arn:aws:s3:::BUCKET_NAME",
                "arn:aws:s3:::BUCKET_NAME/*"
            ],
            "Condition": {
                "Bool": {
                    "aws:SecureTransport": "false"
                }
            }
        }
    ]
}
```

### IAM Policy for EC2

```json
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "s3:GetObject",
                "s3:PutObject",
                "s3:DeleteObject",
                "s3:ListBucket"
            ],
            "Resource": [
                "arn:aws:s3:::BUCKET_NAME",
                "arn:aws:s3:::BUCKET_NAME/*"
            ]
        }
    ]
}
```

---

## File Validation

### Allowed File Types

| Category | Allowed Types | Max Size |
|----------|--------------|----------|
| Images | JPEG, PNG, GIF, WebP | 5 MB |
| Audio | MP3, WAV, WebM | 10 MB |
| Video | MP4, WebM | 50 MB |
| Documents (KYC) | JPEG, PNG | 10 MB |

### Backend Validation

```typescript
const allowedMimeTypes = [
  'image/jpeg',
  'image/png',
  'image/gif',
  'image/webp',
  'audio/mpeg',
  'audio/wav',
  'video/mp4',
  'video/webm',
];

const maxFileSize = 10 * 1024 * 1024; // 10 MB

function validateFile(contentType: string, size: number): boolean {
  return allowedMimeTypes.includes(contentType) && size <= maxFileSize;
}
```

---

## CloudFront (Optional)

For better performance, use CloudFront CDN:

```bash
aws cloudfront create-distribution \
    --origin-domain-name YOUR_BUCKET.s3.us-east-1.amazonaws.com \
    --default-root-object index.html
```

Benefits:
- Global edge caching
- HTTPS by default
- Lower latency
- DDoS protection

---

## Troubleshooting

### Access Denied

1. Check IAM permissions
2. Verify bucket policy
3. Check CORS configuration

### Slow Uploads

1. Use multipart upload for large files
2. Upload from same region when possible
3. Use Transfer Acceleration

### CORS Errors

1. Verify CORS configuration
2. Check allowed origins
3. Ensure correct headers in request

---

## Related Documentation

- [AWS Setup](aws-setup.md)
- [Cognito Setup](cognito-setup.md)
- [Backend API](../api.md)
