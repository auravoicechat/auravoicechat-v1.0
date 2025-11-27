# App CMS — AuraPass Spin (Global)

Editable
- Ticket earn rule (e.g., +1 per 500,000 coins)
- Wheel definitions:
  - Spark/Flare: tokenCost, segments [score, probability], 10x bonus
- Caps: daily spins per user per wheel; optional max Score/day
- Targeting: region, platform, app version, cohorts
- Visibility: on/off in Room Program Slider & VIP Center; assets + copy
- Odds disclosure toggle; RNG seed rotation policy

Validation
- Sum(probabilities) = 1.0 per wheel (±0.001)
- Segments 6–12; non-negative scores

JSON
```json
{
  "aurapassSpin": {
    "enabled": true,
    "earnRule": {"perRechargeCoins": 500000, "ticketsPerUnit": 1},
    "wheels": {
      "spark": {"tokenCost": 1, "tenPackBonus": 0.05, "segments": []},
      "flare": {"tokenCost": 5, "tenPackBonus": 0.08, "segments": []}
    },
    "caps": {"dailySpins":{"spark":200,"flare":100}},
    "targeting": {"regions":["ALL"],"platforms":["ios","android"]},
    "oddsDisclosure": {"viewOddsEnabled": true}
  }
}
```