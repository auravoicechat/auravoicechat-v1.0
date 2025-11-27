# AuraPass (VIP) — Full Specification v0.8

Overview
- Program: AuraPass (VIP1–VIP10)
- Purchase: Real-money subscription (7-day, 30-day; CMS-configurable)
- Tier progress: VIP Score from recharges/bundles (thresholds below; CMS-managed)
- Daily rewards multiplier: VIP1 1.20x → VIP10 3.00x
- Super Mic eligibility: VIP4+ or Level ≥40
- Visual identity: Cosmic/Aurora motif (Comet, Nebula, Aurora, Eclipse). No animal motifs.

Global rules
- Cosmetics precedence: AuraPass visuals override standard frames/effects while active (user can toggle per slot)
- Expiry: All perks/cosmetics end at expiry (optional 24h grace via CMS)
- Notifications: Renewal reminders at 7d/3d/24h
- App Owner CMS manages global ladders, wheels, slider lineups, targeting, and rollouts

VIP Score thresholds (aligned with screenshots; adjustable in CMS)
- VIP1: 2,000
- VIP2: 15,000
- VIP3: 50,000
- VIP4: 125,000
- VIP5: 250,000
- VIP6: 500,000
- VIP7: 850,000
- VIP8: 1,350,000
- VIP9: 1,800,000 (synthesized)
- VIP10: 2,500,000 (synthesized)

Originalized perk names (avoid copying)
- View visitor records → Audience Insights
- Room online list on top → Room Spotlight
- Send pictures in the room → Rich Media Chat
- Profile background / Multiple backgrounds → Dynamic Covers / Cover Rotation
- SVIP Gift → Monthly Aura Crate
- EXP*120% / Level acceleration → EXP Boost (tiered)
- Hide visit records → Ghost Mode (visit trail privacy)
- Customized theme → Custom Themes
- Golden name → Aurora Nameplate
- Hide Online Status → Incognito Status
- 6‑digit Special ID → Short ID (6‑digit vanity)
- 5‑digit Special ID → Ultra Short ID (limited pool)
- Avoid disturbing → Do Not Disturb (DND)
- Can’t be kicked → Kick Shield (owner-only removal)
- Dynamic Avatar (GIF) → Animated Avatar
- Can’t be ban public chat → Chat Mute Guard (owner/admin-lead required for long mutes)
- Unban Account *N → Priority Appeal Tokens (fast-track review; no policy bypass)
- Mic Wave → Aura Wave (mic pulse ring)
- Vehicle → Entry Vehicle (arrival effect)
- Mic Skin → Mic Skin (styled microphone)
- Room Border → VIP Room Border
- Special Colorful Name → Animated Gradient Nameplate

Safety rails (all tiers)
- Kick Shield never blocks room owner or platform enforcement
- Chat Mute Guard restricts low-privilege mod mutes; owner/admin-lead can still apply normal moderation; policy violations override
- Ghost/Incognito never hide actions from moderation logs

Perk matrix (summary)
- Rewards multiplier: 1.20x → 3.00x
- EXP boost: starts 1.05x (VIP1) and scales
- Audience Insights: last 50 → 100 → 200 → 90d → 180d → 365d archive
- Ghost Mode minutes/day scale by tier; split across uses with cooldown
- Visibility: Room Spotlight scales with tier, fair-capped
- Rich Media Chat available from VIP2 when room enables “VIP Media”
- Cosmetics per tier: Crown Title, Medal, Nebula Frame, Aurora Bubble, Entry Trail; higher tiers add Entry Anthem, Seat Aura, Aura Wave, Entry Vehicle, Mic Skin, VIP Room Border

Tier details

VIP1 — “Comet”
- Daily: 1.20x; EXP: 1.05x
- Perks: Audience Insights (50, 7‑day), Room Spotlight (followers bias), Dynamic Cover (1), Monthly Aura Crate I
- Cosmetics: Crown I, Medal I, Frame I, Bubble I, Entry Trail I

VIP2 — “Nebula”
- Daily: 1.30x; EXP: 1.08x
- Perks: Rich Media Chat (room VIP Media), Cover Rotation (5), Ghost 10m/day (12h cd), Insights+ (100), Spotlight+, Crate II
- Cosmetics: Crown II, Medal II, Frame II, Bubble II, Entry Trail II

VIP3 — “Aurora”
- Daily: 1.40x; EXP: 1.10x
- Perks: Priority Mic Queue (1 jump/session), Insights Pro (200 + CSV export), Custom Themes, Aurora Nameplate, Incognito 15m/day (8h cd), Crate III, Short ID request
- Cosmetics: Crown III, Medal III, Frame III, Bubble III, Entry Trail III, Entry Anthem (short; respects room mute)

