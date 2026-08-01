package com.example.scrapuncle.auth.uistate

import com.google.firebase.auth.FirebaseUser

sealed interface AuthState {
    data object Idle : AuthState
    data object Loading : AuthState
    /**
     * [hasProfile] is carried alongside the user because "signed in" alone never told
     * the UI enough to route on: a returning user and a brand-new user look identical
     * at this point, and assuming the latter sent returning users back through profile
     * creation.
     */
    data class Authenticated(
        val user: FirebaseUser,
        val hasProfile: Boolean
    ) : AuthState
    data object Unauthenticated : AuthState
    data class CodeSent(val verificationId: String) : AuthState
    data class Success(val uid: String) : AuthState   // if you really need it
    data class Error(val message: String) : AuthState
}
