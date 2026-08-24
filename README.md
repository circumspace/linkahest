# Linkahest

[![License: Apache 2.0](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://www.android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-purple.svg)](https://kotlinlang.org)

Linkahest cleans shared links before they leave your phone. Send YouTube, Twitter/X, Reddit, Medium, and other URLs through it to strip trackers or open them through alternative frontends.

Link transformation runs locally and history stays off unless you turn it on. Network access is used only when you explicitly check alternative-frontend availability in Settings.

![Home screen](docs/screenshots/main-screen.png)

## Features

- Appears in Android's share menu.
- Removes common tracking parameters from shared links.
- Detects redirect links and extracts the real destination when possible.
- Supports alternative frontends for YouTube, Twitter/X, Reddit, and Medium.
- Lets you pick built-in frontend instances or enter your own.
- Checks built-in and custom frontend availability on demand.
- Keeps transformed-link history disabled by default.
- Supports light, dark, and system themes.

## Additional Screenshots

| Transform Link | Result | Settings |
|:--------------:|:------:|:--------:|
| ![Transform link](docs/screenshots/transform-link.png) | ![Transformed result](docs/screenshots/transform-link-final.png) | ![Settings](docs/screenshots/settings-new.png) |

## Installation

### Zapstore

Linkahest is available on [Zapstore](https://zapstore.dev/apps/com.hermeticvm.linkahest).

### Obtainium or APK

Add the [GitHub releases page](https://github.com/circumspace/linkahest/releases) to Obtainium, or download the APK manually.

## Releases

Pull requests and pushes to `main` run unit tests, lint, and a debug build. Pushing a tag that matches the app version, such as `v0.6.0`, builds signed APK and AAB artifacts and publishes a prerelease.

The release workflow requires these GitHub Actions secrets:

- `SIGNING_KEYSTORE_BASE64`
- `SIGNING_STORE_PASSWORD`
- `SIGNING_KEY_ALIAS`
- `SIGNING_KEY_PASSWORD`

The workflow verifies the signing certificate used by existing releases before publishing, preventing an incompatible update from being released accidentally.

## Donate

If Linkahest is useful to you, donations help keep maintenance moving:

**Bitcoin:** `bc1qjt5n267ka8zuagtmrurez9vjs43hlg3qkqc8sc`

**Bitcoin Lightning:** [hermeticvm@minibits.cash](lightning:hermeticvm@minibits.cash)

**Monero:** `8AuPVyudY9hRedjkRzCisrDq5rnzbUvCTckcQr5dUaGWa1yzo77uMUP8LPpSQvPBbGEktHpPqkHFPdXuCYBEL6iz9kXAhFW`
