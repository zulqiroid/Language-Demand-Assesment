# Language Demand Assessment

A Kotlin Jetpack Compose prototype for a Language Demand Dashboard and resilient Call Provider Failover flow.

The application helps operational users identify high-demand languages, interpreter shortage risk, booking-type pressure, and call connectivity status across two mock call providers.

---

## Features

### Language Demand Dashboard

The dashboard displays operational demand information for languages, including:

- Language name
- Booking type
- Total requests
- Available interpreters
- Unassigned bookings
- Average waiting time
- Risk level: Green, Amber, or Red

### Booking Type Pressure

The app includes a separate booking type pressure section for:

- Face-to-Face
- Telephone
- Video Remote Interpreting
- BSL
- Translation

Each booking type displays:

- Total bookings
- Unassigned bookings
- Risk status

### Call Provider Failover

The app implements two mock call providers:

- Provider A: Primary provider
- Provider B: Backup provider

The call flow works as follows:

1. Provider A is attempted first.
2. If Provider A fails, times out, crashes, or becomes unavailable, Provider B is attempted automatically.
3. The user can see the current call status.
4. A final failed state is shown only when both providers fail.
5. Failover events are logged internally.

The UI also includes provider scenario controls so each provider can be tested with:

- Success
- Unavailable
- Rejected
- Crash
- Timeout

---

## Tech Stack

- Kotlin
- Jetpack Compose
- Material 3
- MVVM
- Clean Architecture
- Coroutines
- StateFlow
- Hilt Dependency Injection
- JUnit
- kotlinx-coroutines-test

---

## Architecture

The project follows Clean Architecture with clear separation between presentation, domain, and data layers.

```text
com.languageempire.assessment
├── core
│   ├── coroutine
│   ├── result
│   ├── time
│   └── designsystem
├── data
│   ├── provider
│   ├── repository
│   └── source
├── di
├── domain
│   ├── model
│   ├── provider
│   ├── repository
│   └── usecase
└── presentation
    ├── call
    └── dashboard
```

### Presentation Layer

Responsible for UI rendering and user interaction.

Main responsibilities:

- Compose screens and reusable UI components
- ViewModels
- UI state models
- UI mappers
- User actions

Important classes:

- `DashboardViewModel`
- `CallFailoverViewModel`
- `DashboardScreen`
- `CallFailoverCard`
- `DashboardUiState`
- `CallFailoverUiState`

### Domain Layer

Contains business rules and app logic.

Main responsibilities:

- Risk calculation
- Dashboard aggregation
- Booking type pressure calculation
- Call failover orchestration
- Repository contracts
- Provider contracts

Important classes:

- `CalculateRiskLevelUseCase`
- `CalculateBookingTypeStatsUseCase`
- `GetDashboardDataUseCase`
- `StartCallWithFailoverUseCase`
- `GetLanguageDemandUseCase`
- `UpdateCallProviderBehaviorUseCase`

### Data Layer

Provides mock/local implementations.

Main responsibilities:

- Local language demand data
- Mock call provider behavior
- In-memory provider scenario storage
- In-memory failover logging

Important classes:

- `LocalLanguageDemandDataSource`
- `MockProviderA`
- `MockProviderB`
- `InMemoryCallProviderScenarioDataSource`
- `InMemoryFailoverLogger`

---

## UI State Management

The app uses sealed classes for predictable UI state rendering.

### Dashboard States

```kotlin
sealed interface DashboardUiState {
    data object Loading : DashboardUiState
    data class Content(...) : DashboardUiState
    data class Error(...) : DashboardUiState
}
```

### Call Failover States

```kotlin
sealed interface CallFailoverUiState {
    data object Idle : CallFailoverUiState
    data class Connecting(...) : CallFailoverUiState
    data class RetryingWithBackupProvider(...) : CallFailoverUiState
    data class Connected(...) : CallFailoverUiState
    data class Failed(...) : CallFailoverUiState
}
```

Expected call states:

- Idle
- Connecting
- Retrying with backup provider
- Connected
- Failed

---

## Failover Flow

The failover use case emits a stream of call statuses.

Example flow when Provider A fails and Provider B succeeds:

```text
Connecting with Provider A
Provider A failed
Retrying with Provider B
Connecting with Provider B
Connected using Provider B
```

Example flow when both providers fail:

```text
Connecting with Provider A
Provider A failed
Retrying with Provider B
Connecting with Provider B
Provider B failed
Final failed state
```

Provider failures are handled safely:

