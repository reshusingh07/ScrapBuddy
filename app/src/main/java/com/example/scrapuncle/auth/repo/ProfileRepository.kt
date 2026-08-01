package com.example.scrapuncle.auth.repo

import com.example.scrapuncle.auth.data.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    suspend fun saveUserProfile(profile: Profile)

    /**
     * Whether the signed-in user has already been through profile creation.
     *
     * This is the question the auth flow asks to decide between sending someone to the
     * app and sending them to CreateProfileScreen. It is a one-shot read rather than a
     * [observeUserProfile] subscription because the answer is needed once, at a decision
     * point, not continuously.
     *
     * Returns false when nobody is signed in. Throws if the profile cannot be read at
     * all (e.g. no network and nothing cached) — callers must not treat an unknown
     * answer as "no profile".
     */
    suspend fun hasCompletedProfile(): Boolean

    fun observeUserProfile(): Flow<Profile?>

    suspend fun updateUserProfile(
        fullName: String,
        email: String
    )

    suspend fun getUserPhone(): String?

    suspend fun signOut()
}