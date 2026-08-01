# Existing User Login Bug Investigation

> **Who is this for?**
> Anyone who wants to understand why returning users were asked to build their profile
> twice, and how it was fixed. No prior knowledge of the codebase is assumed. Technical
> words are explained the first time they appear.

---

## Words you will see in this document

Read these once and the rest will make sense.

| Word | What it actually means |
|---|---|
| **Firebase Auth** | Google's service that checks "is this really your phone number?" by texting you a code. |
| **Firestore** | Google's online database. This app keeps each user's profile there. |
| **Repository** | A helper class whose only job is talking to the server or database. The rest of the app asks it questions instead of calling the network directly. |
| **ViewModel** | The brain behind a screen. It holds the data and makes the decisions, so the screen itself only has to draw things. |
| **StateFlow** | A pipe that always holds the newest value and pushes it to whoever is watching. When the value changes, the screen redraws automatically. |
| **Navigation / NavGraph** | The map of the app. It lists every screen and the paths between them. |
| **Composable / Compose** | The way screens are written in this app — as functions that describe what to draw. |
| **Session** | The app remembering you are logged in, so you don't type your number every time you open it. |
| **OTP** | One-Time Password — the 6-digit code sent by SMS. |
| **uid** | The permanent ID Firebase gives your account. Your profile is stored under this ID. |

---

## 1. Problem

When someone who **already had an account** logged in with their phone number and OTP,
the app made them fill in the "Create Your Profile" form **again** — name, email, PIN
code, gender, the lot. They had already done all of that when they first signed up.

A real-world way to picture it:

> You have been a member of a gym for two years. Every single morning, the person at the
> front desk hands you a blank membership form and asks you to fill it in from scratch —
> even though your completed form is sitting in the filing cabinet right behind them.
>
> The form was never the problem. **Nobody was checking the filing cabinet.**

---

## 2. Expected Behavior

```
EXISTING user                          NEW user
-------------                          --------
Phone number                           Phone number
     |                                      |
    OTP                                    OTP
     |                                      |
     v                                      v
  HOME SCREEN                        CREATE PROFILE SCREEN
 (straight in)                       (fill it in once)
                                            |
                                            v
                                       HOME SCREEN
```

- An existing user should go **straight into the app**.
- A new user should **still** be sent to `CreateProfileScreen`. This must not break.

---

## 3. Actual Behavior

```
EXISTING user                          NEW user
-------------                          --------
Phone number                           Phone number
     |                                      |
    OTP                                    OTP
     |                                      |
     v                                      v
CREATE PROFILE SCREEN  <-- WRONG     CREATE PROFILE SCREEN  <-- correct
```

**Everyone** landed on `CreateProfileScreen`, regardless of whether they had a profile.

Worse, finishing that form calls `saveUserProfile`, which **overwrites** the existing
profile document. So the bug did not merely annoy people — it quietly replaced real
saved data with whatever was typed the second time.

---

## 4. Root Cause

### The one-line version

The app never asked whether a profile already existed. It only ever asked *"is this
person logged in?"* — and then sent every logged-in person to `CreateProfileScreen`.

### The exact code

In `OtpScreen.kt`, this ran whenever sign-in succeeded:

```kotlin
LaunchedEffect(authState) {
    when (authState) {
        is AuthState.Authenticated -> {
            onNavigateToCreateProfile()   // <-- ALWAYS. No condition. No check.
            authViewModel.clearError()
        }
        ...
    }
}
```

`onNavigateToCreateProfile()` is called with **no `if` in front of it**. That single line
is the entire bug.

### Why it was easy to miss

The `AuthState` that the screen was reading simply did not contain the answer:

```kotlin
data class Authenticated(val user: FirebaseUser) : AuthState
```

`Authenticated` said *"someone is signed in"*. It could not say *"...and they already
have a profile"*, because it had nowhere to put that fact. A screen holding only this
state **cannot** tell a returning user apart from a brand-new one. Given the choice, the
code guessed "new user" — every time.

### The information existed, but was never used for this

`ProfileRepository` already had a way to read the profile:

```kotlin
fun observeUserProfile(): Flow<Profile?>
```

