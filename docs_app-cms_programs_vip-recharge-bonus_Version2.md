# App CMS — VIP Recharge Bonus (Global)

Editable
- Ladder (percent by tier 1–10)
- Caps: perRecharge, perDay, perUser30d, globalBudget
- Credit mode: IMMEDIATE or BONUS_WALLET (default IMMEDIATE)
- Targeting: region(s), platform(s), app versions, cohorts
- Visibility: on/off in Room Program Slider & VIP Center; card assets/copy
- Schedule (optional)
- Experiments (A/B): alternative ladders/caps with % allocation

Validation
- Ladder values 0–100%; optional monotonic non-decreasing check
- Caps ≥ 0 and within platform hard limits

Recommended defaults (Mirror)
- Ladder: 2, 5, 7, 9, 12, 15, 20, 25, 28, 30 (%)
- Caps: 1M perRecharge; 5M perDay; 30M perUser30d; globalBudget: 0

JSON
```json
{
  "vipRechargeBonus": {
    "enabled": true,
    "creditMode": "IMMEDIATE",
    "ladder": {"1":0.02,"2":0.05,"3":0.07,"4":0.09,"5":0.12,"6":0.15,"7":0.20,"8":0.25,"9":0.28,"10":0.30},
    "caps": {"perRecharge":1000000,"perDay":5000000,"perUser30d":30000000,"globalBudget":0},
    "targeting": {"regions":["ALL"],"platforms":["ios","android"]},
    "schedule": {"startUtc":"","endUtc":""}
  }
}
```