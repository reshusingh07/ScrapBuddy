package com.example.scrapuncle.auth.repo

import com.example.scrapuncle.auth.data.Address
import com.example.scrapuncle.auth.data.Pickup
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface ScheduleRepository {
    suspend fun submitPickup(
        date: LocalDate,
        timeSlot: String,
        weightRange: String,
        addressId: String?
    ): Result<Unit>


    fun getUserPickups(uid: String): Flow<List<Pickup>>
    suspend fun getUserPickupsOnce(uid: String): List<Pickup>

    suspend fun getAddressById(userId: String, addressId: String): Address?

}
