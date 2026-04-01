# Workout App — Design Spec
**Date:** 2026-04-01  
**Stack:** Android (Kotlin), Jetpack Compose, MVVM, Room, Retrofit

---

## 1. Architecture

**Pattern:** MVVM + Repository, Single Activity, Jetpack Compose + Navigation Compose.

### Project Structure

```
ru.nsu.salina/
├── ui/
│   ├── theme/              # Color.kt, Typography.kt, Shape.kt, Theme.kt
│   ├── components/         # Reusable composables
│   ├── screens/
│   │   ├── workouts/       # WorkoutsScreen+VM, ExecutionScreen+VM,
│   │   │                   # ContraindicationsScreen+VM, CompletionScreen
│   │   ├── calendar/       # CalendarScreen + CalendarViewModel
│   │   └── profile/        # ProfileScreen (stub)
│   └── navigation/         # AppNavigation.kt, NavRoutes.kt
├── domain/model/           # Plan, DayPlan, Exercise, CompletedWorkout (pure data classes)
└── data/
    ├── local/              # AppDatabase, DAOs, Entity classes
    ├── remote/             # ApiService (Retrofit), DTO classes
    └── repository/         # TrainingRepository interface + Impl
```

### Dependencies to add
- Compose BOM + Material 3
- Navigation Compose
- ViewModel Compose
- Room + KSP
- Retrofit + Gson converter
- Coroutines

---

## 2. Data Layer

### Room Tables

| Table | Columns |
|---|---|
| `active_plan` | id, name, currentDay (1–14), isCompleted, restDayShownDate (String?) |
| `day_plans` | id, planId, day, focus |
| `exercises` | id, dayPlanId, name, technique, repetitions (Int?), duration (Int?) |
| `completed_workouts` | id, date (String "yyyy-MM-dd"), dayNumber |

`restDayShownDate` stores the calendar date when a rest day card was last shown, used to auto-advance the next calendar day.

### API (Retrofit, base URL: http://localhost:8080)

- `GET /contraindications` → `List<ContraindicationDto>` (id, name)
- `GET /training-plan?contraindication_ids=1&contraindication_ids=2` → `List<DayPlanDto>` or 422

**422 response:**
```json
{ "error": "422", "message": "Не удалось сформировать тренировочный план с учетом указанных противопоказаний." }
```

### Repository Interface

```kotlin
interface TrainingRepository {
    fun getActivePlan(): Flow<ActivePlan?>
    fun getCompletedDates(): Flow<List<LocalDate>>
    suspend fun getContraindications(): Result<List<Contraindication>>
    suspend fun generateAndSavePlan(contraindicationIds: List<Int>): Result<Unit>
    suspend fun completeCurrentDay()       // currentDay++, saves CompletedWorkout with today's date
    suspend fun finishActivePlan()         // isCompleted = true
    suspend fun checkAndAdvanceRestDay()   // if restDayShownDate == yesterday → currentDay++
}
```

Mapping: DTOs → domain models in Repository; Entity ↔ domain models via extension functions.

---

## 3. Navigation & Screens

### Bottom Bar
3 tabs: Тренировки (`ic_trains`), Календарь (`ic_calendar`), Профиль (`ic_profile`).  
Icons loaded via `painterResource` from existing XML drawables.

### Nav Routes

```
workouts                    ← bottom tab root
workouts/detail             ← bottom sheet over workouts
workouts/execution          ← full screen
workouts/contraindications  ← full screen
workouts/loading            ← full screen
workouts/completion         ← full screen (used for day completion AND plan completion)
calendar                    ← bottom tab root
profile                     ← bottom tab root
```

### Screen Responsibilities

| Screen | Responsibility |
|---|---|
| `WorkoutsScreen` | Active plan block (if any) + "Другие планы" section |
| `WorkoutDetailSheet` | Bottom sheet: today's exercise list (name + reps/duration only) |
| `WorkoutExecutionScreen` | One exercise at a time: name, technique, reps/duration. Buttons: "Следующее" / "Пропустить" |
| `CompletionScreen` | Congratulations. Two modes: day complete (returns to workouts) and plan complete (also returns to workouts, removes active plan block) |
| `ContraindicationsScreen` | Search bar + multi-select list of contraindications, "Далее" button |
| `PlanLoadingScreen` | Full-screen spinner while backend generates plan |
| `CalendarScreen` | Monthly calendar, current day highlighted, completed days marked with a dot |
| `ProfileScreen` | Stub |

