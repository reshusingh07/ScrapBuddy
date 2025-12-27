package com.example.scrapuncle.auth.uistate

import com.example.scrapuncle.auth.ui.Gender

data class CreateProfileUiState(
    val fullName: String = "",
    val email: String = "",
    val pinCode: String = "",
    val gender: Gender? = null,
    val isChecked: Boolean = false,

    val isSubmitEnabled: Boolean = false,
    val submissionInProgress: Boolean = false,
)