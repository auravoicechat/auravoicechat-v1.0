# Architecture

## High-level design
- Diagram: components and data flow (add image under docs/assets/)
- Key components and responsibilities

## Technology choices
- Runtimes, frameworks, storage, messaging
- Rationale and trade-offs (link to ADRs)

## Data model
- Schemas or ER diagrams
- Important constraints and relationships

## Error handling and resilience
- Retries, timeouts, circuit breakers, backoff strategies

## Scalability
- Horizontal/vertical scaling approaches
- Performance considerations and known bottlenecks

## Observability
- Logging, metrics, tracing standards and sinks

---

## Owner CMS & Coins Jar Architecture

The Owner CMS manages the Coins Jar feature and related economy mechanics. See [Owner CMS](docs/owner-cms.md) for complete documentation.

### Coins Jar Data Flow
1. User opens Jar → Fetch task status from server
2. User completes task → Validate against rules, update counters
3. Task completion → Credit coins, update host bonus (if applicable)
4. Day reset → Reset daily counters at local midnight

### Key Components
| Component | Responsibility |
|-----------|----------------|
| Task Service | Validates task completion, enforces rules |
| Counter Service | Tracks daily progress, handles resets |
| Reward Service | Credits coins, applies VIP bonuses |
| Host Bonus Service | Calculates and settles 5% host bonuses |
| Abuse Detection | Monitors for multi-account and bot activity |

### Data Storage
| Store | Data | TTL |
|-------|------|-----|
| Redis | Daily counters, session data | 48 hours |
| PostgreSQL | Task history, user progress | Permanent |
| Elasticsearch | Audit logs, analytics | 365 days |

### Counter Management
- **Daily counters**: Reset at user's local midnight (anchored day window)
- **Mic concurrency**: Real-time tracking with reset on leave
- **Host bonus**: Cumulative, settled in real-time

See [Jar Rules](data/jar-rules.json) for operational rules and [Jar Tasks](data/jar-tasks.json) for task catalog.