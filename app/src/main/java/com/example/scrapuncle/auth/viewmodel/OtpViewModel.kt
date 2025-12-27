package com.example.scrapuncle.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrapuncle.auth.repo.AuthRepository
import com.example.scrapuncle.auth.ui.OTP_LENGTH
import com.example.scrapuncle.auth.uistate.OtpUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch





@HiltViewModel
class OtpViewModel @Inject constructor(
) : ViewModel() {

    private val _uiState = MutableStateFlow(OtpUiState())
    val uiState: StateFlow<OtpUiState> = _uiState

    private var timerJob: Job? = null

    fun onOtpChange(value: String) {
        val filtered = value.filter(Char::isDigit).take(OTP_LENGTH)
        _uiState.value = _uiState.value.copy(
            otp = filtered,
            error = null
        )
    }


    fun submitOtp(onValid: (String) -> Unit) {
        val otp = _uiState.value.otp
        if (otp.length != OTP_LENGTH) {
            _uiState.value = _uiState.value.copy(
                error = "Please enter all 6 digits",
                showToast = true
            )
            return
        }
        onValid(otp)
    }

    fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            for (i in 60 downTo 0) {
                _uiState.value = _uiState.value.copy(
                    resendTimer = i,
                    canResend = (i == 0)
                )
                delay(1000)
            }
        }
    }

    override fun onCleared() {
        timerJob?.cancel()
        super.onCleared()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }



    fun onAuthError(message: String) {
        _uiState.value = _uiState.value.copy(
            otp = "",
            isLoading = false,
            error = message,
            showToast = true
        )
    }

    fun clearToast() {
        _uiState.value = _uiState.value.copy(showToast = false)
    }

}
