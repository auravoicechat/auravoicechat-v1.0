# Aura Voice Chat — Complete App Overview & Gap Analysis

This document provides a comprehensive overview of all features currently documented, how they interconnect, and identifies gaps that need to be addressed for production readiness.

---

## Executive Summary

**Current State**: The documentation covers core features including authentication, economy (coins/diamonds), VIP system, levels/EXP, Coins Jar daily tasks, cosmetics, referrals, and room management. The Owner CMS provides administrative control.

**Completeness**: ~70% documented. Key gaps exist in Rooms/Stage mechanics, Family feature, Store pricing, and CP thresholds.

---

## Feature Inventory

### ✅ Fully Documented Features

| Feature | Documentation | Data Files | Status |
|---------|---------------|------------|--------|
| Authentication | README.md, getting-started.md | — | Complete |
| Daily Login Rewards | README.md | — | Complete |
| VIP System | README.md, docs/owner-cms.md | data/vip-multipliers.json | Complete |
| EXP & Levels | docs/exp-levels-system.md | data/level-rewards.json | Complete |
| Coins Jar | docs/owner-cms.md | data/jar-tasks.json, data/jar-rules.json | Complete |
| Cosmetics | docs/exp-levels-system.md | data/cosmetics.json | Complete |
| Referral - Get Coins | README.md | — | Complete |
| Referral - Get Cash | README.md | — | Complete |
| Branding & Theme | README.md, getting-started.md | — | Complete |

### ⚠️ Partially Documented Features

| Feature | What's Documented | What's Missing |
|---------|-------------------|----------------|
| Rooms & Stage | Basic structure, seat counts, video/music | Detailed seat mechanics, events, gift animations |
| Medals System | Categories, login milestones | Full medal catalog, gift medals thresholds |
| Wallet & Exchange | Coin/Diamond conversion | Transaction limits, fee structures |
| Store & Items | Categories listed | Pricing tables, item catalog |
| CP (Couple Partnership) | Formation fee, basic rewards | Full threshold table, dissolution rules |
| Messaging | DM limits, categories | Attachment rules, retention policy |

### ❌ Not Yet Documented Features

