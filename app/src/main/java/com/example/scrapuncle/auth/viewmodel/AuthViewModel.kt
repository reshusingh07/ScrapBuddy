package com.example.scrapuncle.auth.viewmodel


import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrapuncle.auth.repo.AuthRepository
import com.example.scrapuncle.auth.repo.ProfileRepository
import com.example.scrapuncle.auth.uistate.AuthDestination
import com.example.scrapuncle.auth.uistate.AuthState
import com.example.scrapuncle.auth.uistate.toDestination
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: AuthRepository,
    private val profileRepository: ProfileRepository
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

    // Deliberately no session check in init: Splash is always the start destination and
    // drives the check itself. Doing it in both places meant a second, racing read whose
    // result could land after Splash had already made its decision.

    /**
     * Re-reads who is signed in and whether they have finished onboarding.
     *
     * Splash calls this every time it is shown. That matters because Splash is also where
     * sign-out lands, and this ViewModel is scoped to the Activity — so without a refresh
     * its last value would still describe the session the user just left.
     */
    fun refreshSession() {
        // Set synchronously rather than inside the coroutine. Callers wait for the next
        // settled state, and a stale Authenticated left over from a previous session
        // would otherwise satisfy that wait before the new check had even begun.
        _uiState.value = AuthState.Loading

        viewModelScope.launch {
            val user = auth.currentUser
            _uiState.value = if (user != null) {
                authenticatedStateFor(user)
            } else {
                AuthState.Unauthenticated
            }
        }
    }

    /**
     * Suspends until the session check has settled, then reports where the user belongs.
     */
    suspend fun awaitAuthDestination(): AuthDestination =
        uiState.first { it !is AuthState.Idle && it !is AuthState.Loading }.toDestination()

    /**
     * Turns a signed-in Firebase user into a fully resolved state.
     *
     * Being signed in is only half the answer — a returning user and a first-time user are
     * indistinguishable at this point until the profile is looked up, so that lookup
     * happens here, once, for every route into the app.
     */
    private suspend fun authenticatedStateFor(user: FirebaseUser): AuthState {
        return try {
            AuthState.Authenticated(
                user = user,
                hasProfile = profileRepository.hasCompletedProfile()
            )
        } catch (e: Exception) {
            // Never guess when the lookup fails. Assuming "no profile" is exactly what
            // sends a returning user back to overwrite the profile they already have;
            // assuming "has profile" strands a new user in an app with no profile.
            AuthState.Error("Couldn't load your account. Check your connection and try again.")
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
                authenticatedStateFor(user)
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