But the only caller was `AccountSettingsViewModel`, which uses it to fill in the Account
Settings form. **Nothing ever used it to make a navigation decision.**

So the picture was:

```
     Firestore
  users/{uid} document
   { fullName, phone, ... }        <-- the answer was here the whole time
          |
          | read by
          v
  AccountSettingsViewModel         <-- ...but only to display a form
          |
          X
          |
          +-- NEVER reached the navigation decision
```

### A second, related hole (found during the investigation)

`Splash.kt` had the same blind spot:

```kotlin
val user = FirebaseAuth.getInstance().currentUser
val loggedIn = user != null
onFinished(loggedIn)          // logged in -> Main, always
```

It also only asked "logged in?". So a **new** user who verified their OTP and then closed
the app before finishing the profile form would, on reopening, be dropped straight into
the main app **with no profile at all**. Account Settings would show "Profile not found"
and there was no route back to the form. This was never reported, but it is the same
missing check, and it is fixed here too.

### Not the cause

Ruled out while investigating, so nobody re-checks them:

- ❌ Backend not returning profile status — the profile document is written correctly.
- ❌ Profile never saved — `ProfileRepositoryImpl.saveUserProfile` works fine.
- ❌ Wrong API parsing — nothing was parsed wrongly; the data was never requested.
- ❌ StateFlow not updating — the flow updated correctly; it just carried too little information.
- ❌ Cached data lost — nothing was cached for this purpose in the first place.

The data layer was healthy. **The decision layer never asked the question.**

---

## 5. Files Investigated

Everything read while tracing the flow. ✏️ marks the files that were changed.

**Authentication**
- ✏️ `auth/ui/OtpScreen.kt` — where the bug lived
- `auth/ui/LoginScreen.kt` — phone entry
- `auth/ui/WelcomeScreen.kt` — the signed-out landing screen
- ✏️ `auth/ui/Splash.kt` — startup routing
- ✏️ `auth/viewmodel/AuthViewModel.kt` — sign-in and session logic
- `auth/viewmodel/OtpViewModel.kt` — OTP box input, timer, errors
- `auth/repo/AuthRepository.kt` — Firebase phone-auth calls
- ✏️ `auth/uistate/AuthState.kt` — the auth state type
- ✏️ `auth/uistate/AuthDestination.kt` — **new file**
- `auth/uistate/OtpUiState.kt`, `auth/uistate/UserSessionState.kt` (empty placeholder)

**Profile / onboarding**
- `auth/ui/CreateProfileScreen.kt` — the form users were wrongly shown
- `auth/viewmodel/ProfileViewModel.kt` — form validation and saving
- ✏️ `auth/repo/ProfileRepository.kt` — profile contract
- ✏️ `auth/repositoryImpl/ProfileRepositoryImpl.kt` — Firestore reads/writes
- `auth/data/Profile.kt` — the profile model
- `auth/uistate/CreateProfileUiState.kt`, `auth/repo/ProfileUiEvent.kt`

**Navigation**
- ✏️ `navigation/AppNavGraph.kt` — the app's screen map
- `navigation/Screen.kt` — route names
- `navigation/MainScreen.kt`, `navigation/MainNavGraph.kt`, `navigation/BottomNavBar.kt`
- `MainActivity.kt`, `App.kt`

**Supporting / ruled out**
- `auth/di/AppModule.kt`, `auth/di/RepositoryModule.kt`, `auth/di/ThemeModule.kt`
- `auth/viewmodel/AccountSettingsViewModel.kt` — the only existing profile reader
- `auth/repo/AddressRepository.kt`, `auth/repositoryImpl/ScheduleRepositoryImpl.kt` — to
  confirm what else writes to the `users/{uid}` document
- `data/theme/*`, `ui/theme/*` — checked because `MainActivity` waits on theme loading
- `.maestro/*.yaml` — existing end-to-end tests

**Checked and found irrelevant:** there is no DataStore or SharedPreferences used for
login. The only DataStore in the project stores the light/dark theme setting. The session
lives entirely inside the Firebase SDK.

---

## 6. How the Login Flow Works

### The short version

