package com.example.scrapuncle.auth.module

import java.time.LocalDate

sealed class ScheduleEvent {
    data class SelectDate(val date: LocalDate) : ScheduleEvent()
    data class SelectSlot(val slot: String) : ScheduleEvent()
    data class SelectWeight(val weight: String) : ScheduleEvent()
    data class SelectAddress(val addressId: String) : ScheduleEvent()

    object RefreshPickups : ScheduleEvent()
    object SubmitPickup : ScheduleEvent()
}
