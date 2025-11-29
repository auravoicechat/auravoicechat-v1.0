# AWS Cognito Setup Guide

**Developer:** Hawkaye Visions LTD — Lahore, Pakistan

This guide covers configuring AWS Cognito for authentication in Aura Voice Chat.

---

## Overview

AWS Cognito provides:
- **User Pool**: User directory with sign-up/sign-in
- **Identity Pool**: AWS credentials for accessing AWS services

---

## Quick Setup (CloudFormation)

If you used the CloudFormation template, Cognito is already configured. Get the IDs:

```bash
# Get User Pool ID
aws cloudformation describe-stacks \
    --stack-name aura-voice-chat-production \
    --query "Stacks[0].Outputs[?OutputKey=='CognitoUserPoolId'].OutputValue" \
    --output text

# Get Client ID
aws cloudformation describe-stacks \
    --stack-name aura-voice-chat-production \
    --query "Stacks[0].Outputs[?OutputKey=='CognitoClientId'].OutputValue" \
    --output text
```

---

## Manual Setup

### 1. Create User Pool

```bash
aws cognito-idp create-user-pool \
    --pool-name "aura-voice-chat-users" \
    --auto-verified-attributes email phone_number \
    --username-attributes email phone_number \
    --policies "PasswordPolicy={MinimumLength=8,RequireUppercase=true,RequireLowercase=true,RequireNumbers=true,RequireSymbols=false}"
```

Save the `Id` from the response.

### 2. Create App Client

```bash
aws cognito-idp create-user-pool-client \
    --user-pool-id YOUR_USER_POOL_ID \
    --client-name "aura-voice-chat-app" \
    --generate-secret \
    --explicit-auth-flows ALLOW_USER_PASSWORD_AUTH ALLOW_REFRESH_TOKEN_AUTH ALLOW_ADMIN_USER_PASSWORD_AUTH
```

Save the `ClientId` and `ClientSecret`.

### 3. Create Identity Pool

```bash
aws cognito-identity create-identity-pool \
    --identity-pool-name "aura_voice_chat_identity" \
    --allow-unauthenticated-identities false \
    --cognito-identity-providers ProviderName="cognito-idp.us-east-1.amazonaws.com/YOUR_USER_POOL_ID",ClientId="YOUR_CLIENT_ID"
```

### 4. Configure Identity Pool Roles

Create IAM roles for authenticated users to access AWS services.

---

## User Pool Settings

### Password Policy

- Minimum length: 8 characters
- Require uppercase: Yes
- Require lowercase: Yes
- Require numbers: Yes
- Require symbols: No

### User Attributes

| Attribute | Type | Required |
|-----------|------|----------|
| email | String | Yes (if using email sign-up) |
| phone_number | String | Yes (if using phone sign-up) |
| name | String | No |
| custom:display_name | String | No |

### MFA Configuration

- MFA: Optional
- SMS MFA: Enabled
- TOTP MFA: Can be enabled

---

## Authentication Flows

### 1. Email/Password Sign Up

```kotlin
// Android (Kotlin)
Amplify.Auth.signUp(
    email,
    password,
    AuthSignUpOptions.builder()
        .userAttribute(AuthUserAttributeKey.email(), email)
        .build(),
    { result -> /* Handle success */ },
    { error -> /* Handle error */ }
)
```

### 2. Phone Number Sign Up

```kotlin
Amplify.Auth.signUp(
    phoneNumber, // Format: +1234567890
    password,
    AuthSignUpOptions.builder()
        .userAttribute(AuthUserAttributeKey.phoneNumber(), phoneNumber)
        .build(),
    { result -> /* Handle success */ },
    { error -> /* Handle error */ }
)
```

### 3. Confirm Sign Up

```kotlin
Amplify.Auth.confirmSignUp(
    username,
    confirmationCode,
    { result -> /* Handle success */ },
    { error -> /* Handle error */ }
)
```

### 4. Sign In

```kotlin
Amplify.Auth.signIn(
    username,
    password,
    { result -> /* Handle success */ },
    { error -> /* Handle error */ }
)
```

### 5. Sign Out

```kotlin
Amplify.Auth.signOut { /* Handle completion */ }
```

### 6. Forgot Password

```kotlin
Amplify.Auth.resetPassword(
    username,
    { result -> /* Handle success */ },
    { error -> /* Handle error */ }
)

Amplify.Auth.confirmResetPassword(
    username,
    newPassword,
    confirmationCode,
    { /* Handle success */ },
    { error -> /* Handle error */ }
)
```

---

## Backend Integration

### Verify Cognito Token (Node.js)

```typescript
import { CognitoJwtVerifier } from 'aws-jwt-verify';

const verifier = CognitoJwtVerifier.create({
  userPoolId: process.env.COGNITO_USER_POOL_ID,
  tokenUse: 'access',
  clientId: process.env.COGNITO_CLIENT_ID,
});

async function verifyToken(token: string) {
  try {
    const payload = await verifier.verify(token);
    return payload;
  } catch {
    return null;
  }
}
```

### Admin Operations

```typescript
import { CognitoIdentityProviderClient, AdminCreateUserCommand } from '@aws-sdk/client-cognito-identity-provider';

const client = new CognitoIdentityProviderClient({ region: 'us-east-1' });

// Create user
await client.send(new AdminCreateUserCommand({
  UserPoolId: 'YOUR_USER_POOL_ID',
  Username: 'user@example.com',
  UserAttributes: [
    { Name: 'email', Value: 'user@example.com' },
    { Name: 'email_verified', Value: 'true' },
  ],
}));
```

---

## Security Considerations

1. **Store Client Secret Securely**: Never expose in client-side code
2. **Use HTTPS**: All Cognito API calls use HTTPS
3. **Token Storage**: Store tokens securely (Keychain/Keystore)
4. **Token Refresh**: Implement automatic token refresh
5. **Rate Limiting**: Cognito has built-in rate limiting

---

## Troubleshooting

### "User does not exist"

- Verify username format (email vs phone)
- Check if user is confirmed

### "Invalid verification code"

- Code expires after a set time
- Request new code if expired

### "Password does not conform to policy"

- Check password requirements
- Must meet all policy criteria

---

## Related Documentation

- [AWS Setup](aws-setup.md)
- [S3 Setup](s3-setup.md)
- [Backend API Documentation](../api.md)
