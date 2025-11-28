# Owner CMS

This document defines the Owner CMS—the administrative control panel available to app owners for managing the Coins Jar feature, VIP scaling, visibility, analytics, localization, and anti-abuse configurations.

---

## Overview

The Owner CMS provides real-time control over in-app economy features and user engagement mechanics. All changes are versioned and support instant rollback.

---

## Publishing & Versioning

### Content Publishing
- All CMS updates go through a staged workflow: Draft → Preview → Publish.
- Preview mode allows testing with selected user segments before full rollout.
- Published changes take effect within 60 seconds globally.

### Version Control
- Every configuration change creates a new version with timestamp and author ID.
- Versions are immutable; edits create new versions.
- Maximum 100 versions retained per configuration type.

### Rollback
- Instant rollback available to any previous version.
- Rollback triggers an audit log entry with reason field.
- Active version indicator shows current live configuration.

---

## Coins Jar Controls

### Task Management
| Control | Description |
|---------|-------------|
| Enable/Disable Tasks | Toggle individual tasks on/off without removing them |
| Reward Adjustment | Modify coin rewards per task (applies next reset cycle) |
| Target Thresholds | Update task completion targets |
| Task Visibility | Show/hide tasks from user-facing Jar UI |

### Daily Cycle Settings
| Setting | Default | Range |
|---------|---------|-------|
| Reset Time | Local midnight | Fixed (anchored to user's local time) |
| New User Grace Period | 7 days | 1–30 days |
| Progress Carryover | Disabled | Enable/Disable |

### Host Bonus Configuration
- Real-time settlement: 5% of guest task rewards credited to room host.
- Settlement frequency: Immediate on task completion.
- Host bonus visibility: Shown in host earnings dashboard.

---

## VIP Scaling

### Multiplier Configuration
VIP multipliers apply to EXP earned from gifting and daily rewards. The baseline is 5 coins = 1 EXP.

| VIP Tier | Additive Bonus | Effective EXP per 5 Coins |
|----------|----------------|---------------------------|
| None | +0.0 | 1.0 |
| VIP1 | +0.2 | 1.2 |
| VIP2 | +0.4 | 1.4 |
| VIP3 | +0.6 | 1.6 |
| VIP4 | +0.8 | 1.8 |
| VIP5 | +1.0 | 2.0 |
| VIP6 | +1.2 | 2.2 |
| VIP7 | +1.4 | 2.4 |
| VIP8 | +1.6 | 2.6 |
| VIP9 | +1.8 | 2.8 |
| VIP10 | +2.0 | 3.0 |

### Scaling Categories
| Category | VIP Applied | Notes |
|----------|-------------|-------|
| Gifting EXP | Yes | Per-gift calculation |
| Daily Rewards | Yes | Applied at claim time |
| Jar Task Rewards | Configurable | Default: No |
| Level-up Rewards | No | Fixed coin amounts |

---

## Visibility Settings

### Default Visibility Matrix
| Feature | New Users | Regular Users | VIP Users |
|---------|-----------|---------------|-----------|
| Coins Jar | Show | Show | Show |
| Daily Tasks | Show | Show | Show |
| VIP Benefits | Teaser | Teaser | Full |
| Level Progress | Show | Show | Show |
| Cosmetic Shop | Show | Show | Enhanced |

### Conditional Display Rules
- Jar widget: Hidden if user has no claimable tasks remaining for the day.
- VIP badge: Displays only for VIP1+ users.
- Level milestones: Notification shown on level-up only.

---

## Analytics Dashboard

### Real-Time Metrics
| Metric | Description | Refresh Rate |
|--------|-------------|--------------|
| Active Jar Users | Users with incomplete tasks | 1 minute |
| Task Completion Rate | % tasks completed vs started | 5 minutes |
| Coins Distributed | Total coins via Jar today | 1 minute |
| Host Bonus Pool | Accumulated host bonuses | 1 minute |

### Historical Reports
- Daily Jar engagement: Task completion by type.
- Weekly VIP conversion: Users upgrading after Jar interaction.
- Monthly coin flow: Jar rewards vs other sources.

### Export Options
- CSV export for all metrics.
- API access for integration with external analytics.
- Scheduled reports: Daily/weekly email summaries.

---

## Localization

### Supported Content
| Content Type | Localizable | Notes |
|--------------|-------------|-------|
| Task Descriptions | Yes | Per-language strings |
| Reward Labels | Yes | Include currency formatting |
| Error Messages | Yes | Full localization required |
| CMS UI | No | English only |

### Language Management
- Primary language: English (en).
- Fallback: English when translation missing.
- RTL support: Arabic (ar), Hebrew (he) planned.

### Translation Workflow
1. Add new string keys in CMS.
2. Export translation file.
3. Import completed translations.
4. Preview in target locales.
5. Publish with version control.

---

## Anti-Abuse

### Rate Limiting
| Action | Limit | Window | Penalty |
|--------|-------|--------|---------|
| Task Claims | 50 | 1 hour | Soft lock (retry after window) |
| Invite Submissions | 20 | 1 day | Hard lock (manual review) |
| Share Actions | 100 | 1 day | Ignored beyond limit |

### Detection Rules
| Rule | Trigger | Response |
|------|---------|----------|
| Multi-Account Abuse | Same device ID claiming across accounts | Flag for review |
| Rapid Invite Cycling | >10 invite accepts in 5 minutes | Temporary invite lock |
| Bot Detection | Non-human interaction patterns | Shadow ban pending review |

### Invite Success Criteria
- Invite counted as successful when invitee remains in room for 30 seconds.
- Invitee must be a unique user (not previously invited by same host in 24 hours).
- Self-invites (same device) rejected.

### Mic Presence Validation
- Muted mic allowed: User on mic counts even when muted.
- Mic concurrency: Tracks active mic sessions per user.
- Reset on leave: Mic timer resets when user leaves room.

### Share Intent Validation
- Share completion: Intent-only; actual share destination not tracked.
- Platform support: All share destinations counted equally.
- Cooldown: 60 seconds between share attempts for same content.

### Enforcement Actions
| Severity | Action | Duration |
|----------|--------|----------|
| Low | Warning notification | N/A |
| Medium | Feature restriction | 24 hours |
| High | Account suspension | 7 days |
| Critical | Permanent ban | Indefinite |

---

## Audit Logging

All CMS actions are logged with:
- Timestamp (UTC)
- Admin user ID
- Action type
- Before/after values
- IP address
- Session ID

Logs retained for 365 days. Export available for compliance.

---

## Access Control

### Role Permissions
| Role | View | Edit | Publish | Rollback | Audit |
|------|------|------|---------|----------|-------|
| Viewer | ✓ | — | — | — | — |
| Editor | ✓ | ✓ | — | — | — |
| Publisher | ✓ | ✓ | ✓ | — | — |
| Admin | ✓ | ✓ | ✓ | ✓ | ✓ |

### Session Management
- Session timeout: 30 minutes idle.
- Concurrent sessions: Maximum 3 per admin.
- MFA required for Publisher and Admin roles.

---

## Related Documentation

- [EXP & Levels System](./exp-levels-system.md)
- [Jar Tasks Data](../data/jar-tasks.json)
- [Jar Rules Data](../data/jar-rules.json)
- [VIP Multipliers Data](../data/vip-multipliers.json)
- [Level Rewards Data](../data/level-rewards.json)
- [Cosmetics Data](../data/cosmetics.json)
