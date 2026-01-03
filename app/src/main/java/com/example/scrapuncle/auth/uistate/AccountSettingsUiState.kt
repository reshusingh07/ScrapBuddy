package com.example.scrapuncle.auth.uistate


data class AccountSettingsUiState(
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val gender: String = "",
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val isDirty: Boolean = false,

    val initialFullName: String = "",
    val initialEmail: String = "",

    val isSaveEnabled: Boolean = false,
    val errorMessage: String? = null
)
