package com.example.scrapuncle.auth.data



// auth/data/Address.kt
data class Address(
    val id: String = "",
    val fullAddress: String = "",
    val landmark: String? = "",
    val tag: String = "Home",
    val pinCode: String = "",
    val city: String = "",
    val state: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
