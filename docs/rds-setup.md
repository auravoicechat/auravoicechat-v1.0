# AWS RDS PostgreSQL Setup Guide

**Developer:** Hawkaye Visions LTD — Lahore, Pakistan

This guide covers setting up and managing the PostgreSQL database on AWS RDS.

---

## Quick Start (CloudFormation)

If you used the CloudFormation template, RDS is already configured. Get the endpoint:

```bash
aws cloudformation describe-stacks \
    --stack-name aura-voice-chat-production \
    --query "Stacks[0].Outputs[?OutputKey=='RDSEndpoint'].OutputValue" \
    --output text
```

---

## Manual Setup

### 1. Create DB Subnet Group

```bash
aws rds create-db-subnet-group \
    --db-subnet-group-name aura-voice-chat-db-subnet \
    --db-subnet-group-description "Subnet group for Aura Voice Chat RDS" \
    --subnet-ids subnet-xxxxx subnet-yyyyy
```

### 2. Create RDS Instance

```bash
aws rds create-db-instance \
    --db-instance-identifier aura-voice-chat-postgres \
    --db-instance-class db.t3.micro \
    --engine postgres \
    --engine-version 15.4 \
    --allocated-storage 20 \
    --storage-type gp3 \
    --db-name auravoicechat \
    --master-username aura_admin \
    --master-user-password YOUR_SECURE_PASSWORD \
    --db-subnet-group-name aura-voice-chat-db-subnet \
    --vpc-security-group-ids sg-xxxxx \
    --no-publicly-accessible \
    --backup-retention-period 7 \
    --storage-encrypted
```

### 3. Wait for Instance

```bash
aws rds wait db-instance-available \
    --db-instance-identifier aura-voice-chat-postgres
```

---

## Database Schema

Initialize the database with the provided schema:

```bash
# From EC2 instance
psql -h YOUR_RDS_ENDPOINT -U aura_admin -d auravoicechat -f backend/src/database/schema.sql
```

### Schema Overview

| Table | Description |
|-------|-------------|
| users | User accounts and profiles |
| user_settings | User preferences |
| rooms | Voice/video rooms |
| room_members | Room participants |
| messages | Room chat messages |
| direct_messages | Private messages |
| user_follows | Follow relationships |
| notifications | User notifications |
| daily_rewards | Daily login rewards |
| medals | Achievement medals |
| user_medals | User earned medals |
| gifts | Gift catalog |
| gift_transactions | Gift history |
| wallet_transactions | Financial transactions |
| cp_partnerships | Couple partnerships |
| families | Family groups |
| referrals | Referral tracking |

---

## Connection Configuration

### Environment Variables

```bash
DB_HOST=your-rds-endpoint.region.rds.amazonaws.com
DB_PORT=5432
DB_NAME=auravoicechat
DB_USER=aura_admin
DB_PASSWORD=your_secure_password
DATABASE_URL=postgresql://aura_admin:password@endpoint:5432/auravoicechat
```

### Node.js Connection (pg)

```typescript
import { Pool } from 'pg';

const pool = new Pool({
  host: process.env.DB_HOST,
  port: parseInt(process.env.DB_PORT || '5432'),
  database: process.env.DB_NAME,
  user: process.env.DB_USER,
  password: process.env.DB_PASSWORD,
  ssl: { rejectUnauthorized: false }, // For RDS
  max: 20,
  idleTimeoutMillis: 30000,
});
```

---

## Backup & Recovery

### Automated Backups

RDS automatically takes daily backups with a 7-day retention period (configurable).

### Manual Snapshot

```bash
aws rds create-db-snapshot \
    --db-instance-identifier aura-voice-chat-postgres \
    --db-snapshot-identifier aura-voice-chat-manual-snapshot-$(date +%Y%m%d)
```

### Restore from Snapshot

```bash
aws rds restore-db-instance-from-db-snapshot \
    --db-instance-identifier aura-voice-chat-postgres-restored \
    --db-snapshot-identifier aura-voice-chat-manual-snapshot-20231201
```

---

## Performance Tuning

### Instance Sizing

| Environment | Instance Class | Storage |
|-------------|----------------|---------|
| Development | db.t3.micro | 20 GB |
| Staging | db.t3.small | 50 GB |
| Production | db.t3.medium+ | 100+ GB |

### Parameter Groups

Create a custom parameter group for optimization:

```bash
aws rds create-db-parameter-group \
    --db-parameter-group-name aura-voice-chat-params \
    --db-parameter-group-family postgres15 \
    --description "Optimized parameters for Aura Voice Chat"

# Modify parameters
aws rds modify-db-parameter-group \
    --db-parameter-group-name aura-voice-chat-params \
    --parameters \
        "ParameterName=shared_buffers,ParameterValue={DBInstanceClassMemory/4},ApplyMethod=pending-reboot" \
        "ParameterName=max_connections,ParameterValue=200,ApplyMethod=pending-reboot"
```

### Important Indexes

The schema includes indexes for:
- User lookups (email, phone, username, cognito_sub)
- Room listings (owner_id, is_live, current_participants)
- Message retrieval (room_id, created_at)
- Notification queries (user_id, is_read)

---

## Monitoring

### CloudWatch Metrics

Enable Enhanced Monitoring for detailed metrics:

```bash
aws rds modify-db-instance \
    --db-instance-identifier aura-voice-chat-postgres \
    --monitoring-interval 60 \
    --monitoring-role-arn arn:aws:iam::ACCOUNT:role/rds-monitoring-role
```

### Key Metrics to Monitor

- CPUUtilization
- FreeableMemory
- ReadIOPS / WriteIOPS
- DatabaseConnections
- DiskQueueDepth

---

## Security

### Encryption

- Storage encryption: Enabled (AES-256)
- In-transit encryption: SSL/TLS required
- Connection string requires SSL

### Security Groups

RDS should only be accessible from:
- EC2 instances in the same VPC
- Bastion host (for administration)

### IAM Authentication (Optional)

Enable IAM database authentication:

```bash
aws rds modify-db-instance \
    --db-instance-identifier aura-voice-chat-postgres \
    --enable-iam-database-authentication
```

---

## Maintenance

### Updates

```bash
# Check for updates
aws rds describe-pending-maintenance-actions

# Apply immediately
aws rds apply-pending-maintenance-action \
    --resource-identifier arn:aws:rds:region:account:db:aura-voice-chat-postgres \
    --apply-action system-update \
    --opt-in-type immediate
```

### Scaling

```bash
# Vertical scaling
aws rds modify-db-instance \
    --db-instance-identifier aura-voice-chat-postgres \
    --db-instance-class db.t3.small \
    --apply-immediately
```

---

## Troubleshooting

### Cannot Connect

1. Verify security group allows inbound on port 5432
2. Check that client is in VPC or has VPC access
3. Verify credentials

### Slow Queries

1. Enable Performance Insights
2. Check for missing indexes
3. Analyze query execution plans

### Out of Storage

```bash
# Increase storage
aws rds modify-db-instance \
    --db-instance-identifier aura-voice-chat-postgres \
    --allocated-storage 50 \
    --apply-immediately
```

---

## Related Documentation

- [AWS Setup](aws-setup.md)
- [Cognito Setup](cognito-setup.md)
- [Database Schema](../backend/src/database/schema.sql)
