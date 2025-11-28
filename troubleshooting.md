# Troubleshooting

## Error index
- Exact error strings with resolutions

## Playbooks
- Step-by-step fix procedures

## Diagnostics
- Commands and logs to collect

---

## Coins Jar Validation

### Invite Success Validation
| Issue | Cause | Resolution |
|-------|-------|------------|
| Invite not counted | Invitee left before 30 seconds | Inform user of 30-second minimum stay |
| Duplicate invite rejected | Same invitee within 24 hours | Wait 24 hours or invite different user |
| Self-invite rejected | Same device ID detected | Cannot invite self; use different user |
| New user check failed | User registered > 7 days ago | Target users registered within 7 days |

### Mic Presence Validation
| Issue | Cause | Resolution |
|-------|-------|------------|
| Time not accumulating | User left room (reset on leave) | Rejoin room to restart timer |
| Concurrent session rejected | Multiple mic sessions detected | End other sessions before starting new |
| Muted time not counting | (Should count) | Check for app bug; muted mic allowed |

### Anchored-Day Counter Issues
| Issue | Cause | Resolution |
|-------|-------|------------|
| Counter not reset | Timezone mismatch | Verify user's local timezone |
| Progress lost | Day boundary crossed | Progress resets at local midnight; expected |
| Counter shows yesterday | Clock sync issue | Force refresh or clear cache |

### Task Completion Failures
| Error | Description | Resolution |
|-------|-------------|------------|
| `TASK_NOT_FOUND` | Invalid task ID | Check task catalog for valid IDs |
| `ALREADY_COMPLETED` | Task already claimed today | Wait for daily reset |
| `TARGET_NOT_MET` | Progress below target | Complete remaining requirements |
| `RATE_LIMITED` | Too many claims per hour | Wait 1 hour before retrying |

---

## VIP Scaling Checks

### Daily Reward Scaling
| Issue | Verification | Resolution |
|-------|--------------|------------|
| Wrong multiplier applied | Check VIP tier vs multiplier | Verify tier in user profile |
| Bonus not showing | VIP expired | Renew VIP subscription |
| Partial bonus | VIP changed mid-day | Multiplier at claim time is used |

### VIP Multiplier Reference
| VIP Tier | Daily Reward Multiplier | Gifting EXP Multiplier |
|----------|------------------------|------------------------|
| VIP1 | 1.20x | 1.2 EXP per 5 coins |
| VIP5 | 2.00x | 2.0 EXP per 5 coins |
| VIP10 | 3.00x | 3.0 EXP per 5 coins |

See [VIP Multipliers](data/vip-multipliers.json) for complete tier reference.

### Gifting EXP Calculation Verification
1. Calculate base EXP: `gift_amount / 5`
2. Apply VIP bonus: `base_EXP + (base_EXP × VIP_additive_bonus)`
3. Verify total matches expected value

### Jar Task VIP Scaling
| Check | Expected |
|-------|----------|
| Jar coin rewards | Not scaled by VIP |
| Task targets | Fixed, not scaled |
| Host bonus | Fixed 5%, not scaled |

---

## Diagnostic Commands

### Counter Status Check
```bash
# Check user's daily counter status
GET /debug/jar/counters/{userId}

# Force counter reset (admin only)
POST /debug/jar/reset/{userId}
```

### Task History
```bash
# Get task completion history
GET /debug/jar/history/{userId}?days=7

# Get host bonus history
GET /debug/jar/host-bonus/{userId}?days=30
```

### VIP Verification
```bash
# Check user's VIP status and multipliers
GET /debug/vip/{userId}

# Calculate expected reward
GET /debug/calculate-reward?amount={amount}&vipTier={tier}
```

See [Owner CMS](docs/owner-cms.md) for administrative controls and [Jar Rules](data/jar-rules.json) for rule definitions.