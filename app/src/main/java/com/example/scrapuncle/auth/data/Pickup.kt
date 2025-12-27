package com.example.scrapuncle.auth.data

import com.google.firebase.Timestamp

data class Pickup(
    val id: String = "",
    val date: String = "",
    val slot: String = "",
    val weightRange: String = "",
    val addressId: String = "",
    val status: String = "",
    val userId: String = "",
    val createdAt: Timestamp? = null
)