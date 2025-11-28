# Configuration

## Environment variables
| Name | Default | Required | Description |
|------|---------|----------|-------------|
| APP_PORT | 3000 | No | HTTP server port |
| ... | ... | ... | ... |

## Files
- config.yaml: schema and defaults
- Advanced tuning options

## Feature flags
- Names, defaults, safety notes

---

## Owner CMS Configuration

The Owner CMS provides administrative control over the Coins Jar feature, VIP scaling, and visibility settings. See [Owner CMS](docs/owner-cms.md) for complete documentation.

### Key Configuration Parameters
| Parameter | Default | Description |
|-----------|---------|-------------|
| CMS_SESSION_TIMEOUT | 30 min | Admin session idle timeout |
| CMS_MAX_CONCURRENT_SESSIONS | 3 | Maximum concurrent admin sessions |
| CMS_VERSION_RETENTION | 100 | Maximum versions retained per config type |

### Coins Jar Settings
| Parameter | Default | Description |
|-----------|---------|-------------|
| JAR_RESET_TIME | local_midnight | Daily task reset time |
| JAR_NEW_USER_DAYS | 7 | Days to qualify as new user |
| JAR_HOST_BONUS_PERCENT | 5 | Host bonus percentage |
| JAR_INVITE_SUCCESS_SECONDS | 30 | Seconds for invite success |

See [Jar Rules](data/jar-rules.json) for complete operational rules.