| Feature | Notes from Open Questions |
|---------|---------------------------|
| Family Feature | Member cap, roles, perks, creation fee (questions-pending.md #62-66) |
| Jackpot Mechanics | Pool vs probability model (open-questions-updated.md) |
| Lucky Bag Events | Event triggers, cooldowns |
| Room Events | Rocket, seasonal banners |
| Admin Dashboards | Metrics, fraud detection |
| Gift Catalog | Regional variants, price ranges |
| Baggage System | UI, daily caps, send rules |

---

## Feature Interconnection Map

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                              AURA VOICE CHAT                                    │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  ┌──────────────┐     ┌──────────────┐     ┌──────────────┐                    │
│  │ AUTHENTICATION│────▶│    HOME      │────▶│    ROOMS     │                    │
│  │ Google/FB/Phone│    │ Mine/Popular │     │ Voice/Video  │                    │
│  └──────────────┘     └──────────────┘     └──────────────┘                    │
│         │                    │                    │                             │
│         ▼                    ▼                    ▼                             │
│  ┌──────────────┐     ┌──────────────┐     ┌──────────────┐                    │
│  │ DAILY REWARDS │◀───│   WALLET     │◀────│   GIFTING    │                    │
│  │ Day 1-7 Cycle │     │ Coins/Diamonds│     │ 50→200M coins│                    │
│  └──────────────┘     └──────────────┘     └──────────────┘                    │
│         │                    │                    │                             │
│         │                    │                    │                             │
│         ▼                    ▼                    ▼                             │
│  ┌──────────────┐     ┌──────────────┐     ┌──────────────┐                    │
│  │  VIP SYSTEM  │────▶│  EXP/LEVELS  │◀────│  COINS JAR   │                    │
│  │ VIP1→VIP10   │     │ Level 1→100  │     │ Daily Tasks  │                    │
│  │ 1.2x→3.0x    │     │ 5 coins=1 EXP│     │ Host 5% Bonus│                    │
│  └──────────────┘     └──────────────┘     └──────────────┘                    │
│         │                    │                    │                             │
│         │                    ▼                    │                             │
│         │            ┌──────────────┐            │                             │
│         │            │  COSMETICS   │◀───────────┘                             │
│         │            │Frames/Medals │                                          │
│         │            │Themes/Vehicles│                                          │
│         │            └──────────────┘                                          │
│         │                    │                                                  │
│         ▼                    ▼                                                  │
│  ┌──────────────┐     ┌──────────────┐     ┌──────────────┐                    │
│  │    STORE     │◀────│   PROFILE    │────▶│   MEDALS     │                    │
│  │ Buy cosmetics │     │ Me Screen    │     │ Achievement  │                    │
│  └──────────────┘     └──────────────┘     └──────────────┘                    │
│                              │                                                  │
│                              ▼                                                  │
│                       ┌──────────────┐                                          │
│                       │  REFERRALS   │                                          │
│                       │ Coins / Cash │                                          │
│                       └──────────────┘                                          │
│                                                                                 │
│  ┌─────────────────────────────────────────────────────────────────────────────┤
│  │                        ADMIN LAYER                                          │
│  │  ┌──────────────┐     ┌──────────────┐     ┌──────────────┐                │
│  │  │  OWNER CMS   │────▶│  ANALYTICS   │────▶│ ANTI-ABUSE   │                │
│  │  │ Jar Controls │     │ Metrics/KPIs │     │ Rate Limits  │                │
│  │  │ VIP Config   │     │ Reports      │     │ Fraud Detect │                │
│  │  └──────────────┘     └──────────────┘     └──────────────┘                │
│  └─────────────────────────────────────────────────────────────────────────────┤
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## Detailed Feature Breakdown

### 1. User Journey Flow

```
New User → Auth → Home → Join Room → Send Gift → Earn EXP → Level Up → Unlock Cosmetics
                    ↓
              Daily Rewards → Coins Jar Tasks → More Coins → More Gifting
                    ↓
              VIP Purchase → Multipliers → Faster Progression
                    ↓
              Referrals → Earn More Coins/Cash → Withdraw
```

### 2. Economy Flow

| Source | Output | Conversion |
|--------|--------|------------|
| Daily Login | Coins | 5,000→50,000/day |
| Coins Jar Tasks | Coins | 500→20,000/task |
| Level Rewards | Coins | 1,000→5,000,000 |
| Referral (Get Coins) | Coins | 2.5M + 5% of gifts |
| Referral (Get Cash) | USD | $0.20→$100 per level |
| VIP Multiplier | Bonus | 1.2x→3.0x on rewards |

| Sink | Input | Effect |
|------|-------|--------|
| Gifting | Coins | Converts to Diamonds for recipient |
| Store Purchases | Coins | Buy cosmetics |
| CP Formation | 3M Coins | Couple partnership |
| Family Creation | Coins (TBD) | Create family group |

### 3. Progression Systems

#### Level Progression (1→100)
- **EXP Sources**: Gifting (1 EXP per 5 coins), Daily activities
- **VIP Bonus**: +0.2 to +2.0 EXP per 5 coins
- **Unlocks**: Super Mic (L40), Custom ID (L65), Cosmetics throughout

#### VIP Progression (VIP1→VIP10)
- **Acquisition**: Real money purchase
- **Benefits**: Daily reward multiplier, EXP boost
- **Display**: VIP badge, exclusive frames

#### Medal Progression
- **Categories**: Gift, Achievement, Activity
- **Login Milestones**: 30/60/90/180/365 days
- **Display**: Up to 10 medals under profile name

---

## Documentation Structure

```
auravoicechatdoc/
├── README.md                    # Master product spec
├── docs/
│   ├── owner-cms.md            # CMS administration
│   ├── exp-levels-system.md    # EXP & leveling
│   └── app-overview.md         # This document
├── data/
│   ├── jar-tasks.json          # Daily task catalog
│   ├── jar-rules.json          # Jar operational rules
│   ├── vip-multipliers.json    # VIP tier bonuses
│   ├── level-rewards.json      # Level rewards table
│   └── cosmetics.json          # Cosmetic unlocks
├── getting-started.md          # Onboarding flow
├── configuration.md            # Environment config
├── architecture.md             # System design
├── operations.md               # Monitoring & alerts
├── troubleshooting.md          # Error resolution
├── api.md                      # API reference (stub)
├── security.md                 # Security guidelines (stub)
├── deployment.md               # CI/CD (stub)
├── changelog.md                # Release notes
├── open-questions-updated.md   # Pending decisions
└── questions-pending.md        # Original questions
```

---

## Gap Analysis & Recommendations

### High Priority (Required for Launch)

| Gap | Impact | Recommendation |
|-----|--------|----------------|
| **Room Mechanics** | Core feature | Document seat interactions, mic controls, admin powers |
| **Gift Catalog** | Revenue | Define price tiers, regional variants, animation specs |
| **Store Pricing** | Revenue | Complete pricing tables by category/rarity |
| **CP Thresholds** | Engagement | Finalize 5M→250M threshold table with rewards |
| **Family Feature** | Social | Define member caps, roles, perks, fees |

### Medium Priority (Pre-Launch Polish)

| Gap | Impact | Recommendation |
|-----|--------|----------------|
| API Reference | Development | Complete endpoint documentation |
| Security Guidelines | Compliance | Expand threat model, encryption details |
| Admin Dashboards | Operations | Define metrics, alerts, fraud triggers |
| Baggage System | Engagement | Document UI, send rules, caps |
| Jackpot Mechanics | Engagement | Choose model (pool vs probability) |

### Low Priority (Post-Launch)

| Gap | Impact | Recommendation |
|-----|--------|----------------|
| Prestige System | Long-term retention | Plan for Level 100+ users |
| Guest Mode | Onboarding | Allow trial without account |
| Dark Mode Spec | UX | Finalize palette inversion |
| RTL Support | Internationalization | Arabic/Hebrew layout |

---

## Open Decisions Summary

From `open-questions-updated.md`, these decisions are still pending:

### Authentication & Platform
- [ ] Target SDK: API 33, 34, or latest (#1)
- [ ] Twilio fallback trigger strategy (#3)
- [ ] Tutorial scope (#4)

### Rooms & Stage
- [ ] 16-seat toggle mid-session (#9)
- [ ] Super Mic visual style (#10)
- [ ] Ban duration defaults (#11)
- [ ] Announcement edit cooldown (#13)

### Events & Gifts
- [ ] Initial event list: Lucky Bag only vs more (#14)
- [ ] Multi-send UX (#16)
- [ ] Gift animation concurrency cap (#18)
- [ ] Regional gift precedence (#19)

### Jackpot (If Adopted)
- [ ] Model: Progressive pool vs per-send probability (#20)
- [ ] Show odds publicly (#21)

### Economy & VIP
- [ ] VIP billing model (#30)
- [ ] Large gift warning threshold (#29)

### CP & Family
- [ ] CP formation payment model (#35)
- [ ] CP dissolution rules (#36-37)
- [ ] Family member cap (#63)
- [ ] Family perks (#66)

### Referral & Payouts
- [ ] External payout methods (#41)
- [ ] KYC threshold (#42)

### Messaging & Privacy
- [ ] DM attachments after mutual follow (#45)
- [ ] Quiet hours (#47)
- [ ] Discoverability default (#48)

---

## Next Steps

1. **Immediate**: Answer pending decisions in `open-questions-updated.md`
2. **This Week**: Document Room mechanics, Gift catalog, Store pricing
3. **This Sprint**: Complete CP thresholds, Family feature, API reference
4. **Pre-Launch**: Security audit, Load testing specs, Admin dashboards

---

## Related Documentation

- [README.md](../README.md) — Master product specification
- [Owner CMS](./owner-cms.md) — Administrative controls
- [EXP & Levels](./exp-levels-system.md) — Progression system
- [Open Questions](../open-questions-updated.md) — Pending decisions
- [Questions Pending](../questions-pending.md) — Original question list
