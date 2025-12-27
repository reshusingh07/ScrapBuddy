package com.example.scrapuncle.auth.repo

import android.util.Log
import com.example.scrapuncle.auth.data.Address
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

@Singleton
class AddressRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {


    private fun userAddressesCollection() =
        firestore.collection("users")
            .document(currentUid())
            .collection("addresses")

    private fun currentUid(): String =
        Firebase.auth.currentUser?.uid ?: throw IllegalStateException("User not authenticated")

    suspend fun addAddress(address: Address): Address {
        val docRef = userAddressesCollection().document()
        val final = address.copy(id = docRef.id)
        docRef.set(final).await()
        return final
    }

    suspend fun updateAddress(address: Address) {
        userAddressesCollection().document(address.id).set(address).await()
    }

    suspend fun deleteAddress(addressId: String) {
        userAddressesCollection().document(addressId).delete().await()
    }

    suspend fun getAllAddresses(uid: String): List<Address> {
        val snapshot = firestore.collection("users")
            .document(uid)
            .collection("addresses")
            .orderBy("createdAt")
            .get()
            .await()

        return snapshot.documents.mapNotNull { doc ->
            doc.toObject(Address::class.java)?.copy(id = doc.id)
        }
    }


    suspend fun saveLastSelectedAddress(id: String) {
        firestore.collection("users")
            .document(currentUid())
            .update("lastSelectedAddressId", id)
            .await()
    }

    suspend fun getLastSelectedAddressId(): String? {
        val snap = firestore.collection("users")
            .document(currentUid())
            .get()
            .await()
        return snap.getString("lastSelectedAddressId")
    }

    fun observeAddresses(uid: String): Flow<List<Address>> {
        return firestore.collection("users")
            .document(uid)
            .collection("addresses")
            .orderBy("createdAt")
            .snapshots()
            .map { snap ->
                snap.documents.mapNotNull {
                    it.toObject(Address::class.java)?.copy(id = it.id)
                }
            }
    }


}