```
  ┌──────────────┐
  │  App opens   │
  └──────┬───────┘
         v
  ┌──────────────┐     Ask 2 questions:
  │    SPLASH    │     1. Is anyone signed in?
  │              │     2. Do they have a profile?
  └──────┬───────┘
         │
    ┌────┴─────┬──────────────┐
    v          v              v
 not signed  signed in     signed in
   in        NO profile    HAS profile
    │           │              │
    v           v              v
 WELCOME    CREATE         HOME
    │       PROFILE
    v           │
  LOGIN         └────> HOME
    │
    v
   OTP  ──> same 2 questions ──> HOME  or  CREATE PROFILE
```

### The detailed version

**Step 1 — Phone number.**
`LoginScreen` collects 10 digits. `AuthViewModel` validates them and adds `+91`.

**Step 2 — Ask Firebase for an OTP.**
`AuthRepository.startPhoneNumberVerification()` asks Firebase to text a code. Firebase
replies through callbacks. When the code is sent, the state becomes `CodeSent`, and
`LoginScreen` moves to `OtpScreen`.

**Step 3 — Type the code.**
`OtpScreen` shows six boxes (really one hidden text field behind six drawn boxes).
`OtpViewModel` only checks the *shape* of the input — six digits. It does not know
whether the code is correct.

**Step 4 — Firebase checks the code.**
`AuthViewModel.verifyCode()` builds a credential and calls `signInWithCredential`.
If the code is wrong, Firebase throws and the state becomes `Error`.

**Step 5 — Work out who this is. ← this is the step that was missing.**
Sign-in succeeding only tells us the phone number is genuine. It does **not** say whether
this person has used the app before. So the app now immediately asks Firestore:

> "Is there a profile document at `users/{uid}` with a name in it?"

**Step 6 — Store the answer in one place.**
The answer is packed into the state:

```kotlin
AuthState.Authenticated(user = user, hasProfile = true)
```

**Step 7 — Navigate.**
`OtpScreen` reads `hasProfile` and picks Home or Create Profile.

### Where the profile lives

```
Firestore
└── users
    └── {uid}                      <-- one document per user
        ├── fullName  "Reshi Singh"   <-- proof of a completed profile
        ├── phone     "+91..."
        ├── email, pinCode, gender
        ├── lastSelectedAddressId     <-- written by AddressRepository, NOT the profile
        ├── addresses/   (sub-collection)
        └── pickups/     (sub-collection)
```

This layout matters. The `users/{uid}` document is **shared** — `AddressRepository` also
writes `lastSelectedAddressId` onto it. So merely asking *"does this document exist?"*
is not proof that anyone ever completed the profile form. The check therefore looks for
`fullName`, which is the one field `ProfileViewModel.validateProfile()` always requires.

---

## 7. What Was Changed

Seven files: one new, six edited.

### 7.1 `ProfileRepository.kt` + `ProfileRepositoryImpl.kt` — ask the question

**Added** a way to find out whether a profile exists:

```kotlin
override suspend fun hasCompletedProfile(): Boolean {
    val uid = auth.currentUser?.uid ?: return false

    val snapshot = firestore.collection("users").document(uid).get().await()

    return snapshot.exists() && !snapshot.getString("fullName").isNullOrBlank()
}
```

**Why:** the app had no way to ask this question at all. It is a one-shot read, not a
live subscription, because the answer is needed once at a decision point.

**Why `fullName` and not just `exists()`:** as shown above, `AddressRepository` writes to
the same document, so existence alone can be true without any profile ever being created.

**Why it returns `false` when signed out but *throws* when the read fails:** those are two
genuinely different situations. "Nobody is signed in" is a real answer. "The network is
down" is *not* an answer, and pretending it means "no profile" is exactly what causes the
original bug.

### 7.2 `AuthState.kt` — give the state room to hold the answer

```kotlin
data class Authenticated(
    val user: FirebaseUser,
    val hasProfile: Boolean      // <-- new
) : AuthState
```

**Why:** this is the heart of the fix. The old state physically could not express the
difference between a returning user and a new one. Now it can, so no screen has to guess.

### 7.3 `AuthDestination.kt` — **new file** — write the rule down once

