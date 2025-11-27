# Room Program Slider (In-Room Carousel)

Purpose
- Always-on, compact carousel inside rooms to surface programs and promos:
  - VIP Recharge Bonus, AuraPass Spin
  - Rocket, Lucky Bag
  - Mini‑games (e.g., Greedy Baby)
  - Store promos, Owner pins

Placement & Size
- Right dock; 96 × 128 dp (tablet 120 × 160); radius 12 dp; 12 dp margins
- Z: above background; below full-screen modals

Behavior
- Auto-rotate 5s; pause during interaction; resume after 8s idle
- Swipe to switch; tap to open deep link
- Long-press: Pin (owner), Hide 24h (user), More info
- Reduce Motion: auto-rotate off; manual swipe

Deep links (examples)
- program://vip-recharge-bonus
- program://aurapass-spin
- event://rocket/{roomId}
- event://lucky-bag/{roomId}
- game://greedy-baby/{roomId}
- store://gift/{id}
- room://announcement

Prioritization
- “Has tickets/bonus available” > live game > rocket > owner pin > store/seasonal (CMS can override)

Owner controls
- Toggle slider ON/OFF, pin up to 2 cards, order vs global, hide card 24h (compliance/system cannot be hidden)

CMS model
```json
{
  "roomProgramSlider": {
    "maxSlots": 6,
    "autoRotateSeconds": 5,
    "cards": [
      {"id":"vip_recharge_bonus","type":"PROGRAM","deeplink":"program://vip-recharge-bonus","priority":90},
      {"id":"aurapass_spin","type":"PROGRAM","deeplink":"program://aurapass-spin","priority":85}
    ]
  }
}
```

Accessibility & Telemetry
- Labels: “VIP Recharge Bonus. Button.”; dots: “Card 2 of 5”
- slider_impression/rotate/click/pin_toggle/hide