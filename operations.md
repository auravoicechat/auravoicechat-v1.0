# Operations

## Monitoring
- Key metrics and thresholds

## Logging
- Log levels, formats, sinks
- PII handling

## Alerts
- Alert rules and on-call expectations

## Runbooks
- Incident response steps for common failure modes

---

## Coins Jar Operations

### Daily Reset Monitoring
| Metric | Threshold | Alert |
|--------|-----------|-------|
| Reset completion rate | < 99% | Warning |
| Reset latency | > 5s | Warning |
| Failed resets | > 100/hour | Critical |

### Counter Health Checks
| Check | Interval | Action on Failure |
|-------|----------|-------------------|
| Redis counter sync | 1 minute | Reconcile from DB |
| Day boundary alignment | 5 minutes | Force reset |
| Host bonus settlement | Real-time | Queue for retry |

### Task Completion Metrics
| Metric | Description |
|--------|-------------|
| jar_task_completions | Tasks completed per type per hour |
| jar_coins_distributed | Total coins awarded via Jar |
| jar_host_bonus_total | Host bonus coins settled |
| jar_validation_failures | Task validation failures |

### Daily Counter Reset Procedure
1. Identify user's local timezone
2. Calculate reset timestamp (local midnight)
3. Reset all daily counters atomically
4. Log reset event with user ID and timestamp
5. Trigger notification if unclaimed rewards exist

### VIP Scaling Verification
| Check | Expected Result |
|-------|-----------------|
| Daily reward multiplier | Matches VIP tier |
| Gifting EXP bonus | Additive per VIP tier |
| Jar task rewards | Not scaled by VIP |

See [Owner CMS](docs/owner-cms.md) for control panel operations and [Jar Rules](data/jar-rules.json) for operational rules.