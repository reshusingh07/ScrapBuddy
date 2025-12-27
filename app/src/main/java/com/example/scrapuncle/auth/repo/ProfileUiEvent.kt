package com.example.scrapuncle.auth.repo

sealed class ProfileUiEvent {
    object AccountCreated : ProfileUiEvent()
    data class ShowMessage(val message: String) : ProfileUiEvent()
}