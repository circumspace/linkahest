# Project Status

Last updated: 2026-05-13

## Current Release State

- Current app version in development: `0.5.1` (`versionCode 10`).
- Latest published GitHub release: `v0.5.0`.
- Project license: Apache-2.0.
- Main development branch: `main`.
- The settings and privacy quality pass has been merged.

## App Scope

Linkahest is an Android share-target app for cleaning and transforming links before sharing them onward. It strips common tracking parameters and can redirect supported services through alternative frontends.

Supported service transformations currently include:

- YouTube to Invidious
- Twitter/X to Nitter
- Reddit to Redlib
- Medium to Scribe

The app remains offline-only and does not request network permission.

## User-Facing Features

- Android share sheet integration.
- Universal tracking parameter cleanup.
- Cloaked redirect URL extraction where possible.
- Configurable frontend instances.
- Optional transformation history, disabled by default.
- Light, dark, and system theme selection.
- Refreshed Material Expressive UI treatment.
- Refreshed launcher and store icon assets.

## Privacy Defaults

- Transformation history is opt-in.
- Turning history off clears stored history.
- No backend availability checks run inside the app.
- No network permission is declared.

## Build Notes

- Gradle/AGP compatibility currently relies on `android.builtInKotlin=false` and `android.newDsl=false` in `gradle.properties`.
- Those flags produce deprecation warnings but are still required with the current plugin/dependency setup.
- Debug builds are installable with `./gradlew installDebug`.
- Release builds currently produce an unsigned APK unless signing is configured separately.

## Metadata And Assets

- Runtime Android resources live under `app/src/main/res/`.
- Source icon assets live under `assets/icons/`.
- README screenshots live under `docs/screenshots/`.
- Store/readme image assets live under `docs/images/` when needed.
- Zapstore metadata is maintained in `zapstore.yaml`.
