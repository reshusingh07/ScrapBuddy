package com.example.scrapuncle.auth.module

sealed interface ScheduleUiEvent {
    data class PickupScheduled(val message: String) : ScheduleUiEvent
    object NavigateBack : ScheduleUiEvent
}
