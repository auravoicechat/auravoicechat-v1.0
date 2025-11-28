# Getting Started

## Basic Onboarding Flow

### Overview
Upon first launch, Aura Voice Chat presents a streamlined onboarding experience to authenticate users and establish the app’s visual identity.

### Theme and Branding
- App theme colors: Purple and White gradient (custom background)
- Preloader: Displays Aura Voice Chat logo (see Logo Specification)
- Login screen: Gradient background, official logo at the top, authentication options at the bottom

### Design Tokens
- Primary Purple: `#c9a8f1`
- Primary White: `#ffffff`
- Background Gradient:
  - Direction: Top-to-bottom (recommended) or diagonal
  - Example CSS (reference):
    ```css
    background: linear-gradient(180deg, #c9a8f1 0%, #ffffff 100%);
    ```
  - Android XML (example):
    ```xml
    <gradient
        android:startColor="#c9a8f1"
        android:endColor="#ffffff"
        android:angle="270" />
    ```
- Accessibility: Maintain AA contrast for text over lighter portions of gradient (use #2E2E2E or #1A1A1A for dark text if not white).

### Logo Specification
![Aura Voice Chat logo: neon microphone inside concentric techno rings on dark background with pink/purple/cyan accents](docs/assets/aura_logo.png)

(Reference image1 provided)

| Aspect | Guidance |
|--------|----------|
| Source Size | Master artwork recommended at 1024×1024 px (PNG). |
| Formats | PNG (raster), optional SVG if separable layers (for marketing), WebP for in-app performance. |
| Background | Dark techno grid with neon ring; treat as non-transparent. Provide an alternate transparent-layer version if adaptive icon separation is desired. |
| Safe Area | Keep circular neon ring and microphone glyph within 80% of canvas width/height to avoid clipping in adaptive masks. |
| Minimum Display Size | 48×48 dp (in lists). Below this, simplify or use a reduced “mic only” glyph. |
| Alt Text | “Aura Voice Chat logo: neon microphone inside concentric ring on dark, tech-accent background.” |
| Do / Don’t | Do maintain glow and ring integrity. Don’t recolor the microphone independently; avoid stretching or cropping ring. |

#### Android Launcher Icon (Adaptive)
If you produce adaptive icons:
- Foreground layer: Microphone + inner neon ring (trim background artifacts).
- Background layer: Solid or subtle gradient derived from `#c9a8f1` → `#5e4b85` (darker purple).  
- Provide mipmap assets:

| Density | Size (px) |
|---------|-----------|
| mdpi | 48 |
| hdpi | 72 |
| xhdpi | 96 |
| xxhdpi | 144 |
| xxxhdpi | 192 |

File suggestion:
```
mipmap-anydpi-v26/ic_launcher.xml
mipmap-anydpi-v26/ic_launcher_round.xml
mipmap-mdpi/ic_launcher.png
...
```

Adaptive XML example:
```xml
<?xml version="1.0" encoding="utf-8"?>
<adaptive-icon xmlns:android="http://schemas.android.com/apk/res/android">
    <background android:drawable="@color/ic_launcher_background"/>
    <foreground android:drawable="@mipmap/ic_launcher_foreground"/>
</adaptive-icon>
```

#### Palette Extraction (Approximate)
(Use for accent chips, loading spinner, highlight glows)
- Neon Fuchsia: `#ff34d9` (approx from glow)
- Vibrant Magenta: `#d958ff`
- Cyan Accent: `#35e8ff`
- Dark Canvas: `#12141a`
- Soft Glow White: `#e9f2ff`

### First-Run Sequence
1. Install the app from the Play Store or via APK.
2. Launch the app:
   - Preloader shows the logo centered (with subtle fade-in).
3. Login screen appears with:
   - Top: Logo
   - Background: Purple–white gradient
   - Bottom: Authentication options:
     - Login via Google
     - Login via Facebook
     - Login via Mobile (phone icon)

### Authentication Options
- Google Sign-In:
  - Tap “Login via Google”
  - Google account chooser returns session on success.
- Facebook Login:
  - Tap “Login via Facebook”
  - Facebook OAuth flow returns session on success.
- Mobile Login:
  - Tap phone icon
  - Begins phone number entry + OTP verification (details TBD).

### UI Requirements and Assets
| Element | Spec |
|---------|------|
| Preloader Duration | 0.8–1.2s or until initialization completes. |
| Logo Placement | Centered horizontally; top margin ~64dp from status bar inset. |
| Button Height | 48–56dp; corner radius 8dp. |
| Button Icons | Google, Facebook official; phone glyph (Material Icons `ic_call`). |
| Typography | Wordmark fallback: Roboto Medium 24sp (if logo text version needed). |
| Touch Targets | ≥ 44×44dp. |
| Spacing | 24dp vertical between auth buttons; 16dp min side padding. |

### Behavior and Edge Cases
- Preloader > 2s: Show spinner below logo.
- Offline at Login: Show banner “No internet. Retry.”
- Auth Failure: Toast/snackbar with provider-specific retry message.
- Existing Session: Skip login, navigate to Home.
- Denied Google/Facebook consent: Return to login with non-blocking error message.

### Permissions
- Microphone not requested at login; prompt upon first entry into a voice room or start of a call.
- If permanently denied, show rationale screen with “Open Settings” CTA.

### Analytics (Optional)
| Event | Properties |
|-------|------------|
| preloader_shown | duration_ms |
| login_screen_view | none |
| auth_attempt | provider, result(success|error), error_code(optional) |
| auth_method_switch | from_provider, to_provider |

### Accessibility
- Logo: Provide contentDescription for screen readers.
- Button Labels: Text + icon; ensure readable contrast on gradient (consider overlay scrim behind buttons if gradient is too light).
- Motion: Preloader animation subtle (avoid flashing neon pulse > 3 per second).

### Future Enhancements (placeholders)
- Guest Mode
- Passwordless email link login
- Dark Mode: Gradient variant (e.g., `#1a1524` → `#33264d`)
- Animated adaptive icon (limited usage)
