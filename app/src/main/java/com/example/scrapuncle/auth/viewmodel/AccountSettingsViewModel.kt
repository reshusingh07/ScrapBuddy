package com.example.scrapuncle.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrapuncle.auth.repo.AccountSettingsEvent
import com.example.scrapuncle.auth.repositoryImpl.ProfileRepositoryImpl
import com.example.scrapuncle.auth.uistate.AccountSettingsUiState

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AccountSettingsViewModel @Inject constructor(
    private val repo: ProfileRepositoryImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountSettingsUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<AccountSettingsEvent>()
    val events = _events.asSharedFlow()

    init {
        loadProfile()
    }


    // ------------------------------------------------------------------
    // LOAD USER PROFILE
    // ------------------------------------------------------------------
    private fun loadProfile() {
        viewModelScope.launch {
            try {
                repo.observeUserProfile().collect { profile ->
                    if (profile == null) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = "Profile not found"
                            )
                        }
                        return@collect
                    }
                        val phone = profile.phone.ifBlank {
                            repo.getUserPhone() ?: ""
                        }

                        _uiState.update {
                            it.copy(
                                fullName = profile.fullName,
                                email = profile.email,
                                phone = phone,

                                // store initial values
                                initialFullName = profile.fullName,
                                initialEmail = profile.email,

                                isDirty = false,
                                isSaveEnabled = false,
                                isLoading = false,
                                errorMessage = null
                            )
                        }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to load profile"
                    )
                }
            }
        }
    }

    // ------------------------------------------------------------------
    // FIELD CHANGES
    // ------------------------------------------------------------------

    fun onFullNameChanged(value: String) {
        updateField(full = value, email = null)
    }

    fun onEmailChanged(value: String) {
        updateField(full = null, email = value)
    }

    // Core logic for updating fields
    private fun updateField(full: String?, email: String?) {
        _uiState.update { old ->

            val newState = old.copy(
                fullName = full ?: old.fullName,
                email = email ?: old.email
            )

            newState.copy(
                isDirty = isDirty(newState),
                isSaveEnabled = canSave(newState)
            )
        }
    }


    // ------------------------------------------------------------------
    // VALIDATION
    // ------------------------------------------------------------------

    private fun isDirty(state: AccountSettingsUiState): Boolean {
        return state.fullName.trim() != state.initialFullName.trim() ||
                state.email.trim() != state.initialEmail.trim()
    }

    private fun canSave(state: AccountSettingsUiState): Boolean {
        if (!isDirty(state)) return false
        if (state.fullName.isBlank()) return false
        if (state.email.isNotBlank() && "@" !in state.email) return false
        return true
    }
    private fun isValid(state: AccountSettingsUiState): Boolean {
        if (state.fullName.isBlank()) return false
        if (state.email.isNotBlank() && "@" !in state.email) return false
        return true
    }

    // ------------------------------------------------------------------
    // SAVE PROFILE
    // ------------------------------------------------------------------
    fun onSaveClicked() {
        val state = _uiState.value
        if (!state.isSaveEnabled) return

        _uiState.update { it.copy(isSaving = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                repo.updateUserProfile(
                    fullName = state.fullName.trim(),
                    email = state.email.trim()
                )

                // Reset initial values so Save button becomes disabled again
                _uiState.update {
                    it.copy(
                        initialFullName = it.fullName,
                        initialEmail = it.email,
                        isSaving = false,
                        isDirty = false,
                        isSaveEnabled = false
                    )
                }

                _events.emit(AccountSettingsEvent.ProfileSaved)

            } catch (e: Exception) {
                val msg = e.message ?: "Failed to save profile"

                _uiState.update { it.copy(isSaving = false, errorMessage = msg) }
                _events.emit(AccountSettingsEvent.ShowMessage(msg))
            }
        }
    }

    // ------------------------------------------------------------------
    // SIGN OUT
    // ------------------------------------------------------------------
    fun onSignOutClicked() {
        viewModelScope.launch {
            try {
                repo.signOut()
                _events.emit(AccountSettingsEvent.SignedOut)

            } catch (e: Exception) {
                val msg = e.message ?: "Failed to sign out"
                _events.emit(AccountSettingsEvent.ShowMessage(msg))
            }
        }
    }

    private fun computeSaveEnabled(state: AccountSettingsUiState): Boolean {
        return state.fullName.trim() != state.initialFullName.trim() ||
                state.email.trim() != state.initialEmail.trim()
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
