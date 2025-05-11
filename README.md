# ♛ Chess N-Queens Puzzle App

An Android application for solving the classic N-Queens puzzle, built using **Jetpack Compose**, **Kotlin**, and the **MVVM architecture**.

---

## 📱 Overview

This app challenges users to solve the N-Queens problem — placing N queens on an N×N chessboard such that no two queens threaten each other.

### ✅ Features

- Dynamic N×N chessboard UI
- Game logic via core `GameEngine`
- Real-time conflict highlighting
- Timer, click counter, pause/resume
- Persistent settings (board size & theme)
- Local leaderboard
- Sound feedback & splash screen

---

## 🧱 Architecture

The app uses **MVVM** and Clean Architecture principles. Each layer has a clear responsibility:

### 🔄 MVVM + Clean Architecture

- **View**: Composables (UI) observe state from ViewModel
- **ViewModel**: Handles UI logic, transforms state, emits events
- **UseCase**: Business rules as single-responsibility classes
- **Repository**: Abstracts access to local data (Room, preferences)

---

### 📦 Project Structure

```
com.ejoe.chess
├── data                        # Data layer
│   ├── local
│   │   ├── dao                # Room DAO interfaces
│   │   ├── entity            # Room entities
│   │   └── sharerpreferences # Settings via SharedPreferences
│   ├── mapper                 # Domain-to-entity mapping
│   └── repository             # Repository implementations
├── di                         # Dependency injection (Koin modules)
├── domain                     # Business logic layer
│   ├── logic                  # Game logic (GameEngine, CellPosition)
│   ├── model                  # App models (GameResult, GameSettings)
│   ├── repository             # Abstract repo interfaces
│   └── usecase                # Business actions (Get/Save use cases)
├── presentation               # UI layer
│   ├── components             # Reusable UI (e.g., ChessButton)
│   ├── game                   # Game screen, state & logic
│   ├── leaderboard            # Leaderboard screen/view model
│   ├── menu                   # Main menu screen/view model
│   ├── navigation             # Navigation graph & routes
│   ├── settings               # Game settings screen/view model
│   ├── splash                 # Splash screen loader
│   └── ui.theme               # Colors, typography, theme
├── util.sound                 # Sound playback helpers
├── MainActivity.kt            # App entry point
├── ChessApp.kt                # Application class
└── AndroidManifest.xml
```

### 🧪 Test Structure

```
test/
└── java/
    └── com.ejoe.chess
        └── GameEngineTest.kt    # Unit tests for GameEngine
```

---

## 🚀 Running the App

To build and launch the app:

1. Clone the repo:
   ```bash
   git clone https://github.com/e-Joe/chess.git
   ```
2. Open the project in **Android Studio Meerkat or newer**
3. Sync Gradle
4. Run the app on an emulator or physical device

> Minimum SDK: 26  
> Target SDK: 35

---

## 🧪 Running Tests

The `GameEngine` logic is fully unit tested.

### Via Android Studio:
- Right-click on `GameEngineTest.kt` > Run 'GameEngineTest'

### Or via terminal:
```bash
./gradlew test
```

---

## 📥 Download

You can download and install the app directly via **AppCircle**:

🔗 **[Download Chess N-Queens Puzzle App](https://dist.appcircle.io/home/static-profile?profileId=fb12f242-4060-4641-902d-0f4158c0c6fb)**

---

## 💡 Improvements To Consider

- Add UI tests for GameScreen and ViewModels
- Include transition and board placement animations
- Implement a solver for solution hints
- Add remote leaderboard with Firebase/Backend

---

## 👤 Author

Ilija Vucetic  
📧 vuceticilija@gmail.com  
🔗 [github.com/e-Joe](https://github.com/e-Joe)
