# Harmonie — Workout App

An Android app for following structured 14-day workout plans with exercise-by-exercise guidance.

## Features

- **Workouts screen** — shows active plan progress, today's plan details, and available plans to start
- **Exercise execution** — step-by-step exercise cards with technique, reps/duration, Skip and Next buttons
- **Contraindications survey** — multi-select list with search before starting a plan; sent to the backend to tailor the plan
- **Plan loading** — shows progress while the backend generates a 14-day plan
- **Completion screen** — day completion and full-plan congratulations variants
- **Calendar** — monthly grid with completed workout days marked
- **Rest day handling** — rest days are shown for one day, then auto-advanced on next app open

## Tech Stack

| Layer | Technology |
|---|---|
| UI | Jetpack Compose + Material 3 |
| Navigation | Navigation Compose |
| State | ViewModel + StateFlow |
| Local DB | Room (with KSP) |
| Network | Retrofit + Gson |
| Async | Kotlin Coroutines |
| Java time on API 24 | core library desugaring |

## Project Structure

```
app/src/main/java/ru/nsu/salina/
├── data/
│   ├── local/
│   │   ├── dao/          # Room DAOs
│   │   ├── entity/       # Room entities
│   │   ├── AppDatabase.kt
│   │   └── Mappers.kt    # Entity ↔ domain conversions
│   ├── remote/
│   │   ├── ApiService.kt
│   │   └── RetrofitClient.kt
│   └── repository/
│       ├── TrainingRepository.kt      # interface
│       └── TrainingRepositoryImpl.kt
├── domain/model/
│   └── Models.kt         # domain data classes
├── ui/
│   ├── components/       # shared composables (ActivePlanBlock, AppBottomBar, …)
│   ├── navigation/       # NavRoutes, AppNavigation
│   ├── screens/
│   │   ├── calendar/
│   │   ├── profile/
│   │   └── workouts/     # Workouts, Execution, Contraindications, PlanLoading, Completion
│   └── theme/            # Color, Typography, Shape, Theme
├── HarmonieApp.kt        # Application subclass (DI root)
└── MainActivity.kt
```

## Prerequisites

- Android Studio Meerkat or newer
- JDK 11+
- Backend running at `http://localhost:8080` (mapped to `10.0.2.2:8080` inside the emulator)

## Running

1. Clone the repository and open the project in Android Studio.
2. Start the backend server on your host machine at port `8080`.
3. Create an AVD (API 24+) or connect a physical device.
4. Run the app (`Shift+F10`).

## API

The app talks to two endpoints:

| Endpoint | Description |
|---|---|
| `GET /contraindications` | Returns list of `{id, name}` contraindications |
| `GET /training-plan?contraindication_ids=…` | Generates a 14-day plan; returns 422 if constraints are too restrictive |

Cleartext HTTP to `10.0.2.2` and `localhost` is explicitly permitted via `res/xml/network_security_config.xml`.
