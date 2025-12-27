package com.example.scrapuncle.auth.repositoryImpl

import com.example.scrapuncle.auth.data.Address
import com.example.scrapuncle.auth.data.Pickup
import com.example.scrapuncle.auth.repo.ScheduleRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.jvm.java

@Singleton
class ScheduleRepositoryImpl @Inject constructor(
) : ScheduleRepository {

    private val firestore = Firebase.firestore
    private val auth = Firebase.auth


    private fun userPickupsCollection(uid: String) =
        firestore.collection("users")
            .document(uid)
            .collection("pickups")


    override suspend fun submitPickup(
        date: LocalDate,
        timeSlot: String,
        weightRange: String,
        addressId: String?
    ): Result<Unit> {

        return try {
            val uid = auth.currentUser?.uid ?: return Result.failure(Exception("Not logged in"))

            // generate document reference FROM USER PATH
            val docRef = userPickupsCollection(uid).document()

            val pickup = hashMapOf(
                "id" to docRef.id,
                "userId" to uid,
                "date" to date.toString(),
                "slot" to timeSlot,
                "weight" to weightRange,
                "addressId" to addressId,
                "status" to "Pending",
                "createdAt" to FieldValue.serverTimestamp()
            )

            docRef.set(pickup).await()

            firestore.collection("pickups")
                .document(pickup["id"].toString())
                .set(pickup)
                .await()
            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserPickupsOnce(uid: String): List<Pickup> {
        return  userPickupsCollection(uid)
            .get()
            .await()
            .toObjects(Pickup::class.java)
    }

    override fun getUserPickups(uid: String): Flow<List<Pickup>> {
        return userPickupsCollection(uid)
            .snapshots()
            .map { it.toObjects(Pickup::class.java) }
    }

    override suspend fun getAddressById(userId: String, addressId: String): Address? {
        if (addressId.isEmpty()) return null

        return try {
            firestore.collection("users")
                .document(userId)
                .collection("addresses")
                .document(addressId)
                .get()
                .await()
                .toObject(Address::class.java)
        } catch (e: Exception) {
            null
        }
    }


}