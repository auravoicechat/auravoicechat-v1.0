# Owner Panel

Complete documentation for the Owner Panel in Aura Voice Chat. The Owner Panel provides full control over all app operations, economy, and business management.

## Overview

The Owner Panel is the highest-level administrative interface with complete control over the app including financial management, economy settings, feature configuration, and staff management.

---

## Access Control

### Owner Credentials

- Separate login portal
- Hardware security key required
- Biometric verification
- IP whitelist enforced
- All actions logged with video capture

### Owner Roles

| Role | Description |
|------|-------------|
| Super Owner | Full unrestricted access |
| Co-Owner | All access except owner management |
| Finance Owner | Financial and economy access only |

---

## Dashboard

### Executive Dashboard

| Widget | Information |
|--------|-------------|
| Revenue Today | Real-time purchase totals |
| Revenue MTD | Month-to-date revenue |
| Active Users | Current online count |
| Growth Rate | User growth percentage |
| Payout Queue | Pending withdrawals |
| System Health | Server status |

### Financial Overview

| Metric | Description |
|--------|-------------|
| Gross Revenue | Total purchases |
| Net Revenue | After refunds/chargebacks |
| Coin Economy | Coins in circulation |
| Payout Obligations | Pending earnings/withdrawals |
| Profit Margin | Revenue minus payouts |

---

## Economy Management

### Coin Economy Controls

| Setting | Description | Range |
|---------|-------------|-------|
| Coin Purchase Rate | Coins per $1 | 80,000 - 120,000 |
| Diamond Conversion | Diamond to coin rate | 25% - 35% |
| Daily Reward Base | Day 1 reward amount | 3,000 - 10,000 |
| VIP Multiplier Cap | Maximum VIP bonus | 2.0x - 4.0x |
| Gift Tax Rate | Platform fee on gifts | 0% - 15% |

### Economy Balance Tools

| Tool | Purpose |
|------|---------|
| Inflation Monitor | Track coin supply growth |
| Sink Analysis | Where coins leave circulation |
| Faucet Analysis | Where coins enter circulation |
| Velocity Tracker | How fast coins move |
| Whale Monitor | Top holder concentration |

### Economy Presets

| Preset | Coin Rate | VIP Cap | Gift Tax |
|--------|-----------|---------|----------|
| Conservative | 80,000/$ | 2.0x | 10% |
| Balanced | 100,000/$ | 3.0x | 5% |
| Generous | 120,000/$ | 4.0x | 0% |

---

## Revenue Management

### Purchase Settings

| Product | Base Price | Coins Given | Bonus Range |
|---------|------------|-------------|-------------|
| Starter Pack | $0.99 | 80,000 | 0-20% |
| Basic Pack | $4.99 | 450,000 | 0-25% |
| Standard Pack | $9.99 | 950,000 | 0-30% |
| Premium Pack | $24.99 | 2,600,000 | 0-35% |
| Elite Pack | $49.99 | 5,500,000 | 0-40% |
| Ultimate Pack | $99.99 | 12,000,000 | 0-50% |

### First Recharge Bonus

| Setting | Value |
|---------|-------|
| Bonus Percentage | 100% (double coins) |
| One-time only | Yes |
| Display prominence | High |

### Recharge Events

Configure ongoing recharge promotions:
- Daily Surge bonuses
- Aurora Milestones
- Flash sales
- Seasonal bonuses

---

## Payout Management

### Earning System Controls

| Setting | Description | Range |
|---------|-------------|-------|
| Earning Rate | $ earned per coins sent | 0.3% - 0.7% |
| Clearance Period | Days before withdrawal | 3 - 14 days |
| Min Withdrawal | Minimum payout amount | $1 - $10 |
| Max Daily Payout | Per-user daily limit | $50 - $500 |
| Platform Fee | Withdrawal fee | 0% - 5% |

### Payout Queue

| Column | Description |
|--------|-------------|
| User | Requester details |
| Amount | Withdrawal amount |
| Method | Payment destination |
| Requested | Request timestamp |
| Status | Pending/Processing/Complete/Failed |
| Actions | Approve/Reject/Hold |

### Batch Payout Actions

- Approve all pending (filtered)
- Export payout list
- Bulk bank transfer
- Manual verification queue

### Payout Holds

Automatically hold payouts when:
- New user (first withdrawal)
- Large amount (configurable threshold)
- Suspicious activity flagged
- KYC incomplete
- Region restrictions

---

## Guide Management

### Guide Program Settings

| Setting | Description |
|---------|-------------|
| Enable Program | On/Off |
| Min Level | Level required to apply |
| Monthly Targets | Number of targets |
| Max Monthly Earning | Cap per guide |
| Conversion Bonus | USD to coin bonus |

### Guide Oversight

- View all active guides
- Performance metrics per guide
- Earnings history
- Approval/revocation controls

---

## Reseller Management

