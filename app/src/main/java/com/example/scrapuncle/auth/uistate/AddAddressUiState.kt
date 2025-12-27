package com.example.scrapuncle.auth.uistate

import com.example.scrapuncle.auth.data.Address
import com.example.scrapuncle.auth.ui.AddressAs

data class AddAddressUiState(
    val fullAddress: String = "",
    val landmark: String = "",
    val tag: AddressAs? = null,
    val pinCode: String = "",
    val city: String = "",
    val state: String = "",
    val isSaving: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val saveSuccess: Boolean? = null,
    val addresses: List<Address> = emptyList()
)