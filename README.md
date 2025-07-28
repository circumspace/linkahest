# Linkahest

[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://www.android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-purple.svg)](https://kotlinlang.org)

A privacy-focused Android app written in Kotlin that transforms social media links to selfhostable alternative frontends before sharing them. First released under GPL v3 in 2025.

<div align="center">
  <img src="linkahest_icon_512.png" alt="Linkahest App Icon" width="128" height="128">
</div>`

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

## Screenshots

| Main Screen | Share from YouTube | Choose Option | Share Transformed | Custom Instances |
|:-----------:|:------------------:|:-------------:|:-----------------:|:----------------:|
| ![main](mainscreen-preview.png) | ![share](share-from-yt.png) | ![option](share-new-link.png) | ![transform](share-sheet.png) | ![instances](custom-instances.png) |


## Installation

### Zapstore (Recommended)
Linkahest is available on [Zapstore](https://zapstore.dev/):
```
Search for "Linkahest" in Zapstore (on Android)
```

### Obtainium (or direct APK download)
Add [release link](https://github.com/circumspace/linkahest/releases) to Obtainium or install APK manually

### Build from Source
```bash
git clone https://github.com/circumspace/linkahest.git
cd linkahest
./gradlew assembleDebug
./gradlew installDebug
```

## Quick Start

1. **Install the app** from Zapstore or download the APK
2. **Share any link** from YouTube, Twitter/X, or Reddit
3. **Select "Linkahest"** from the share menu
4. **Choose transformation** (clean URL, Invidious, Nitter, or Redlib)
5. **Share the transformed link** to other apps

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
Release APK: `app/build/outputs/apk/release/app-release-unsigned.apk` (**NOTICE**: Release APs are untested as of yet)

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

## Contributing

We welcome contributions! Here's how to get started:

### Development Setup
```bash
# Clone the repository
git clone https://github.com/circumspace/linkahest.git
cd linkahest

# Open in Android Studio
# Or build from command line
./gradlew assembleDebug
```

### Contribution Process
1. **Fork the repository**
2. **Create a feature branch**: `git checkout -b feature/amazing-feature`
3. **Make your changes** following the existing code style
4. **Test your changes** on a real device or emulator
5. **Commit with clear messages**: `git commit -m 'Add amazing feature'`
6. **Push to your branch**: `git push origin feature/amazing-feature`
7. **Open a Pull Request** with a clear description

### Code Style
- Follow standard Kotlin conventions
- Use meaningful variable and function names
- Add comments for complex logic
- Ensure the app builds without warnings

### Reporting Issues
- **Bug reports**: Use [GitHub Issues](https://github.com/circumspace/linkahest/issues)
- **Feature requests**: Open an issue with the "enhancement" label
- **Security issues**: Email [security@circumspace.com](mailto:security@circumspace.com)

## Authors

- **hermeticvm** - Initial work and primary maintainer - [circumspace](https://github.com/circumspace)

## License

This project is licensed under the GNU General Public License v3.0 - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- Built with [Jetpack Compose](https://developer.android.com/jetpack/compose)
- Uses [Material Design 3](https://m3.material.io/) components
- Cypherpunks, nostriches and other freedom tech people
