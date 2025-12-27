package com.example.scrapuncle.auth.data

data class Profile(
    val uid: String = "",
    val fullName: String = "",
    val phone: String = "",
    val email: String = "",
    val pinCode: String = "",
    val gender: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
