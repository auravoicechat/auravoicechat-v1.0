# Admin Panel

Complete documentation for the Admin Panel in Aura Voice Chat. Admins are staff members with elevated privileges to manage users, content, and app operations.

## Overview

The Admin Panel provides tools for day-to-day app management including user moderation, content review, support tickets, and basic analytics.

---

## Access Levels

### Admin Roles

| Role | Access Level | Permissions |
|------|--------------|-------------|
| Support Admin | Level 1 | View users, handle tickets, basic moderation |
| Content Admin | Level 2 | Manage gifts, events, announcements |
| Senior Admin | Level 3 | User bans, coin adjustments, advanced moderation |
| Lead Admin | Level 4 | All admin functions, admin management |

---

## Dashboard

### Main Dashboard

- **Active Users:** Real-time online count
- **Daily Stats:** New users, transactions, rooms active
- **Alerts:** Flagged content, pending reviews, urgent tickets
- **Quick Actions:** Common admin tasks

### Key Metrics

| Metric | Description |
|--------|-------------|
| DAU | Daily Active Users |
| New Registrations | Today's signups |
| Active Rooms | Currently open rooms |
| Transactions | Gift/purchase volume |
| Pending Tickets | Support queue |
| Flagged Content | Awaiting review |

---

## User Management

### User Search

Search by:
- User ID
- Username
- Phone number
- Email
- Device ID

### User Profile View

| Section | Information |
|---------|-------------|
| Basic Info | ID, name, level, VIP status, join date |
| Wallet | Coins balance, diamonds, transaction history |
| Activity | Room history, gift history, login logs |
| Social | Friends, followers, CP status, family |
| Flags | Warnings, bans, reports against user |
| Devices | Device history, IP logs |

### User Actions

| Action | Permission Level | Description |
|--------|------------------|-------------|
| View Profile | Level 1 | See user details |
| Send Warning | Level 1 | Issue formal warning |
| Mute User | Level 2 | Restrict chat (1hr-7d) |
| Restrict Gifts | Level 2 | Disable gift sending |
| Temporary Ban | Level 3 | Ban user (1d-30d) |
| Permanent Ban | Level 3 | Permanent account ban |
| Coin Adjustment | Level 3 | Add/remove coins (with reason) |
| Level Adjustment | Level 4 | Modify user level |
| VIP Grant | Level 4 | Grant VIP status |
| Account Restore | Level 4 | Restore banned account |

### Coin Adjustment Limits

| Admin Level | Single Adjustment Limit | Daily Limit |
|-------------|------------------------|-------------|
| Level 3 | 1,000,000 coins | 5,000,000 coins |
| Level 4 | 10,000,000 coins | 50,000,000 coins |

---

## Content Moderation

### Report Queue

| Report Type | Priority | SLA |
|-------------|----------|-----|
| Harassment | High | 1 hour |
| Inappropriate Content | High | 2 hours |
| Scam/Fraud | Critical | 30 minutes |
| Spam | Medium | 4 hours |
| Username Violation | Low | 24 hours |
| Other | Low | 24 hours |

### Moderation Actions

| Action | Effect |
|--------|--------|
| Dismiss | Close report as invalid |
| Warn User | Issue warning, log incident |
| Remove Content | Delete offending content |
| Mute | Temporary chat restriction |
| Ban | Temporary or permanent ban |
| Escalate | Send to senior admin/owner |

### Auto-Moderation Flags

System-flagged content for review:
- Profanity filter triggers
- Spam detection
- Unusual activity patterns
- Mass reporting
- Suspicious transactions

---

## Room Management

### Room Search

Search by:
- Room ID
- Room name
- Owner ID/name

### Room Actions

| Action | Permission Level |
|--------|------------------|
| View Room Details | Level 1 |
| Join Room (Hidden) | Level 2 |
| Close Room | Level 3 |
| Ban Room | Level 3 |
| Edit Room Settings | Level 4 |

### Room Monitoring

- Current participants
- Chat history (last 24 hours)
- Gift activity
- Game activity
- Report history

