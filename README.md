# Linkahest Android App

A privacy-focused Android app that transforms social media links to selfhostable alternative frontends before sharing them. Built with Kotlin and Jetpack Compose. Vibe coded with passion and [goose](https://github.com/block/goose).

## Features

Sharing links to content (posts, threads, comments, videos) from the following commercial, privacy-invasive platforms will be transformed to community-hosted/self-hosted, privacy-preserving alternatives:

- 🔗 **YouTube**
  - Clean YouTube URLs (remove tracking parameters)
  - Convert to **Invidious** instances for privacy
  
- 🐦 **Twitter/X**
  - Convert Twitter/X links to **Nitter** instances
  
- 🐦 **Reddit**
  - Convert Reddit links to Redlib instances

- 📱 **Seamless Integration**
  - Appears in Android share sheet
  - Material Design 3 interface
  
- 🔒 **Privacy First**
  - Works completely offline
  - No cloud sync
  - No data collection
  - Local database only


## How to Use

1. Share any YouTube or Twitter/X/Reddit link from any app
2. Select "Linkahest" from the share menu
3. Choose your preferred transformation
4. Share the transformed link to other apps

## Link transformations

### YouTube
- **Strip tracking identifier**: `youtube.com/watch?v=ID&si=tracking` → `youtube.com/watch?v=ID`
- **Invidious**: `youtube.com/watch?v=ID` → `yewtu.be/watch?v=ID`

### Twitter/X
- **Nitter**: `twitter.com/user/status/ID` → `nitter.net/user/status/ID`

### Reddit
- **Redlib**: `reddit.com/r/subreddit/comments/postID` → `rl.bloat.cat/r/subreddit/comments/postID`

## Building the App

### Prerequisites

- Android Studio Arctic Fox or newer
- JDK 8 or newer
- Android SDK API 34

### Build Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Install to connected device
./gradlew installDebug

# Build release APK (unsigned)
./gradlew assembleRelease
```

### Generated APK Location

Debug APK: `app/build/outputs/apk/debug/app-debug.apk`
Release APK: `app/build/outputs/apk/release/app-release-unsigned.apk`

## Project Structure

```
app/src/main/java/com/hermeticvm/linkahest/
├── ui/
│   ├── screens/           # Compose screens
│   ├── components/        # Reusable UI components  
│   └── theme/            # Material Design 3 theme
├── data/
│   ├── database/         # Room database components
│   ├── repository/       # Data access layer
│   └── models/          # Data models
├── domain/
│   ├── usecases/        # Business logic
│   └── transformers/    # URL transformation logic
├── MainActivity.kt
└── ShareReceiverActivity.kt
```

## Architecture

- **MVVM** pattern with Repository
- **Jetpack Compose** for UI
- **Room** for local storage
- **Material Design 3** components
- **Kotlin Coroutines** for async operations

## Testing

You can test the app by:

1. Installing the debug APK on your device
2. Sharing any YouTube, Twitter or Reddit link from a browser
3. Selecting Linkahest from the share options

## Development Notes

- Minimum SDK: API 24 (Android 7.0)
- Target SDK: API 34 (Android 14)
- Uses Material 3 dynamic theming
- Supports both light and dark themes

## TODO List

### Functionality
- [x] Settings screen for custom instances
- [ ] History management UI
- [x] Additional social platforms
- [x] Custom URL patterns

### Code Signing & Distribution
- [ ] Figure out how to do codesigning
- [ ] Automate codesigning via script, makefile or function associated with git pushes
- [ ] Research how to publish to Zapstore
- [ ] Test released artifact installation with Obtainium

### Branding & Documentation
- [x] Rename app in code dependent upon final naming decision
  - Final name: **Linkahest** ✅
- [ ] Update readme and dev docs (features, functionality, name)

### Development Infrastructure
- [ ] Mirror code to github for issue tracking & contributions