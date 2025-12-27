package com.example.scrapuncle.auth.repo

import com.example.scrapuncle.auth.data.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    suspend fun saveUserProfile(profile: Profile)

    fun observeUserProfile(): Flow<Profile?>

    suspend fun updateUserProfile(
        fullName: String,
        email: String
    )

    suspend fun getUserPhone(): String?

    suspend fun signOut()
}