```kotlin
sealed interface AuthDestination {
    data object Welcome : AuthDestination
    data object CreateProfile : AuthDestination
    data object Home : AuthDestination
}

fun AuthState.toDestination(): AuthDestination = when (this) {
    is AuthState.Authenticated ->
        if (hasProfile) AuthDestination.Home else AuthDestination.CreateProfile
    else -> AuthDestination.Welcome
}
```

**Why:** the rule for choosing a destination now lives in exactly one place. Screens
navigate on a destination rather than each re-deriving the rule — which is how the two
different blind spots (OTP screen and Splash) came to exist in the first place.

Note the `else` branch: an `Error` state resolves to `Welcome`, **not** to
`CreateProfile`. If the profile lookup failed, we do not know the answer, and guessing
"no profile" would recreate the bug.

### 7.4 `AuthViewModel.kt` — do the lookup, once, for every route in

```kotlin
private suspend fun authenticatedStateFor(user: FirebaseUser): AuthState {
    return try {
        AuthState.Authenticated(user, hasProfile = profileRepository.hasCompletedProfile())
    } catch (e: Exception) {
        AuthState.Error("Couldn't load your account. Check your connection and try again.")
    }
}
```

Both entry points — a fresh OTP sign-in and a session restored at startup — go through
this one function, so they cannot drift apart.

Also changed:

- `checkUserSession()` became **public** `refreshSession()`. Splash calls it on every
  visit. This matters because sign-out lands back on Splash, and `AuthViewModel` is
  scoped to the Activity — without a refresh, its last value would still describe the
  session the user just left.
- `refreshSession()` sets `Loading` **synchronously**, before launching its coroutine.
  Otherwise a stale `Authenticated` from a previous session could satisfy a waiting
  caller before the new check had even started.
- The `init { checkUserSession() }` block was **removed**. Splash is always the start
  destination and drives the check itself; doing it in both places created a second,
  racing read whose result could land after Splash had already decided.
- Added `awaitAuthDestination()`, which waits for the check to settle and reports where
  the user belongs.

### 7.5 `OtpScreen.kt` — the actual bug fix

```kotlin
is AuthState.Authenticated -> {
    if (state.hasProfile) onNavigateToHome() else onNavigateToCreateProfile()
}
```

**Why:** this is the `if` that was missing. New users still reach `CreateProfileScreen`;
returning users no longer do.

Also: the `when` now binds `when (val state = authState)` instead of casting
`authState as AuthState.Error` in the branch body.

### 7.6 `Splash.kt` — close the second hole

- Now takes the `AuthViewModel` instead of calling `FirebaseAuth.getInstance()` directly
  from inside a Composable. Screens should not be doing their own auth lookups.
- Calls `refreshSession()`, then reports a full `AuthDestination` instead of a bare
  `Boolean`.
- Starts the lookup **before** the 2.8-second animation and awaits it after, so the
  lookup runs *during* the animation instead of adding to it.
- The drawing code moved into a private `SplashContent`, so the `@Preview` still works
  without needing a ViewModel.

### 7.7 `AppNavGraph.kt` — wire it up

```kotlin
Splash(authViewModel = authViewModel) { destination ->
    val route = when (destination) {
        AuthDestination.Home -> Screen.Main.route
        AuthDestination.CreateProfile -> Screen.CreateProfile.route
        AuthDestination.Welcome -> Screen.Welcome.route
    }
    navController.navigate(route) { popUpTo(Screen.Splash.route) { inclusive = true } }
}
```

Two back-stack corrections came with this:

- **After OTP**, navigation now clears the stack (`popUpTo(0)`). Previously Login and OTP
  stayed behind it, so pressing Back from `CreateProfileScreen` returned to the OTP
  screen — whose code has already been used and cannot work again.
- **After creating a profile**, the old code did
  `popUpTo(Screen.Welcome.route) { inclusive = true }`. If the user reached the form from
  Splash (the app-was-closed case), `Welcome` is not on the back stack at all, and
  `popUpTo` on a missing route does nothing — leaving screens stacked up. It now clears
  the stack properly.

Routing back through Splash after profile creation was **kept deliberately**: Splash
re-reads the just-saved profile, which keeps "where does this user belong" a question
only one screen answers.