### Reseller Program Settings

| Setting | Description |
|---------|-------------|
| Enable Program | On/Off |
| Registration Fee | Offline payment required |
| Tier Structure | Bronze/Silver/Gold/Platinum/Diamond |
| Wholesale Rates | Discount per tier |
| Volume Bonuses | Additional incentives |

### Reseller Oversight

- Active reseller list
- Transaction volumes
- Customer counts
- Profit margins
- Ban/suspend controls

---

## Feature Toggles

### Global Features

| Feature | Status | Description |
|---------|--------|-------------|
| Games | On/Off | All games enabled |
| Lucky 777 | On/Off | Slot machine game |
| Greedy Baby | On/Off | Wheel game |
| CP System | On/Off | Couple partnerships |
| Family System | On/Off | Family feature |
| Earning System | On/Off | User earnings |
| Guide System | On/Off | Guide program |
| Reseller System | On/Off | Reseller program |

### Feature Flags

| Flag | Description |
|------|-------------|
| NEW_USER_BONUS | First-time rewards |
| REFERRAL_PROGRAM | Invite rewards |
| DAILY_REWARDS | Login bonuses |
| VIP_SYSTEM | VIP features |
| EVENTS | Event system |

---

## Staff Management

### Admin Management

| Action | Description |
|--------|-------------|
| Create Admin | Add new admin account |
| Edit Permissions | Modify access level |
| Suspend Admin | Temporarily disable |
| Remove Admin | Delete admin account |
| View Logs | See admin activity |

### Admin Roles Configuration

Define custom permission sets:
- User management
- Content moderation
- Financial access
- Event management
- System settings

---

## System Configuration

### App Settings

| Setting | Description |
|---------|-------------|
| App Name | Display name |
| App Version | Minimum required version |
| Maintenance Mode | Disable app access |
| Force Update | Require app update |
| Server Region | Primary server location |

### Rate Limits

| Limit | Value |
|-------|-------|
| Messages/minute | 30 |
| Gifts/minute | 20 |
| Room joins/hour | 50 |
| Friend requests/day | 100 |
| API calls/minute | 100 |

### Security Settings

| Setting | Description |
|---------|-------------|
| Max Devices | Accounts per device |
| Session Timeout | Auto-logout time |
| OTP Expiry | Verification code life |
| Ban Appeal Period | Days to appeal |

---

## Analytics & Reports

### Revenue Reports

| Report | Frequency |
|--------|-----------|
| Daily Revenue | Daily |
| Weekly Summary | Weekly |
| Monthly P&L | Monthly |
| Quarterly Review | Quarterly |
| Annual Report | Yearly |

### Export Options

- CSV export
- PDF reports
- API access
- Scheduled emails

---

## Versioning & Rollback

### Configuration Versions

All settings changes are versioned:
- Automatic snapshots
- Manual save points
- Compare versions
- Rollback to any version

### Rollback Process

1. Select configuration area
2. View version history
3. Compare with current
4. Preview changes
5. Confirm rollback
6. Monitor effects

---

## Emergency Controls

### Kill Switches

| Switch | Effect |
|--------|--------|
| Disable Purchases | Stop all payments |
| Disable Withdrawals | Halt all payouts |
| Disable Gifts | Stop coin transfers |
| Disable Games | Turn off all games |
| Maintenance Mode | Full app lockout |

### Emergency Contacts

- Payment processor hotline
- Server provider support
- Legal counsel
- Security team

---

## Audit & Compliance

### Audit Trail

All owner actions logged:
- Timestamp
- Action type
- Before/after values
- IP address
- Session ID

### Compliance Tools

- Data export (GDPR)
- User deletion
- Transaction records
- Tax documentation

---

## API Endpoints

```
# Economy
GET /owner/economy/stats
PUT /owner/economy/settings
GET /owner/economy/presets
POST /owner/economy/presets/apply

# Revenue
GET /owner/revenue/dashboard
GET /owner/revenue/reports
PUT /owner/revenue/pricing

# Payouts
GET /owner/payouts/queue
POST /owner/payouts/{id}/approve
POST /owner/payouts/{id}/reject
POST /owner/payouts/batch

# Features
GET /owner/features
PUT /owner/features/{feature}
GET /owner/feature-flags
PUT /owner/feature-flags/{flag}

# Staff
GET /owner/admins
POST /owner/admins
PUT /owner/admins/{id}
DELETE /owner/admins/{id}
GET /owner/admins/{id}/logs

# System
GET /owner/system/settings
PUT /owner/system/settings
POST /owner/system/maintenance

# Versioning
GET /owner/versions/{area}
POST /owner/versions/{area}/rollback
```

---

## Related Documentation

- [Admin Panel](./admin-panel.md)
- [Owner CMS](./owner-cms.md)
- [Reseller Panel](./reseller-panel.md)
- [Economy Balance](./economy-balance.md)
