package com.example.scrapuncle.auth.uistate

import com.example.scrapuncle.auth.data.Address
import com.example.scrapuncle.auth.data.PickupWithAddress
import java.time.LocalDate



data class ScheduleUiState(
    val pickups: List<PickupWithAddress> = emptyList(),
    val addresses: List<Address> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isAddressLoading: Boolean = false,

    val selectedDate: LocalDate = LocalDate.now(),
    val selectedSlot: String? = null,
    val selectedWeight: String? = null,
    val selectedAddressId: String? = null,
    val isSubmitting: Boolean = false,
    val success: Boolean = false
)

