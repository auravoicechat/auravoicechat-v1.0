# App CMS — Room Program Slider (Global)

Editable
- Global lineup and order
- Card definitions (deeplink, asset, copy)
- Priority and conditions (e.g., show when user has ≥1 Spin Ticket, VIP≥4)
- Targeting by region/platform/app version/cohorts
- A/B experiments for order and creatives

Card JSON
```json
{
  "roomProgramSlider": {
    "maxSlots": 6,
    "autoRotateSeconds": 5,
    "cards": [
      {"id":"vip_recharge_bonus","type":"PROGRAM","deeplink":"program://vip-recharge-bonus","priority":95},
      {"id":"aurapass_spin","type":"PROGRAM","deeplink":"program://aurapass-spin","priority":90}
    ]
  }
}
```

Publish
- Drag-and-drop reorder, staging preview, staged rollout, rollback