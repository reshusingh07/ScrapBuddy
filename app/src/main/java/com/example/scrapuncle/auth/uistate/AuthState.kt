package com.example.scrapuncle.auth.uistate

import com.google.firebase.auth.FirebaseUser

sealed interface AuthState {
    data object Idle : AuthState
    data object Loading : AuthState
    data class Authenticated(val user: FirebaseUser) : AuthState
    data object Unauthenticated : AuthState
    data class CodeSent(val verificationId: String) : AuthState
    data class Success(val uid: String) : AuthState   // if you really need it
    data class Error(val message: String) : AuthState
}
