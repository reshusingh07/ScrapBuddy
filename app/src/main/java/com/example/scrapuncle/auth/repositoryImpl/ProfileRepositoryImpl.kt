package com.example.scrapuncle.auth.repositoryImpl

import com.example.scrapuncle.auth.data.Profile
import com.example.scrapuncle.auth.repo.ProfileRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

//
@Singleton
class ProfileRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ProfileRepository {


    private fun requireUid(): String {
        return auth.currentUser?.uid
            ?: throw IllegalStateException("User not logged in")
    }

    private fun userDocRef() =
        firestore.collection("users").document(requireUid())

    override suspend fun saveUserProfile(profile: Profile) {
        val uid = requireUid()
        val now = System.currentTimeMillis()

        val toSave = profile.copy(
            uid = uid,
            createdAt = profile.createdAt,
            updatedAt = now
        )

        userDocRef().set(toSave).await()
    }

    override fun observeUserProfile(): Flow<Profile?> = callbackFlow {
        val listener = userDocRef().addSnapshotListener { snapshot, error ->
            if (error != null) return@addSnapshotListener
            val profile = snapshot?.toObject(Profile::class.java)
            trySend(profile)
        }

        awaitClose { listener.remove() }
    }

    override suspend fun updateUserProfile(
        fullName: String,
        email: String
    ) {
        val now = System.currentTimeMillis()

        userDocRef().update(
            mapOf(
                "fullName" to fullName,
                "email" to email,
                "updatedAt" to now
            )
        ).await()
    }

    override suspend fun getUserPhone(): String? {
        return auth.currentUser?.phoneNumber
    }

    override suspend fun signOut() {
        auth.signOut()
    }

}