---

## 8. Why This Fix Works

### The old flow

```
OTP verified
     |
     v
"Is someone signed in?"  --- yes --->  CREATE PROFILE
                                       (for everybody)
```

One question. One destination. The returning user never had a chance.

### The new flow

```
OTP verified
     |
     v
"Is someone signed in?"  --- no ---> WELCOME
     |
    yes
     |
     v
"Do they have a profile?"  <-- THE NEW QUESTION (reads Firestore)
     |
  ┌──┴───┬──────────┐
 yes    no      can't tell
  │      │          │
  v      v          v
HOME  CREATE     show an error,
      PROFILE    don't guess
```

**It fixes existing users** because the app now actually looks in the filing cabinet
before handing out a blank form.

**It cannot break new users** because when there is no profile document, the check
returns `false` and the user is sent to `CreateProfileScreen` exactly as before. The new
user path is unchanged in behaviour — it just arrives there for a *reason* now instead of
by default.

**It cannot silently regress**, because the third branch exists. The most dangerous thing
a fix like this can do is turn "I don't know" into "no profile" — that quietly restores
the original bug whenever the network hiccups, and it overwrites real user data when it
does. So an unknown answer produces a visible error instead of a guess.

**It stays fixed**, because the rule lives in one function (`toDestination`) fed by one
lookup (`authenticatedStateFor`). The bug existed in two places (OTP screen and Splash)
precisely because the rule was written nowhere and assumed everywhere.

---

## 9. Verification

Run against a Pixel 7 emulator (API 17 image label, Android 17), debug build.

### Automated — all passing

| # | What | How | Result |
|---|---|---|---|
| 1 | Whole project still compiles | `./gradlew assembleDebug` | ✅ BUILD SUCCESSFUL |
| 2 | Hilt dependency graph is valid (`AuthViewModel` now injects `ProfileRepository`) | kapt/Hilt compile step | ✅ Passed |
| 3 | Existing user with profile → **Home** | `AuthDestinationTest` | ✅ Passed |
| 4 | User **without** profile → **CreateProfile** | `AuthDestinationTest` | ✅ Passed |
| 5 | Signed-out → **Welcome** | `AuthDestinationTest` | ✅ Passed |
| 6 | Failed lookup → **Welcome**, never CreateProfile | `AuthDestinationTest` | ✅ Passed |
| 7 | `Idle`/`Loading` never route into the app | `AuthDestinationTest` | ✅ Passed |
| 8 | Real signed-in user reads as "has profile" (real Firestore) | `AuthDestinationTest` | ✅ Passed |
| 9 | Signed-out lookup returns `false` without throwing | `AuthDestinationTest` | ✅ Passed |
| 10 | **App restart with an existing session → Home** | Launched the app on a device with a live session | ✅ Landed on Home ("Hello 👋", "Reshi Singh") — no profile form |
| 11 | Signed-out cold start → Welcome → Login | `maestro test .maestro/launch-smoke.yaml` | ✅ All 4 steps COMPLETED |

Tests 3–9 are a new file, `app/src/androidTest/.../auth/AuthDestinationTest.kt`
(8 tests, all green via `./gradlew connectedDebugAndroidTest`).

Test 10 is the headline result: **before the fix this user would have been shown
`CreateProfileScreen`.** They went straight to Home.

### An unplanned but useful finding

The emulator's Firestore client was **offline** during testing. Two things came out of
that, both good:

- Test 10 still worked — the profile was served from Firestore's local cache, so returning
  users are routed correctly even with no connectivity.
- An uncached read genuinely does throw `FirebaseFirestoreException: client is offline`.
  This is not hypothetical: had `hasCompletedProfile()` swallowed errors into `false`, an
  offline launch would have sent every existing user back to the profile form and
  overwritten their data. The error branch is load-bearing.

### Not verified on-device — needs a real SMS

These need a live phone number receiving an SMS. I could not complete a real OTP
sign-in in this environment, so they were verified by code trace and by the unit-level
tests of the exact logic they run (rows 3–9), **not** by tapping through the app:

