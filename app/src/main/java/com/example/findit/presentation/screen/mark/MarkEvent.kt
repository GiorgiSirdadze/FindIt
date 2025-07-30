package com.example.findit.presentation.screen.mark

import com.google.android.gms.maps.model.LatLng

sealed class MarkEvent {
    data class UpdateSelectedLocation(val latLng: LatLng) : MarkEvent()
    data object NavigateBack : MarkEvent()
}