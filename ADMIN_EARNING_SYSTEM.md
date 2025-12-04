# Admin Hierarchy & Earning System Documentation

**Developer:** Hawkaye Visions LTD — Pakistan  
**Date:** December 4, 2025  
**Version:** 1.0

---

## Table of Contents
1. [Admin Hierarchy System](#admin-hierarchy-system)
2. [Customer Support Room](#customer-support-room)
3. [Guide System](#guide-system)
4. [Earning System](#earning-system)
5. [Database Schema](#database-schema)
6. [API Endpoints](#api-endpoints)
7. [Implementation Details](#implementation-details)

---

## Admin Hierarchy System

### Overview
Multi-tier admin hierarchy with clear reporting structure and permissions.

### Hierarchy Levels

#### Level 1: Owner 👑
- **User ID:** `owner_admin_001`
- **Email:** Hamziii886@gmail.com
- **Password:** MnIHbK123xD
- **Reports to:** None
- **Permissions:**
  - ✅ Full control over everything
  - ✅ Manage all admins (assign/remove)
  - ✅ Approve all cashout requests
  - ✅ Edit earning targets
  - ✅ Ban/kick any user
  - ✅ Delete any message/room
  - ✅ Issue diamonds and coins
  - ✅ Manage events and medals
  - ✅ Access all analytics
  - ✅ View sensitive data
  - ✅ Modify VIP tiers
  - ✅ Bypass all restrictions

#### Level 2: Country Admin 🌍
- **One per country**
- **Reports to:** Owner
- **Permissions:**
  - ✅ Ban users in their country
  - ✅ Kick users in their country
  - ✅ Delete messages
  - ✅ View reports
  - ✅ Manage small admins in their country
  - ✅ Manage guides in their country
  - ⚠️ Country-restricted permissions

**Example Countries:**
- United States
- United Kingdom
- Pakistan
- India
- Canada
- Australia
- etc.

#### Level 3-5: Small Admins ⭐✨💫
Three hierarchical levels reporting to Country Admin:

**Admin Level 1** (Senior) ⭐
- Kick users
- Delete messages
- View detailed reports
- Limited moderation

**Admin Level 2** (Mid-level) ✨
- Kick users
- Delete messages
- View basic reports

**Admin Level 3** (Junior) 💫
- Kick users
- Delete messages
- View limited reports

#### Level 6: Customer Support 🎧
- **Reports to:** Small Admins/Country Admin
- **Permissions:**
  - ✅ View support tickets
  - ✅ Respond to users
  - ✅ Escalate issues
  - ✅ View user profiles
  - ✅ Limited moderation (warnings only)

### Reporting Structure
```
Owner (👑)
├── Country Admin USA (🌍)
│   ├── Admin Level 1 (⭐)
│   │   ├── Admin Level 2 (✨)
│   │   │   └── Admin Level 3 (💫)
│   │   │       └── Customer Support (🎧)
│   │   └── Admin Level 2 (✨)
│   └── Admin Level 1 (⭐)
├── Country Admin UK (🌍)
│   ├── Admin Level 1 (⭐)
│   └── Customer Support (🎧)
└── Country Admin Pakistan (🌍)
    └── Admin Level 1 (⭐)
```

---

## Customer Support Room

### Configuration
**Hardcoded in:** `OwnerConfig.CustomerSupportRoom`

```kotlin
Room ID: customer_support_001
Room Name: "Aura Support Center 🎧"
Description: "Get help from our 24/7 support team"
Cover Image: https://auravoicechat.com/assets/support_banner.png
Permanent: true
Official: true
Open 24/7: true
```

### Features
- ✅ Always visible to all users
- ✅ Cannot be deleted
- ✅ Cannot be modified by non-owners
- ✅ Support staff always have access
- ✅ Users can ask for help anytime
- ✅ Aura logo and official badge
- ✅ Priority support queue

### Support Staff
Designated users with `CUSTOMER_SUPPORT` admin level can:
- Access the support room
- Respond to user queries
- Escalate complex issues
- View user profiles for context
- Provide guided help

---

## Guide System

### Overview
**Girls Only** - Female users can apply to become guides and earn through helping users.

### Application Process

#### Step 1: Eligibility Check
- ✅ Female gender (verified)
- ✅ Age 18+ years
- ✅ Active account
- ✅ Good standing (no bans)

#### Step 2: Application Form
Located in **Me Section** → "Become a Guide"

**Required Information:**
1. **Languages** (comma separated)
   - e.g., "English, Spanish, French"
   
2. **Previous Experience** (100-500 words)
   - Customer service experience
   - Community management
   - Tutoring/mentoring
   - Any relevant background
   
3. **Motivation** (150-500 words)
   - Why become a guide?
   - What can you offer users?
   - How will you help the community?
   
4. **Available Hours per Week**
   - Minimum: 10 hours
   - Recommended: 15-20 hours

#### Step 3: Review Process
1. Application submitted
2. Status: **PENDING** (24-48 hour review)
3. Admin reviews application
4. Decision: **APPROVED** or **REJECTED**
5. Notification sent to applicant

#### Step 4: Activation (If Approved)
- Guide badge activated 👑
- Access to guide dashboard
- Special guide targets assigned
- Guide training materials provided

### Guide Benefits

#### Financial Benefits
- 💎 **Diamond rewards** per guide session
- 🎯 **Special guide targets** (higher rewards)
- 💰 **Bonus earnings** for exceeding targets
- 📊 **Priority in earnings queue**

#### Recognition Benefits
- 👑 **Special guide badge** (visible to all)
- ⭐ **Higher profile ranking**
- 🏆 **Guide leaderboard** placement
- 📱 **Featured guide** status

#### Support Benefits
- 🎧 **Priority customer support**
- 📚 **Exclusive training materials**
- 👥 **Guide community access**
- 🎁 **Monthly guide bonuses**

### Guide Targets
Higher earning potential than regular users:

| Tier | Name | Required Diamonds | Cash Reward | Bonus Coins |
|------|------|-------------------|-------------|-------------|
| 1 | Guide Bronze | 50,000 💎 | $15 | 5,000 🪙 |
| 2 | Guide Silver | 100,000 💎 | $30 | 10,000 🪙 |
| 3 | Guide Gold | 200,000 💎 | $60 | 20,000 🪙 |
| 4 | Guide Platinum | 500,000 💎 | $150 | 50,000 🪙 |
| 5 | Guide Diamond | 1,000,000 💎 | $300 | 100,000 🪙 |

---

## Earning System

### System Overview
**Target-Based ONLY** - No streaks, no other earning methods.

### Core Principle
```
Receive Diamonds → Complete Targets → Withdraw Cash
```

### What Counts
✅ **Diamonds RECEIVED from other users**
- Gifts received
- Calls received
- Guide sessions (for guides)
- Room performances
- Any incoming diamond transactions

### What Does NOT Count
❌ Diamonds sent to others
❌ Streak bonuses
❌ Daily login rewards (coins only, not cash)
❌ Event participation (unless diamonds received)
❌ Referral bonuses (coins only, not cash)

### Regular User Targets

| Tier | Name | Required Diamonds | Cash Reward | Bonus Coins | Duration |
|------|------|-------------------|-------------|-------------|----------|
| 1 | Bronze Target | 30,000 💎 | $10 | 3,000 🪙 | Monthly |
| 2 | Silver Target | 70,000 💎 | $20 | 7,000 🪙 | Monthly |
| 3 | Gold Target | 150,000 💎 | $45 | 15,000 🪙 | Monthly |
| 4 | Platinum Target | 300,000 💎 | $90 | 30,000 🪙 | Monthly |
| 5 | Diamond Target | 700,000 💎 | $210 | 70,000 🪙 | Monthly |

### Cashout Process

#### Step 1: Achieve Target
- Receive required diamonds
- Target completion tracked automatically
- Notification when eligible for cashout

#### Step 2: Request Cashout
Navigate to: **Wallet** → "Earning Targets" → "Request Cashout"

**Required Information:**
- Target tier completed
- Payment method
- Payment details (account info)
- Amount to withdraw

#### Step 3: Clearance Period (5 Days)
- Status: **PENDING_CLEARANCE**
- Fraud prevention check
- Account verification
- Cannot be cancelled during this period

#### Step 4: Owner Approval
- After 5 days: Status becomes **PENDING_APPROVAL**
- Owner reviews request
- Decision: **APPROVED** or **REJECTED**
- Rejection includes reason

#### Step 5: Payment Processing
- Status: **APPROVED** → **PROCESSING**
- Payment sent to user's account
- 1-3 business days transfer
- Status: **COMPLETED**
- User receives confirmation

### Payment Information

#### Conversion Rate
```
100,000 Diamonds = $30 USD
```

**Examples:**
- 50,000 💎 = $15
- 200,000 💎 = $60
- 500,000 💎 = $150
- 1,000,000 💎 = $300

#### Minimum Cashout
```
$10.00 USD
```

#### Payment Methods
- 💳 Bank Transfer
- 💰 PayPal
- 🌐 Wise (TransferWise)
- 📱 Mobile Money (selected countries)
- 💵 Western Union (selected countries)

#### Clearance Period
```
5 Days (mandatory)
```

#### Approval Required
```
Owner approval (Hamziii886@gmail.com)
```

### Alternative: Diamond to Coin Exchange
Users can convert diamonds to coins anytime WITHOUT cashout:

```
100,000 Diamonds → 30,000 Coins (30% rate)
```

**Benefits:**
- Instant conversion
- No waiting period
- No approval needed
- Use coins in-app

**Use Cases:**
- Buy gifts
- Purchase VIP
- Unlock features
- Store items

### Target Reset Schedule
```
Monthly (1st of each month at 00:00 UTC)
```

**What Resets:**
- Diamond count for targets
- Target tier progress
- Bonus eligibility

**What Persists:**
- Total lifetime diamonds
- Historical cashouts
- User ranking
- Guide status

---

## Database Schema

### New Tables (Version 3)

#### admins
```sql
CREATE TABLE admins (
    userId TEXT PRIMARY KEY,
    email TEXT NOT NULL,
    name TEXT NOT NULL,
    adminLevel TEXT NOT NULL, -- OWNER, COUNTRY_ADMIN, etc.
    country TEXT,
    reportsTo TEXT,
    isActive INTEGER NOT NULL DEFAULT 1,
    assignedAt INTEGER NOT NULL,
    cachedAt INTEGER NOT NULL
);

CREATE INDEX idx_admins_level ON admins(adminLevel);
CREATE INDEX idx_admins_country ON admins(country);
```

#### guide_applications
```sql
CREATE TABLE guide_applications (
    applicationId TEXT PRIMARY KEY,
    userId TEXT NOT NULL,
    status TEXT NOT NULL, -- PENDING, APPROVED, REJECTED, SUSPENDED
    appliedAt INTEGER NOT NULL,
    reviewedAt INTEGER,
    rejectionReason TEXT,
    cachedAt INTEGER NOT NULL
);

CREATE INDEX idx_guide_userId ON guide_applications(userId);
CREATE INDEX idx_guide_status ON guide_applications(status);
```

#### earning_targets
```sql
CREATE TABLE earning_targets (
    targetId TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    requiredDiamonds INTEGER NOT NULL,
    cashReward REAL NOT NULL,
    isActive INTEGER NOT NULL DEFAULT 1,
    validFrom INTEGER NOT NULL,
    validUntil INTEGER NOT NULL,
    cachedAt INTEGER NOT NULL
);

CREATE INDEX idx_targets_active ON earning_targets(isActive);
```

#### cashout_requests
```sql
CREATE TABLE cashout_requests (
    requestId TEXT PRIMARY KEY,
    userId TEXT NOT NULL,
    amount REAL NOT NULL,
    diamonds INTEGER NOT NULL,
    status TEXT NOT NULL, -- PENDING_CLEARANCE, PENDING_APPROVAL, etc.
    requestedAt INTEGER NOT NULL,
    clearanceDate INTEGER NOT NULL,
    approvedBy TEXT,
    approvedAt INTEGER,
    rejectionReason TEXT,
    paymentMethod TEXT NOT NULL,
    cachedAt INTEGER NOT NULL
);

CREATE INDEX idx_cashout_userId ON cashout_requests(userId);
CREATE INDEX idx_cashout_status ON cashout_requests(status);
```

---

## API Endpoints

### Admin System (10 endpoints)
```
GET    /api/v1/admin/all
GET    /api/v1/admin/level/{level}
GET    /api/v1/admin/country/{country}
POST   /api/v1/admin/assign
POST   /api/v1/admin/{userId}/remove
```

### Guide System (6 endpoints)
```
POST   /api/v1/guide/apply
GET    /api/v1/guide/application/my
GET    /api/v1/guide/applications
POST   /api/v1/guide/applications/{applicationId}/review
GET    /api/v1/guide/profile/{userId}
GET    /api/v1/guide/profile/my
```

### Earning System (7 endpoints)
```
GET    /api/v1/earning/targets
GET    /api/v1/earning/status
POST   /api/v1/earning/cashout/request
GET    /api/v1/earning/cashout/my
GET    /api/v1/earning/cashout/pending
POST   /api/v1/earning/cashout/{requestId}/review
```

### Customer Support (1 endpoint)
```
GET    /api/v1/support/room
```

**Total New Endpoints:** 24

---

## Implementation Details

### Files Created

#### Configuration
- `OwnerConfig.kt` - Admin hierarchy config, customer support room

#### Data Models
- `AdminModels.kt` - All admin/guide/earning data models

#### Database
- `Entities.kt` - 4 new entities added
- `Daos.kt` - 4 new DAOs with 25+ queries
- `AppDatabase.kt` - Version upgraded to 3

#### UI Screens
- `GuideApplicationScreen.kt` - Guide application form
- `EarningTargetSheetScreen.kt` - Target display for users/guides

#### ViewModels
- `GuideApplicationViewModel.kt` - Guide application logic
- `EarningTargetViewModel.kt` - Earning target logic

#### API
- `ApiService.kt` - 24 new endpoints added

#### Navigation
- `Navigation.kt` - Routes for guide application and earning targets

### Navigation Routes Added
```
guide/apply              - Guide application screen
earning/targets          - Regular user earning targets
earning/targets/guide    - Guide earning targets
```

### Access Points

#### For Users:
- **Me Section** → "Earning Targets" → View targets
- **Me Section** → "Become a Guide" → Apply (girls only)
- **Wallet** → "Earning Status" → View progress
- **Wallet** → "Request Cashout" → Submit request

#### For Guides:
- **Me Section** → "Guide Dashboard"
- **Me Section** → "Guide Targets"
- **Wallet** → "Guide Earnings"

#### For Admins:
- **Admin Panel** → "Manage Admins"
- **Admin Panel** → "Review Applications"
- **Admin Panel** → "Approve Cashouts"

#### For Owner:
- **Owner Panel** → Full control over everything

---

## Security Considerations

### Owner Credentials
```kotlin
Email: Hamziii886@gmail.com (hardcoded)
Password: MnIHbK123xD (hardcoded)
User ID: owner_admin_001 (hardcoded)
```

⚠️ **Important:** These are hardcoded in `OwnerConfig.kt` and should be changed on first deployment.

### Admin Verification
- All admin actions logged
- Country admins restricted to their country
- Small admins report to country admins
- Support staff have limited permissions

### Cashout Security
- 5-day mandatory clearance
- Owner approval required
- Payment details verified
- Fraud detection on unusual patterns
- Cannot cancel during clearance

### Guide Verification
- Gender verification required
- Age verification (18+)
- Application review by admins
- Can be suspended for violations
- Regular performance monitoring

---

## Future Enhancements

### Planned Features
1. **Admin Panel UI** - Dedicated admin dashboard
2. **Cashout Approval Screen** - Owner review interface
3. **Guide Dashboard** - Guide performance tracking
4. **Analytics Dashboard** - Earning statistics
5. **Automated Fraud Detection** - AI-powered checks
6. **Multi-currency Support** - Support more payment methods
7. **Guide Training System** - Online training for guides
8. **Performance Metrics** - Detailed guide analytics

---

**Document Version:** 1.0  
**Last Updated:** December 4, 2025  
**Maintained By:** Hawkaye Visions LTD — Pakistan