- Timeout is mapped to `CallFailureReason.Timeout`
- Provider crash is mapped to `CallFailureReason.ProviderCrashed`
- Provider unavailable is mapped to `CallFailureReason.ProviderUnavailable`
- Rejected connection is mapped to `CallFailureReason.ConnectionRejected`

Logging failure does not block failover. If failover logging fails, the backup provider is still attempted.

---

## Risk Calculation Rules

Risk is calculated using interpreter availability, unassigned bookings, waiting time, and unassigned ratio.

### Red Risk

A demand item is marked Red when one or more of the following conditions are met:

- Unassigned bookings are high
- Available interpreters are critically low
- Average waiting time is high
- Unassigned ratio is high

### Amber Risk

A demand item is marked Amber when pressure exists but is not critical.

### Green Risk

A demand item is marked Green when demand is manageable.

For missing booking types with zero demand:

- Total bookings = 0
- Unassigned bookings = 0
- Risk = Green

This avoids incorrectly marking a booking type as high risk when there is no demand.

---

## Assumptions

- This is a prototype application using local mock data.
- Provider A is always the primary provider.
- Provider B is always the backup provider.
- Provider B is only attempted when Provider A fails, crashes, times out, or becomes unavailable.
- Failover logging is non-critical and must not interrupt call continuity.
- Risk rules are deterministic and implemented locally.
- Booking type stats are calculated from the same loaded dashboard data to avoid duplicate loading.
- Provider scenario controls are included to make failover behavior easy to test during review.

---

## Testing

The project includes unit tests for core business logic and ViewModels.

Covered areas:

- Risk level calculation
- Booking type aggregation
- Dashboard data aggregation
- Provider A success flow
- Provider A failure with Provider B retry
- Provider A crash handling
- Provider A timeout handling
- Both providers failed state
- Failover logger failure handling
- Dashboard ViewModel content and error states
- Call Failover ViewModel start, reset, scenario update, and duplicate-click protection

Run unit tests:

```powershell
.\gradlew.bat :app:testDebugUnitTest
```

Run debug build:

```powershell
.\gradlew.bat :app:assembleDebug
```

Run full local verification:

```powershell
.\gradlew.bat clean
.\gradlew.bat :app:assembleDebug
.\gradlew.bat :app:testDebugUnitTest
```

Expected result:

```text
BUILD SUCCESSFUL
```

---

## How to Run

1. Open the project in Android Studio.
2. Sync Gradle.
3. Select the `app` configuration.
4. Run on an emulator or Android device.

Minimum SDK:

```text
24
```

---

## Manual Test Scenarios

Use the Provider Test Scenario controls in the Call Connectivity card.

### Scenario 1

```text
Provider A = Success
Provider B = Success
```

Expected result:

```text
Connected using Provider A
Provider B is not attempted
```

### Scenario 2

```text
Provider A = Unavailable
Provider B = Success
```

Expected result:

```text
Provider A fails
Provider B is attempted automatically
Connected using Provider B
```

### Scenario 3

```text
Provider A = Crash
Provider B = Success
```

Expected result:

```text
Provider A crash is handled safely
Provider B is attempted automatically
Connected using Provider B
```

### Scenario 4

```text
Provider A = Timeout
Provider B = Success
```

Expected result:

```text
Provider A times out
Provider B is attempted automatically
Connected using Provider B
```

### Scenario 5

```text
Provider A = Rejected
Provider B = Rejected
```

Expected result:

```text
Both providers fail
Final failed state is shown
```

---

## Design System

The app uses a centralized Compose design system.

Design system includes:

- Central colors
- Central spacing
- Central dimensions
- Central shapes
- Central typography
- Reusable components

Important files:

```text
core/designsystem/theme
core/designsystem/component
```

This avoids random hardcoded styling and keeps the UI consistent.

---

## Performance and Reliability Considerations

- `StateFlow` is used for lifecycle-aware UI state.
- `collectAsStateWithLifecycle()` is used in Compose.
- `LazyColumn` uses stable keys.
- Domain logic is separated from UI rendering.
- Failover flow uses coroutines and timeout handling.
- Provider crashes are caught and mapped to safe failure states.
- Dashboard aggregation avoids duplicate language demand loading.
- Scenario changes are blocked while a call attempt is active.
- Logging failure does not block provider retry.

---

## Build Status

Latest local verification:

```text
assembleDebug: PASS
testDebugUnitTest: PASS
```

---

## Author Notes

This project was built as an Android Developer Assessment prototype. The focus is on clean architecture, state-driven UI, deterministic business logic, resilient call failover handling, and testable Kotlin code.
