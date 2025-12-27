package com.example.scrapuncle.auth.viewmodel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrapuncle.auth.data.Pickup
import com.example.scrapuncle.auth.data.PickupWithAddress
import com.example.scrapuncle.auth.module.ScheduleEvent
import com.example.scrapuncle.auth.module.ScheduleUiEvent
import com.example.scrapuncle.auth.repo.AddressRepository
import com.example.scrapuncle.auth.repo.ScheduleRepository
import com.example.scrapuncle.auth.uistate.ScheduleUiState
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val scheduleRepo: ScheduleRepository, private val addressRepo: AddressRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScheduleUiState())
    val uiState = _uiState.asStateFlow()

    private val rawPickups = MutableStateFlow<List<Pickup>>(emptyList())


//    private val _toastEvent = MutableSharedFlow<Pair<String, Long>>()
//    val toastEvent = _toastEvent.asSharedFlow()

    private val _uiEvents = MutableSharedFlow<ScheduleUiEvent>()
    val uiEvents = _uiEvents.asSharedFlow()

    init {
        observePickups()
        observeAddresses()
        combinePickupsWithAddresses()
    }


    private fun observeAddresses() {
        val uid = Firebase.auth.currentUser?.uid ?: return

        viewModelScope.launch {
            addressRepo.observeAddresses(uid).collect { list ->
                update { copy(addresses = list) }
            }
        }
    }

//    fun onUiEvent(event: ScheduleUiEvent) {
//        when (event) {
//            is ScheduleUiEvent.AddressCreated -> {
//                update { copy(selectedAddressId = event.addressId) }
//
//                // optional but recommended
//                viewModelScope.launch {
//                    addressRepo.saveLastSelectedAddress(event.addressId)
//                }
//            }
//
//            ScheduleUiEvent.NavigateBack -> Unit
//        }
//    }

    fun onEvent(event: ScheduleEvent) {
        when (event) {
            is ScheduleEvent.SelectDate -> update { copy(selectedDate = event.date) }

            is ScheduleEvent.SelectSlot -> update { copy(selectedSlot = event.slot) }

            is ScheduleEvent.SelectWeight -> update { copy(selectedWeight = event.weight) }

            is ScheduleEvent.SelectAddress -> {
                update { copy(selectedAddressId = event.addressId) }
                viewModelScope.launch {
                    try {
                        addressRepo.saveLastSelectedAddress(event.addressId)
                    } catch (e: Exception) {
                        android.util.Log.w("ScheduleVM", "saveLastSelectedAddressId failed", e)
                    }
                }
            }


            ScheduleEvent.RefreshPickups -> refreshPickups()

            ScheduleEvent.SubmitPickup -> submitPickup()

        }
    }

    private fun update(block: ScheduleUiState.() -> ScheduleUiState) {
        _uiState.value = _uiState.value.block()
    }


    private fun observePickups() {
        update { copy(isLoading = true) }
        val uid = Firebase.auth.currentUser?.uid ?: return

        viewModelScope.launch {
            scheduleRepo.getUserPickups(uid).collect { list ->
                rawPickups.value = list
                update { copy(isLoading = false) }
            }
        }
    }


    private fun submitPickup() = viewModelScope.launch {
        val state = uiState.value

        // Validation
        if (state.selectedSlot.isNullOrEmpty() ||
            state.selectedWeight.isNullOrEmpty() ||
            state.selectedAddressId.isNullOrEmpty()
            ) {
            update { copy(error = "All fields are required.") }
            return@launch
        }

        update { copy(isSubmitting = true, error = null, success = false) }

        val result = scheduleRepo.submitPickup(
            date = state.selectedDate,
            timeSlot = state.selectedSlot,
            weightRange = state.selectedWeight,
            addressId = state.selectedAddressId
        )

        if (result.isSuccess) {
            update { copy(isSubmitting = false) }

            _uiEvents.emit(
                ScheduleUiEvent.PickupScheduled(
                    "Pickup scheduled successfully!"
                )
            )
        } else {
            update {
                copy(
                    isSubmitting = false,
                    error = result.exceptionOrNull()?.message ?: "Submit failed"
                )
            }
        }
    }


    private fun refreshPickups() = viewModelScope.launch {
        val uid = Firebase.auth.currentUser?.uid ?: return@launch
        scheduleRepo.getUserPickupsOnce(uid)
    }


    // Toast message for user
    fun validatePickup(): String? {
        val state = uiState.value

        if (state.selectedDate == null) return "Please select the pickup date"
        if (state.selectedSlot.isNullOrEmpty()) return "Please select a time slot"
        if (state.selectedWeight.isNullOrEmpty()) return "Please select the weight range"

        return null
    }


//    fun reloadAddresses() = viewModelScope.launch {
//        loadAddresses()
//    }

//    fun onAddressSelected(address: Address) {
//        selectedAddress = address
//        selectedAddressId = address.id
//    }


    fun loadAddresses() {
        viewModelScope.launch {
            update { copy(isAddressLoading = true) }

            try {
                val uid = Firebase.auth.currentUser?.uid ?: return@launch
                val lastSelected = addressRepo.getLastSelectedAddressId()
                val list = addressRepo.getAllAddresses(uid)

                update {
                    copy(
                        addresses = list,
                        selectedAddressId = lastSelected ?: list.firstOrNull()?.id,
                        isAddressLoading = false
                    )
                }
            } catch (e: Exception) {
                update { copy(isAddressLoading = false, error = e.message) }
            }
        }
    }

    private fun combinePickupsWithAddresses() {
        viewModelScope.launch {
            combine(
                rawPickups, uiState.map { it.addresses }) { pickups, addresses ->
                pickups.map { pickup ->
                    PickupWithAddress(
                        pickup = pickup,
                        address = addresses.firstOrNull { it.id == pickup.addressId })
                }
            }.collect { mapped ->
                update { copy(pickups = mapped) }
            }
        }
    }
}