---

## Support Tickets

### Ticket Categories

| Category | Examples |
|----------|----------|
| Account Issues | Login problems, recovery |
| Payment Issues | Failed purchases, refunds |
| Technical Issues | Bugs, crashes |
| Report User | Harassment, scam reports |
| Feature Request | Suggestions |
| Other | General inquiries |

### Ticket Workflow

```
New → Assigned → In Progress → Resolved → Closed
                     ↓
                 Escalated → Owner Review
```

### Response Templates

Pre-built responses for common issues:
- Account recovery steps
- Payment troubleshooting
- Feature explanations
- Policy reminders

---

## Announcements

### Announcement Types

| Type | Visibility | Duration |
|------|------------|----------|
| System | All users | Until dismissed |
| Event | All users | Event duration |
| Maintenance | All users | Until complete |
| Targeted | Specific users | Custom |

### Create Announcement

- Title (max 50 chars)
- Body (max 500 chars)
- Image (optional)
- Link (optional)
- Target audience
- Start/end time
- Priority level

---

## Event Management

### Event Actions (Level 2+)

| Action | Description |
|--------|-------------|
| View Events | See all active/scheduled events |
| Create Event | Set up new event (requires Owner approval) |
| Edit Event | Modify event details |
| End Event | Terminate event early |
| Award Prizes | Distribute event rewards |

### Event Types Manageable

- Room competitions
- Gift events
- Lucky events
- Seasonal events

---

## Analytics (View Only)

### Available Reports

| Report | Description |
|--------|-------------|
| User Growth | Daily/weekly/monthly signups |
| Retention | User return rates |
| Engagement | DAU, session time, actions |
| Revenue | Purchases, gift volume |
| Top Users | Leaderboards |
| Room Stats | Popular rooms, activity |

---

## Audit Logs

All admin actions are logged:

| Field | Description |
|-------|-------------|
| Timestamp | When action occurred |
| Admin ID | Who performed action |
| Action Type | What was done |
| Target | User/room/content affected |
| Details | Specific parameters |
| Reason | Admin's stated reason |

### Log Retention

- Standard actions: 90 days
- Financial actions: 2 years
- Ban actions: Permanent

---

## Admin Tools

### Bulk Actions

| Action | Permission | Use Case |
|--------|------------|----------|
| Bulk Message | Level 3 | Notify user groups |
| Bulk Mute | Level 3 | Event moderation |
| Bulk Coin Grant | Level 4 | Event rewards |

### Search Tools

- Advanced user search
- Transaction search
- Gift flow analysis
- Device/IP lookup

### Export Tools

- User lists (CSV)
- Transaction reports
- Moderation logs
- Analytics data

---

## Admin Panel Security

### Access Control

- 2FA required
- Session timeout: 30 minutes
- IP whitelist (optional)
- Action confirmation for critical ops

### Prohibited Actions

- Accessing own account data
- Modifying own wallet
- Granting self VIP/coins
- Accessing admin accounts

---

## API Endpoints

```
# Users
GET /admin/users/search
GET /admin/users/{userId}
POST /admin/users/{userId}/warn
POST /admin/users/{userId}/mute
POST /admin/users/{userId}/ban
POST /admin/users/{userId}/coins

# Reports
GET /admin/reports
GET /admin/reports/{reportId}
POST /admin/reports/{reportId}/action

# Tickets
GET /admin/tickets
GET /admin/tickets/{ticketId}
POST /admin/tickets/{ticketId}/respond
POST /admin/tickets/{ticketId}/close

# Rooms
GET /admin/rooms/search
GET /admin/rooms/{roomId}
POST /admin/rooms/{roomId}/close

# Announcements
GET /admin/announcements
POST /admin/announcements
PUT /admin/announcements/{id}
DELETE /admin/announcements/{id}

# Logs
GET /admin/logs
GET /admin/logs/export
```

---

## Related Documentation

- [Owner Panel](./owner-panel.md)
- [Owner CMS](./owner-cms.md)
- [Operations](./operations.md)
