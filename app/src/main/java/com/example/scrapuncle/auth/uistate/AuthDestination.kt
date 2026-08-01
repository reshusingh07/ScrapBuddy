package com.example.scrapuncle.auth.uistate

/**
 * Where a user belongs once we know both who they are and whether they have onboarded.
 *
 * Screens navigate on this rather than on [AuthState] directly, so the rule for choosing
 * a destination lives in one place instead of being re-derived at each call site.
 */
sealed interface AuthDestination {

    /** Nobody is signed in. */
    data object Welcome : AuthDestination

    /** Signed in, but has never completed profile creation. */
    data object CreateProfile : AuthDestination

    /** Signed in and already onboarded. */
    data object Home : AuthDestination
}

/**
 * Anything that is not a confirmed, profiled session sends the user back to the start of
 * the auth flow — including [AuthState.Error], because a failed profile lookup means we
 * do not know whether a profile exists, and guessing either way harms someone.
 */
fun AuthState.toDestination(): AuthDestination = when (this) {
    is AuthState.Authenticated ->
        if (hasProfile) AuthDestination.Home else AuthDestination.CreateProfile

    else -> AuthDestination.Welcome
}
