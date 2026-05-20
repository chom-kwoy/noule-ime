# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Noule is an Android Input Method Editor (IME) supporting Korean (Hangul/Hanja), Chinese (Cangjie), Manchurian script, English, and symbols. Written in Java, minSdk 24, targetSdk 36.

## Build Commands

```bash
./gradlew assembleDebug            # Build debug APK
./gradlew assembleRelease          # Build release APK
./gradlew test                     # Run unit tests
./gradlew connectedAndroidTest     # Run instrumented tests on device
./gradlew build                    # Full build (compile + test)
```

Install and test on a connected device:
```bash
./gradlew installDebug
```

## Architecture

### Entry Points

- **`NouleIME.java`** — The `InputMethodService` subclass. Initializes language data asynchronously on creation, manages keyboard lifecycle (`onCreateInputView`, `onStartInput`, `onUpdateSelection`), listens for preference changes.
- **`NouleIMESettings.java`** — Launcher activity; settings UI via `PreferenceFragmentCompat`.

### Main Keyboard View

**`NouleKeyboardView.java`** (extends `ConstraintLayout`) manages three layouts:
- `EN_LAYOUT` — English
- `KO_LAYOUT` — Korean (Hangul/Hanja)
- `SPECIAL_LAYOUT` — Symbols/special chars

It handles touch input, key repeat, haptic feedback, composing text state, and suggestion display. Theme customization (colors, button styles) is applied here. Suggestions are rendered via `SuggestionAdapter` (RecyclerView).

### Language Processing (`lang/` package)

| Class | Responsibility |
|---|---|
| `HangulData.java` | Hangul composition/decomposition, Unicode normalization. Contains static maps: `consInfoMap`, `vowelInfoMap`, `composeMap`, `toCompat`, and jamo character sets. |
| `HanjaDict.java` | Two `PatriciaTrie`-backed dictionaries (Hanja and Cangjie), loaded asynchronously from assets. Prefix-match for suggestions. |
| `ManchuData.java` | Romanization-to-Manchurian script conversion, detection of Manchurian text. |
| `SymbolData.java` | Symbol/special character data loaded from assets. |

### Assets (Data Files)

Located in `app/src/main/assets/`:
- `hanja.txt`, `freq-hanja.txt`, `freq-hanjaeo.txt` — Hanja dictionaries
- `compat-table.txt`, `Unihan_DictionaryLikeData.txt` — Unicode compatibility data
- `mssymbol.txt` — Symbol data

### Key Dependencies

- `com.github.skydoves:colorpickerview` — Color picker in settings
- `org.apache.commons:commons-collections4` — `PatriciaTrie` used in `HanjaDict`