VIP4 — “Eclipse”
- Daily: 1.60x; EXP: 1.15x
- New: Super Mic eligibility
- Perks: Spotlight Pro, Ghost 20m/day (2 uses), Rich Media Persist (room VIP Media), Video Covers (≤10s), Short ID lock window, Crate IV, Seat Aura I
- Cosmetics: Crown IV, Medal IV, Frame IV, Bubble IV, Entry Trail IV

VIP5 — “Solar Flare”
- Daily: 1.80x; EXP: 1.18x
- Perks: DND, Kick Shield (admins need owner approval), Ghost 25m/day, Nameplate+, Custom Themes+, Cover Rotation 8, Short ID change once/term, Crate V, Seat Aura II
- Cosmetics: Crown V, Medal V, Frame V, Bubble V, Entry Trail V

VIP6 — “Starlight”
- Daily: 2.00x; EXP: 1.22x
- Perks: Priority Join Lane (auto-enter on next open seat; 10 min cd; owner can disable), Ghost 30m/day (2–3 uses; 6h cd), Spotlight Elite (fair-capped), Animated Avatar, Chat Mute Guard (low-priv mods ≤60s), DND & Kick Shield persist, Custom Themes++ & Cover Rotation 10, Insights 90‑day, Crate VI
- Cosmetics: Crown VI, Medal VI, Frame VI, Bubble VI, Entry Trail VI, Seat Aura III

VIP7 — “Supernova”
- Daily: 2.20x; EXP: 1.26x
- Perks: VIP Room Border, Ultra Short ID (5‑digit; one change/term while supply lasts), Priority Appeal Token ×1/term, Chat Mute Guard+ (only owner/admin-lead can long-mute), Ghost 40m/day (3 uses; 6h cd), Incognito 30m/day, Animated Gradient Nameplate, Emote Pack+, DND/Kick Shield/Animated Avatar continue, Insights 180‑day, Crate VII
- Cosmetics: Crown VII, Medal VII, Frame VII, Bubble VII, Entry Trail VII (dual glow), VIP Room Border

VIP8 — “Quasar”
- Daily: 2.50x; EXP: 1.30x
- Identity/cosmetics:
  - Aura Wave (mic pulse ring)
  - Entry Vehicle (arrival effect)
  - Mic Skin (gold/aurora ringed mic)
  - VIP Room Border (upgraded)
- Perks: Ghost 50m/day (≤4 uses; 4h cd), Incognito 45m/day, Ultra Short ID guaranteed + Short ID reserve; one vanity change/term, Priority Appeal Tokens ×3/term, Chat Mute Guard+ continues, Kick Shield & DND continue, Animated Gradient Nameplate Pro, Dual Entry Trail, Insights 365‑day, Crate VIII
- Cosmetics: Crown VIII, Medal VIII, Frame VIII, Bubble VIII, Entry Trail VIII, Aura Wave, Entry Vehicle, Mic Skin, VIP Room Border+

VIP9 — “Aurora Prime” (synthesized, balanced)
- Daily: 2.80x; EXP: 1.30x
- Perks: Spotlight Elite+, Ghost 45m/day (3 uses; 4h cd), Priority Mic Queue×2, Entry Anthem+ (longer; user/room mute respected), Dual Trail Pro, Crate IX, Insights 18‑month archive
- Cosmetics: Crown IX (animated), Medal IX, Frame IX, Bubble IX, Entry Trail IX

VIP10 — “Eclipse Prime” (synthesized, prestige)
- Daily: 3.00x; EXP: 1.40x
- Perks: Spotlight Champion (fair‑capped), Ghost 60m/day (4 uses), Co‑host Express (owner acceptance), VIP Lounge Pass (rooms may enable VIP-only lane), Exclusive Seasonal Cosmetic/month, Crate X, Insights 24‑month archive
- Cosmetics: Crown X (signature animated), Medal X, Frame X+, Bubble X+, Streaked Comet Entry Trail, Profile Backdrop “Eclipse”

Store & pricing (to finalize)
- Durations: 7 or 30 days (CMS)
- Upgrade: pay difference; CMS sets proration vs reset
- Grace: default 0h; optional 24h

VIP‑Linked Programs (Permanent)
- VIP Recharge Bonus (immediate bonus coins by tier): see docs/features/vip-linked-programs.md and docs/programs/vip-recharge-bonus.md
- AuraPass Spin (tickets → VIP Score wheels): see docs/features/vip-linked-programs.md and docs/programs/aurapass-spin.md