# VIP Recharge Bonus — Persistent Program

Purpose
- Always-on program: every recharge grants extra coins according to the user’s current AuraPass (VIP) tier.

How it works
- At recharge settlement, apply the tier’s bonus percentage to the recharge amount
- Credit bonus coins immediately to the user’s balance (recommended mode)

Ladder (Mirror; CMS-editable by App Owner)
- VIP1: 2%
- VIP2: 5%
- VIP3: 7%
- VIP4: 9%
- VIP5: 12%
- VIP6: 15%
- VIP7: 20%
- VIP8: 25%
- VIP9: 28%
- VIP10: 30%

Economy caps (recommended; CMS)
- perRecharge: 1,000,000 coins
- perDay: 5,000,000 coins
- perUser30d: 30,000,000 coins
- globalBudget: 0 (unlimited) or a hard cap

Eligibility
- The tier applied is the tier at payment authorization time

UI surfaces
- Room Program Slider → program://vip-recharge-bonus
- VIP Center section: current tier rate, ladder table, records

APIs
- GET /programs/vip-recharge-bonus/status
- GET /programs/vip-recharge-bonus/records?cursor=…

Anti-abuse
- Server authoritative calculation; idempotent ledger (txnId + userId)
- Chargebacks revoke proportional bonus; velocity checks