| Scenario | Expected | Status |
|---|---|---|
| Existing user: OTP → Home | Skips the profile form | Logic verified (rows 3, 8); needs a manual OTP run to confirm end-to-end |
| New user: OTP → CreateProfile | Still onboards | Logic verified (row 4); needs a manual OTP run |
| Invalid OTP | Error shown, boxes cleared | Code path unchanged by this fix; `AuthState.Error` → `otpViewModel.onAuthError` |
| Logout → login again | Correct routing | `refreshSession()` clears the stale state; needs a manual OTP run |

**To confirm these manually** (about two minutes):

1. Open the app on a signed-out device → should show **Welcome**.
2. Tap Get Started, enter a number that **already has a profile**, submit the SMS code.
   → Should land on **Home**. *(This is the bug. Before the fix it showed the form.)*
3. Force-close and reopen → should land on **Home**, no login.
4. Profile tab → Account Settings → Sign out → should return to **Welcome**.
5. Log in with a **fresh** number → should show **Create Your Profile**.
6. Complete the form → should land on **Home**. Reopen → straight to **Home**.
7. Enter a wrong OTP → should show an error and clear the boxes, and must **not** navigate.

### Regressions checked

- New-user onboarding: unchanged — no profile still routes to `CreateProfileScreen`.
- Sign-out: still returns to Splash, which now correctly re-reads the session rather than
  trusting a stale value.
- `@Preview` for Splash: still compiles and renders (moved to `SplashContent`).
- No other caller of `AuthState.Authenticated` existed, so adding a field broke nothing.
- `.maestro/launch-smoke.yaml` — the existing CI flow — still passes unmodified.

### ⚠️ One side effect you should know about

Running `./gradlew connectedDebugAndroidTest` **uninstalls the app afterwards**, which is
standard Gradle behaviour. That wiped the emulator's saved login session. The app has been
reinstalled, but **the emulator is now signed out** and will need a fresh phone + OTP
login. Nothing in Firestore was touched — the profile document is intact, so logging back
in with the same number is the ideal way to test scenario 2 above.

---

## 10. Future Improvements

Ordered by how much they are worth.

**1. Let users retry a failed profile check without redoing the OTP.**
Today, if the Firestore lookup fails right after sign-in, the user sees an error and has
to start over — even though they are already signed in. A "Retry" button calling
`refreshSession()` would fix this without touching the auth flow. *This is the weakest
part of the current fix.*

**2. Make `saveUserProfile` merge instead of overwrite.**
`ProfileRepositoryImpl.saveUserProfile` uses `.set(toSave)` with no merge option. Since
`users/{uid}` also holds `lastSelectedAddressId`, saving a profile **erases** that field.
It is harmless today because profiles are only created before any address exists — but it
becomes real data loss the moment profile editing is added. Use `SetOptions.merge()`.

**3. Give "profile complete" an explicit field.**
The check currently infers completeness from `fullName` being non-blank. That is correct
today, but it is an inference. An explicit `profileCompleted: Boolean` on the document
would say what is meant, and survives future changes to which fields are required.

**4. Move the required-fields rule out of the ViewModel.**
`ProfileViewModel.validateProfile()` decides what makes a profile valid, and
`hasCompletedProfile()` has to stay consistent with it by hand. If someone makes
`pinCode` optional, only one of the two will be updated. A shared
`Profile.isComplete()` on the model would tie them together.

**5. Cover the OTP screen with a UI test.**
Rows 3–9 test the decision; nothing automatically tests that `OtpScreen` *calls the right
callback*. A Compose UI test with a fake auth state would close the last gap, but it needs
`OtpScreen` to accept an interface rather than the concrete `AuthViewModel`.

**6. Add a Firebase Auth emulator to the test setup.**
This is what would have let the OTP scenarios be verified automatically instead of by
hand, since it allows sign-in without a real SMS.

**7. Show something during the Splash lookup.**
Splash currently waits for `max(2.8s, lookup time)`. On a slow connection the animation
just sits there. A small spinner after ~3 seconds would explain the wait.

**8. Delete `auth/uistate/UserSessionState.kt`.**
It is an empty class (`class UserSessionState {}`) that nothing uses. It was left alone
to keep this change focused, but it is dead code.
