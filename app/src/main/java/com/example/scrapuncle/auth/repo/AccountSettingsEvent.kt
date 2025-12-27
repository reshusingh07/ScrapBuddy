package com.example.scrapuncle.auth.repo

sealed interface AccountSettingsEvent {
    data object ProfileSaved : AccountSettingsEvent
    data object SignedOut : AccountSettingsEvent
    data class ShowMessage(val message: String) : AccountSettingsEvent
}