# AuraPass Spin — Persistent Program

Purpose
- Always-on “spin” program to earn AuraPass (VIP) Score to help keep/upgrade VIP tiers.

Tickets (default)
- +1 Spin Ticket per 500,000 coins recharged (CMS-adjustable)
- Tickets may also be sold in bundles (CMS)

Wheels
- Spark: lower ticket cost; smaller Score yields; 10x spin = +5% Score bonus
- Flare: higher ticket cost; larger Score yields; 10x spin = +8% Score bonus
- Each spin guarantees VIP Score (no blanks)

UI
- Room Program Slider → program://aurapass-spin
- VIP Center “Spin” module with Spark/Flare tabs; Records and optional Odds

CMS (global controls live under App Owner CMS)
- Ticket earn rule, wheel segments/probabilities, caps, targeting, visibility

APIs
- GET /programs/aurapass-spin/status
- POST /programs/aurapass-spin/spin { wheel, count: 1|10 }
- GET /programs/aurapass-spin/records?cursor=…

Fairness
- Secure PRNG; signed results (resultId); idempotent ticket consumption