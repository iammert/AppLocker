# Changelog

## 2.0.0

The project builds again with current tools and targets Android 16. minSdk is now 23.

### Privacy and security
- Removed ads, Firebase Analytics, the Facebook SDK, Crashlytics, Bugsnag and Stetho
- The lock pattern is stored as a salted PBKDF2 hash. Existing patterns are converted by the
  database migration to version 5.
- Pattern input is blocked for 30 seconds after five wrong attempts
- App data is excluded from cloud backups and device transfer
- The lock screens, the vault, the intruder gallery and the browser are hidden from
  screenshots and the recent apps preview
- Vault files and intruder photos are stored in app specific storage instead of
  `/sdcard/applocker`. Existing files are moved on the first start.
- Unencrypted previews are no longer left in the cache
- The private browser blocks file and content access and mixed content, enables Safe Browsing
  and clears its data when it is closed

### Fixes
- Originals added to the vault are removed from the gallery on Android 11 and later
- Every screened call is answered, also when two calls come in at the same time
- The call blocker asks for the call screening role on Android 10 and later
- The front camera is released right after the intruder photo is taken
- Fragments observe LiveData with the view lifecycle
- "Lock all apps" ignores apps that were uninstalled
- The notification channel has a real name and notifications are posted once
- Crash with apps that have a styled launcher label

### Performance
- The foreground app detection queries usage events incrementally and checks usage access
  every five seconds instead of every 100 ms
- File encryption uses a 64 KB buffer and reports progress only when it changes
- The app list is sorted in linear time and the settings screen no longer loads app icons
- Room and Dagger are processed with KSP

### Android versions
- Targets Android 16: exported components, immutable PendingIntents, package visibility,
  notification and media permissions, a `specialUse` foreground service, edge-to-edge and
  predictive back
- Fingerprint unlock uses BiometricPrompt
- Per-app language settings on Android 13 and later

### Project
- AGP 9.4, Gradle 9.6, Kotlin 2.4 and a version catalog
- PatternLockView is included as a module, because it is no longer published
- Unit tests, GitHub Actions and Dependabot
