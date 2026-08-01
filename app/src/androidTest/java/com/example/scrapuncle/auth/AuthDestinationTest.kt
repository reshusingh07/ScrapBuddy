package com.example.scrapuncle.auth

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.scrapuncle.auth.repositoryImpl.ProfileRepositoryImpl
import com.google.firebase.FirebaseApp
import com.example.scrapuncle.auth.uistate.AuthDestination
import com.example.scrapuncle.auth.uistate.AuthState
import com.example.scrapuncle.auth.uistate.toDestination
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assume.assumeNotNull
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Covers the decision that used to be missing: after a user is authenticated, do they go
 * into the app or into profile creation?
 *
 * Runs on a device because it exercises the real Firebase classes. The Firestore test is
 * read-only and never writes or deletes anything.
 */
@RunWith(AndroidJUnit4::class)
class AuthDestinationTest {

    private val auth get() = FirebaseAuth.getInstance()

    // ------------------------------------------------------------------
    // The routing rule itself
    // ------------------------------------------------------------------

    @Test
    fun authenticatedUserWithProfileGoesHome() {
        val user = auth.currentUser
        assumeNotNull("Needs a signed-in device", user)

        val state = AuthState.Authenticated(user = user!!, hasProfile = true)

        // This is the bug: before the fix a returning user was sent to CreateProfile.
        assertEquals(AuthDestination.Home, state.toDestination())
    }

    @Test
    fun authenticatedUserWithoutProfileGoesToCreateProfile() {
        val user = auth.currentUser
        assumeNotNull("Needs a signed-in device", user)

        val state = AuthState.Authenticated(user = user!!, hasProfile = false)

        // New-user onboarding must keep working.
        assertEquals(AuthDestination.CreateProfile, state.toDestination())
    }

    @Test
    fun unauthenticatedUserGoesToWelcome() {
        assertEquals(AuthDestination.Welcome, AuthState.Unauthenticated.toDestination())
    }

    @Test
    fun failedProfileLookupGoesToWelcomeRatherThanGuessing() {
        // An unknown answer must never be read as "no profile" — that is precisely what
        // would send a returning user to overwrite the profile they already have.
        val state = AuthState.Error("Couldn't load your account.")

        assertEquals(AuthDestination.Welcome, state.toDestination())
    }

    @Test
    fun inProgressStatesNeverResolveToAnAppDestination() {
        // Splash waits these out instead of routing on them.
        assertEquals(AuthDestination.Welcome, AuthState.Idle.toDestination())
        assertEquals(AuthDestination.Welcome, AuthState.Loading.toDestination())
    }

    // ------------------------------------------------------------------
    // The Firestore lookup that feeds the rule
    // ------------------------------------------------------------------

    @Test
    fun signedInUserWithSavedProfileIsReportedAsComplete() = runBlocking {
        assumeNotNull("Needs a signed-in device with a saved profile", auth.currentUser)

        val repo = ProfileRepositoryImpl(auth, FirebaseFirestore.getInstance())

        assertTrue(
            "The signed-in user has a profile document, so onboarding must be skipped",
            repo.hasCompletedProfile()
        )
    }

    @Test
    fun profileLookupIsFalseWhenNobodySignedIn() = runBlocking {
        // Uses a second FirebaseApp so the device's real session is left untouched: this
        // auth instance simply has nobody signed into it.
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val isolatedApp = runCatching {
            FirebaseApp.initializeApp(context, FirebaseApp.getInstance().options, SIGNED_OUT_APP)
        }.getOrElse { FirebaseApp.getInstance(SIGNED_OUT_APP) }

        val repo = ProfileRepositoryImpl(
            FirebaseAuth.getInstance(isolatedApp),
            FirebaseFirestore.getInstance()
        )

        // Answers false rather than throwing, and without touching Firestore at all —
        // so a signed-out user is routed by the auth check, not by a network error.
        assertFalse(repo.hasCompletedProfile())
    }

    private companion object {
        const val SIGNED_OUT_APP = "auth-destination-test"
    }
}
