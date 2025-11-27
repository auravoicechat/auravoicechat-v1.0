# App CMS — Approvals, Staging, and Rollbacks

Workflow
- Draft → Validate (schema + semantic checks) → Staging → Rollout (0/10/50/100) → Monitor → Rollback
- High-risk areas require dual-approval: VIP ladders/caps, wheel segments/odds, credit mode changes

Rollback
- Revert to any signed version; cache bust; audit entry

Audit
- Diff (before/after), actor, approvers, timestamp, reason; export CSV/JSON