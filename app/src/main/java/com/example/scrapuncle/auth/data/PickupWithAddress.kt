package com.example.scrapuncle.auth.data


data class PickupWithAddress(
    val pickup: Pickup,
    val address: Address? = null
)
