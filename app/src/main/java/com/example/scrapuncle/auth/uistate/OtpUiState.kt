package com.example.scrapuncle.auth.uistate

data class OtpUiState(
    val otp: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val resendTimer: Int = 60,
    val canResend: Boolean = false,
    val showToast: Boolean = false
)

