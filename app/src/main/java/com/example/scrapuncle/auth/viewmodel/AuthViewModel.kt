package com.example.scrapuncle.auth.viewmodel


import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrapuncle.auth.repo.AuthRepository
import com.example.scrapuncle.auth.uistate.AuthState
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: AuthRepository
) : ViewModel() {

    private val auth get() = repo.getAuth()

    private val _uiState = MutableStateFlow<AuthState>(AuthState.Idle)
    val uiState: StateFlow<AuthState> = _uiState

    //  Phone input controlled by ViewModel
    private val _phone = MutableStateFlow("")
    val phone: StateFlow<String> = _phone

    // Store latest verificationId and token
    private var currentVerificationId: String? = null
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null

    private var verificationId: String? = null

    init {
        checkUserSession()
    }

    private fun checkUserSession() {
        viewModelScope.launch {
            _uiState.value = AuthState.Loading
            val user = auth.currentUser
            _uiState.value = if (user != null) {
                AuthState.Authenticated(user)
            } else {
                AuthState.Unauthenticated
            }
        }
    }

    //  Called from UI when user types
    fun onPhoneChanged(input: String) {
        // Only digits, max 10
        if (input.length <= 10 && input.all { it.isDigit() }) {
            _phone.value = input

            // If user is changing input, clear stale error
            if (_uiState.value is AuthState.Error) {
                _uiState.value = AuthState.Idle
            }
        }
    }

    //  Validation lives here, not in UI
    private fun isValidPhone(phone: String): Boolean {
        return phone.length == 10 && phone.all { it.isDigit() }
    }

    fun canStartVerification(): Boolean = isValidPhone(_phone.value)

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // Auto retrieval or instant verification -> sign in
            viewModelScope.launch {
                _uiState.value = AuthState.Loading
                signInWithCredential(credential)
            }
        }

        override fun onVerificationFailed(e: FirebaseException) {
            _uiState.value = AuthState.Error(e.localizedMessage ?: "Verification failed")
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            // Save for manual code verification or resending
            currentVerificationId = verificationId
            resendToken = token
            _uiState.value = AuthState.CodeSent(verificationId)
        }
    }

    fun startPhoneVerification(activity: Activity) {
        val rawPhone = _phone.value

        if (!isValidPhone(rawPhone)) {
            _uiState.value = AuthState.Error("Enter a valid 10-digit phone number")
            return
        }

        _uiState.value = AuthState.Loading

        val formattedNumber = "+91$rawPhone"

        try {
            repo.startPhoneNumberVerification(
                phoneNumber = formattedNumber,
                activity = activity,
                callbacks = callbacks,
                timeoutSeconds = 60
            )
        } catch (e: Exception) {
            _uiState.value = AuthState.Error(e.localizedMessage ?: "Failed to start verification")
        }
    }



    fun verifyCode(code: String) {
        val verificationId = currentVerificationId ?: run {
            _uiState.value = AuthState.Error("Request OTP again")
            return
        }

        _uiState.value = AuthState.Loading
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        viewModelScope.launch { signInWithCredential(credential) }
    }

    private suspend fun signInWithCredential(credential: AuthCredential) {
        try {
            auth.signInWithCredential(credential).await()
            val user = auth.currentUser
            _uiState.value = if (user != null) {
                AuthState.Authenticated(user)
            } else {
                AuthState.Error("Sign-in failed")
            }
        } catch (e: Exception) {
            _uiState.value = AuthState.Error(e.localizedMessage ?: "Sign-in error")
        }
    }

    // expose helper for resend (optional)
    fun resendCode(activity: Activity) {
        val phoneNumber = "+91${_phone.value}"

        val token = resendToken ?: run {
            _uiState.value = AuthState.Error("Cannot resend yet. Try again later.")
            return
        }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .setForceResendingToken(token)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
        _uiState.value = AuthState.Loading
    }


    // inside AuthViewModel
    fun clearError() {
        if (_uiState.value is AuthState.Error) {
            _uiState.value = AuthState.Idle
        }
    }


//    fun signOut() {
//        auth.signOut()
//        _uiState.value = AuthState.Unauthenticated
//    }
//
//    fun isLoggedIn(): Boolean {
//        return auth.currentUser != null
//    }

    fun resetState() {
        _uiState.value = AuthState.Idle
    }

}
