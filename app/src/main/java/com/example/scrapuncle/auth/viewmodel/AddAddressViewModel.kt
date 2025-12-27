package com.example.scrapuncle.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrapuncle.auth.data.Address
import com.example.scrapuncle.auth.repo.AddressRepository
import com.example.scrapuncle.auth.ui.AddressAs
import com.example.scrapuncle.auth.uistate.AddAddressUiState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject




sealed class AddressUiEvent {
    data class ShowMessage(val message: String) : AddressUiEvent()
}


@HiltViewModel
class AddAddressViewModel @Inject constructor(
 private val repo: AddressRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddAddressUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<AddressUiEvent>()
    val events = _events.asSharedFlow()

//    val saved = addressRepo.addAddress(address)
//
//    scheduleViewModel.onUiEvent(
//    ScheduleUiEvent.AddressCreated(saved.id)
//    )


    private fun currentUid(): String? = FirebaseAuth.getInstance().currentUser?.uid

    init {
        loadAddresses()
    }

    // -------- Load all addresses ----------
    fun loadAddresses() {
        val uid = currentUid() ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val list = repo.getAllAddresses(uid)
                _uiState.update { it.copy(isLoading = false, addresses = list) }
            }
            catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }


    // -------- Field updates ----------
    fun onAddressChange(value: String) {
        _uiState.update { it.copy(fullAddress = value) }
    }

    fun onLandmarkChange(value: String) {
        _uiState.update { it.copy(landmark = value) }
    }

    fun onCityChange(value: String) {
        _uiState.update { it.copy(city = value) }
    }

    fun onStateChange(value: String) {
        _uiState.update { it.copy(state = value) }
    }
    fun onPinCodeChange(v: String) {
        val filtered = v.filter { it.isDigit() }.take(6)
        _uiState.update { it.copy(pinCode = filtered) }
    }
    fun onTagChange(tag: AddressAs) {
        _uiState.update { it.copy(tag =tag) }
    }


    // -------- Save address ----------
    fun saveAddress( ) {
        val state = _uiState.value

        if(state.fullAddress.isBlank()) {
            _uiState.update { it.copy(error = "Please enter the address") }
            return
        }
        if(state.city.isBlank()) {
            _uiState.update { it.copy(error =  "Please enter your city") }
            return
        }
        if(state.state.isBlank()) {
            _uiState.update { it.copy(error =  "Please enter your state") }
            return
        }
        if(state.pinCode.length != 6) {
            _uiState.update { it.copy(error =  "Please enter a valid 6-digit pincode") }
            return
        }
        if(state.tag == null) {
            _uiState.update { it.copy(error = "Please select type of this address") }
            return
        }

//        if (state.fullAddress.isBlank() ||
//            state.city.isBlank() ||
//            state.state.isBlank()
//            ) {
//            _uiState.update { it.copy(error = "Address is required") }
//            return
//        }

        val uid = currentUid()
        if (uid == null) {
            _uiState.update { it.copy(error = "User not signed in") }
            return
        }

        val address = Address(
            fullAddress = state.fullAddress.trim(),
            landmark = state.landmark.ifBlank { null },
            tag = state.tag.name,
            city = state.city.trim(),
            state = state.state.trim(),
            pinCode = state.pinCode.trim()
        )


        viewModelScope.launch {
            val startTime = System.currentTimeMillis()
            _uiState.update { it.copy(isSaving = true, error = null, saveSuccess = null) }
            try {

                val elapsed = System.currentTimeMillis() - startTime
                if (elapsed < 700) {
                    delay(700 - elapsed)
                }

                repo.addAddress(address)
                val updated = repo.getAllAddresses(uid)
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        saveSuccess = true,
                        addresses = updated,
                        fullAddress = "",
                        landmark = "",
                        city = "",
                        state = "",
                        pinCode = "",
                        tag = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        saveSuccess = false,
                        error = e.message
                    )
                }
            }
        }
    }





//    fun deleteAddress(id: String, onDone: (() -> Unit)? = null) {
//        viewModelScope.launch {
//            try {
//                isLoading.value = true
//                repo.deleteAddress(id)
//                addresses.value = repo.getAllAddresses()
//                onDone?.invoke()
//            } catch (e: Exception) {
//                error.value = e.message
//            } finally {
//                isLoading.value = false
//            }
//        }
//    }



    // optionally reset saveResult after UI handled it
    fun clearSaveResult() {
        _uiState.update { it.copy(saveSuccess = null) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