### ViewModels

- `WorkoutsViewModel` — active plan state, available plans, triggers rest day check on launch
- `ExecutionViewModel` — exercise list for current day, currentIndex, skip/next logic
- `ContraindicationsViewModel` — fetch list, search query, selected ids, trigger plan generation
- `CalendarViewModel` — load completed dates from Room

`PlanLoadingScreen` and `CompletionScreen` share state via `ContraindicationsViewModel` and `WorkoutsViewModel` respectively (no own VM needed).

---

## 4. UI Theme & Components

### Theme (Material 3)

```kotlin
// Color.kt
val PrimaryBlue     = Color(0xFF3A99E6)   // buttons, primary accents
val SecondaryPurple = Color(0xFFCF85DE)   // secondary elements
val SurfaceBlue     = Color(0xFFCFDDFF)   // cards, chips background
val Background      = Color(0xFFFFFFFF)

// Shape.kt
val CardShape   = RoundedCornerShape(16.dp)
val ButtonShape = RoundedCornerShape(12.dp)
val ChipShape   = RoundedCornerShape(50)

// Typography.kt — system Roboto font
// Defines: titleLarge, titleMedium, bodyMedium, labelSmall
```

### Reusable Components (`ui/components/`)

| File | Contents |
|---|---|
| `PlanCard.kt` | Plan card: name, duration in days, "Начать" button. Plan name is hardcoded in the app (single available plan: e.g. "14-дневный курс") since the API does not return a plan name. |
| `ActivePlanBlock.kt` | Current plan block: name, current day, "Детали" + "Начать"/"Выполнено"/"Отдых" button |
| `ExerciseListItem.kt` | Exercise row in list (name + reps/duration, no technique) |
| `ExerciseDetailCard.kt` | Full exercise card with technique for execution screen |
| `ContraindicationChip.kt` | Selectable chip for contraindications multi-select |
| `AppBottomBar.kt` | Bottom navigation bar with 3 tabs |
| `LoadingScreen.kt` | Full-screen centered spinner |

---

## 5. Business Logic & Key Flows

### Start New Plan
1. User taps "Начать" on a plan card in "Другие планы"
2. If active plan exists → `finishActivePlan()`
3. Navigate to `ContraindicationsScreen`
4. User selects contraindications → tap "Далее"
5. Navigate to `PlanLoadingScreen`
6. `GET /training-plan?contraindication_ids=...`
   - **200** → save plan to Room → `navigate(workouts)` + Snackbar "План успешно создан"
   - **422** → `navigate(workouts)` + Snackbar with error message from response

### Execute Workout
1. "Начать" on active plan block → `navigate(execution)` passing current day exercises
2. "Следующее" / "Пропустить" → `currentIndex++`
3. After last exercise → `navigate(completion)` with mode = DayComplete
4. "Готово" on CompletionScreen → `completeCurrentDay()` (saves date, increments currentDay) → `navigate(workouts)`
5. If after increment `currentDay > 14` → show CompletionScreen with mode = PlanComplete, then finish plan and return to workouts

### Rest Day Handling
- On `WorkoutsScreen` launch: call `checkAndAdvanceRestDay()`
  - If `restDayShownDate == yesterday` → `currentDay++`
- If current day's focus == "Отдых":
  - Mark `restDayShownDate = today` (if not already today)
  - Show rest day card in active plan block

### Current Day Button States

| Situation | Button |
|---|---|
| Normal day, not yet completed | "Начать" — active, blue (PrimaryBlue) |
| Day already completed today | "Выполнено" — disabled |
| Rest day | "Отдых" — disabled, grey |

### Calendar
`CalendarViewModel` loads `Set<LocalDate>` of completed workout dates from Room.  
Completed days are marked with a colored dot (PrimaryBlue) below the date number.  
Current calendar date is highlighted with a filled circle background (SurfaceBlue).

---

## 6. Error Handling

| Scenario | Handling |
|---|---|
| `/contraindications` network error | Show error message on ContraindicationsScreen, retry button |
| `/training-plan` 422 | Navigate back to workouts, show error Snackbar |
| `/training-plan` network error | Navigate back to workouts, show generic error Snackbar |
| Room read/write error | Not expected; propagate as crash (internal error) |
