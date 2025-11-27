# App Owner CMS — Global Configuration

Purpose
- Single control plane for the app owner to manage global economy and UX:
  - VIP programs (Recharge Bonus ladder, AuraPass Spin tickets/wheels)
  - Room Program Slider (lineup, targeting, assets)
  - Rocket/Lucky Bag/mini-games visibility and rules
  - Store promos, banners, copy, assets
  - Experiments, staged rollouts, region/platform targeting
  - Approvals, audit, and rollback

Lifecycle
- Draft → Validate → Preview (staging) → Rollout (0/10/50/100) → Monitor → Rollback
- Versioned config with immutable history
- Segmentation: region, platform, app version, cohort (A/B), VIP tier bands

Roles
- APP_OWNER, ADMIN, CONTENT_MANAGER, ANALYST (see access-control.md)

Security
- SSO + 2FA; per-action audit; optional IP allowlist; rate-limited writes