# EXP & Levels System

This document defines the experience points (EXP) and leveling system, including EXP sources, VIP bonuses, level unlock bands, rewards, and integration with the Coins Jar feature.

---

## Overview

The EXP & Levels system tracks user engagement and unlocks rewards, cosmetics, and features as users progress. EXP is earned through various activities, with VIP users receiving additive bonuses.

---

## EXP Baseline

The core conversion rate for EXP:

**5 Coins spent = 1 EXP** (baseline)

This applies to:
- Gifting coins to other users
- Certain premium purchases

---

## EXP Sources

### Primary Sources
| Activity | EXP Earned | Notes |
|----------|------------|-------|
| Gifting (Sending) | 1 EXP per 5 coins | VIP bonus applies |
| Daily Login | Fixed per day | VIP bonus applies |
| Level-up Bonus | Milestone rewards | Fixed amounts |

### Secondary Sources (Coins Jar)
| Task Type | EXP Earned | Notes |
|-----------|------------|-------|
| Jar Task Completion | Configurable | Default: No EXP, coins only |
| Invite Tasks | 0 EXP | Coins reward only |
| Mic Presence Tasks | 0 EXP | Coins reward only |

### Activity-Based EXP
| Activity | EXP Earned |
|----------|------------|
| Room hosting (daily) | 10 EXP |
| Room participation (30+ min) | 5 EXP |
| First gift sent (daily) | 2 EXP |
| Profile completion | 50 EXP (one-time) |

---

## VIP Additive Bonuses

VIP tiers provide additive bonuses to the baseline EXP rate. The bonus applies per 5-coin increment.

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

### Calculation Example
A VIP5 user sending a 1,000,000 coin gift:
- Base EXP: 1,000,000 / 5 = 200,000 EXP
- VIP5 Bonus: 200,000 × 1.0 = 200,000 additional EXP
- Total EXP: 400,000 EXP

---

## Level Unlock Bands

Levels are grouped into bands with increasing EXP requirements per level.

### Level Progression
| Level Range | EXP per Level | Cumulative EXP (End of Band) |
|-------------|---------------|------------------------------|
| 1–10 | 1,000 | 10,000 |
| 11–20 | 2,500 | 35,000 |
| 21–30 | 5,000 | 85,000 |
| 31–40 | 10,000 | 185,000 |
| 41–50 | 20,000 | 385,000 |
| 51–60 | 40,000 | 785,000 |
| 61–70 | 80,000 | 1,585,000 |
| 71–80 | 160,000 | 3,185,000 |
| 81–90 | 320,000 | 6,385,000 |
| 91–100 | 500,000 | 11,385,000 |

### Level Cap
- Maximum level: 100
- EXP beyond level 100 is tracked but does not grant additional levels.
- Prestige system: Planned for future release.

---

## Level Rewards

Coin rewards are granted upon reaching specific levels. For levels 1–10, rewards are given at every level. Beyond level 10, rewards are given at milestone levels (every 5 levels). See [Level Rewards Data](../data/level-rewards.json) for the complete table.

### Early Level Rewards (1–10)
| Level | Coin Reward |
|-------|-------------|
| 1 | 1,000 |
| 2 | 1,500 |
| 3 | 2,000 |
| 4 | 2,500 |
| 5 | 5,000 |
| 6 | 5,500 |
| 7 | 6,000 |
| 8 | 6,500 |
| 9 | 7,000 |
| 10 | 10,000 |

### Milestone Rewards Summary (15+)
Levels 11–14 have no coin rewards. Starting at level 15, rewards are granted at each milestone level (every 5 levels).

| Level | Coin Reward | Special Unlock |
|-------|-------------|----------------|
| 5 | 5,000 | — |
| 10 | 10,000 | Frame unlock |
| 15 | 15,000 | Medal unlock |
| 20 | 25,000 | — |
| 25 | 35,000 | Vehicle unlock |
| 30 | 50,000 | Theme unlock |
| 35 | 75,000 | Vehicle unlock |
| 40 | 100,000 | **Super Mic unlock** |
| 45 | 150,000 | Vehicle unlock |
| 50 | 200,000 | Frame unlock |
| 55 | 300,000 | Vehicle unlock |
| 60 | 400,000 | Entrance Style unlock |
| 65 | 500,000 | Vehicle unlock, Custom ID (45 days) |
| 70 | 750,000 | Entrance Style unlock |
| 75 | 1,000,000 | Vehicle unlock |
| 80 | 1,500,000 | Opening Page Cover unlock |
| 85 | 2,000,000 | Vehicle unlock |
| 90 | 2,500,000 | Opening Page Cover unlock |
| 95 | 3,000,000 | Vehicle unlock |
| 100 | 5,000,000 | Vehicle unlock, Opening Page Cover unlock |

---

## Super Mic Feature

Unlocked at Level 40, Super Mic provides enhanced audio broadcasting capabilities:

### Features
- Priority audio channel in crowded rooms
- Enhanced voice effects
- Visual indicator on user avatar
- Increased mic hold duration

### Requirements
- Reach Level 40
- One-time activation (no recurring cost)
- Available in all room types

---

## Coins Jar Integration

The Coins Jar system provides daily tasks that reward coins. While Jar tasks do not directly grant EXP, the coins earned can be used for gifting, which then converts to EXP.

### Integration Flow
1. User completes Jar task → Coins credited
2. User sends gift with earned coins → EXP earned
3. VIP bonus applied to gift EXP

### Jar-to-Level Synergy
| Jar Task Type | Coins Earned | Potential EXP (if gifted) |
|---------------|--------------|---------------------------|
| Invite Tasks | 500–10,000 | 100–2,000 (base) |
| Mic Tasks | 1,000–5,000 | 200–1,000 (base) |
| Gifting Tasks | 2,000–20,000 | 400–4,000 (base) |
| Daily Check-in | 1,000 | 200 (base) |

---

## Progress Display

### Level Badge
- Displayed next to username in all contexts
- Badge color evolves with level bands:
  - 1–25: Bronze
  - 26–50: Silver
  - 51–75: Gold
  - 76–99: Platinum
  - 100: Diamond

### Progress Bar
- Circular progress indicator on profile
- Shows: Current Level, EXP Progress, EXP to Next Level
- Tap for detailed breakdown

### Leaderboard Integration
- Level leaderboards: Global, Friends, Room
- Weekly and all-time rankings
- Rewards for top performers (configurable in CMS)

---

## Data Persistence

### EXP Tracking
- All EXP transactions logged with source and timestamp
- Real-time balance updates
- Historical EXP breakdown available in profile

### Level History
- Level-up timestamps recorded
- Rewards claim status tracked
- Rollback not supported (EXP only accrues)

---

## Related Documentation

- [Owner CMS](./owner-cms.md)
- [VIP Multipliers Data](../data/vip-multipliers.json)
- [Level Rewards Data](../data/level-rewards.json)
- [Cosmetics Data](../data/cosmetics.json)
- [Jar Tasks Data](../data/jar-tasks.json)
