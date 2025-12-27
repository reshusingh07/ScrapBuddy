package com.example.scrapuncle.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrapuncle.auth.data.Profile
import com.example.scrapuncle.auth.repositoryImpl.ProfileRepositoryImpl
import com.example.scrapuncle.auth.repo.ProfileUiEvent
import com.example.scrapuncle.auth.ui.Gender
import com.example.scrapuncle.auth.uistate.CreateProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repo: ProfileRepositoryImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateProfileUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<ProfileUiEvent>()
    val events = _events.asSharedFlow()

    fun onFullNameChanged(value: String) {
        _uiState.update {
            it.copy(fullName = value).updateSubmitState()
        }
    }

    fun onEmailChanged(value: String) {
        _uiState.update { it.copy(email = value) }
    }

    fun onPinCodeChanged(value: String) {
        val filtered = value.filter { it.isDigit() }.take(6)
        _uiState.update {
            it.copy(pinCode = filtered).updateSubmitState()
        }
    }

    fun onCheckedChanged(checked: Boolean) {
        _uiState.update {
            it.copy(isChecked = checked).updateSubmitState()
        }
    }

    fun onSubmit() {
        val state = _uiState.value
        val error = validateProfile()
        if (error != null) {
            viewModelScope.launch {
                _events.emit(ProfileUiEvent.ShowMessage(error))
            }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(submissionInProgress = true) }

            try {
                val userPhone = repo.getUserPhone() ?: ""

                val profile = Profile(
                    fullName = state.fullName.trim(),
                    email = state.email.trim(),
                    phone = userPhone,
                    pinCode = state.pinCode.trim(),
                    gender = state.gender!!.name,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )

                repo.saveUserProfile(profile)

                _uiState.update { it.copy(submissionInProgress = false) }
                _events.emit(ProfileUiEvent.AccountCreated)

            } catch (e: Exception) {
                _uiState.update { it.copy(submissionInProgress = false) }
                _events.emit(ProfileUiEvent.ShowMessage("Failed: ${e.message}"))
            }
        }
    }

    fun onGenderSelected(gender: Gender) {
        _uiState.update {
            it.copy(gender = gender).updateSubmitState()
        }
    }

    fun validateProfile(): String? {
        val state = _uiState.value

        if (state.fullName.isBlank())
            return "Please enter your full name"

        if (state.pinCode.length != 6)
            return "Please enter a valid 6-digit pincode"

        if (state.gender == null)
            return "Please select your gender"

        if (!state.isChecked)
            return "Please accept the Privacy Policy"

        return null
    }


}

private fun CreateProfileUiState.updateSubmitState(): CreateProfileUiState {
    val enabled = fullName.isNotBlank() &&
            pinCode.length == 6 &&
            gender != null &&
            isChecked

    return copy(isSubmitEnabled = enabled)
}
