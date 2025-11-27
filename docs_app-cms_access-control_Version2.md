# App CMS — Access Control

Roles
- APP_OWNER: full write + publish
- ADMIN: write; publish where delegated
- CONTENT_MANAGER: assets/copy/order only
- ANALYST: read-only

Security
- SSO + mandatory 2FA
- Fine-grained scopes (VIP ladders, wheels, slider)
- Optional IP allowlist, signed config artifacts