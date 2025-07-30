package com.example.findit.presentation.screen.location

import com.google.android.gms.maps.model.LatLng

sealed class LocationEvent {
    data class OpenBottomSheet(val postId : String, val userName: String, val description: String) : LocationEvent()
    data class SetCurrentUserLocation(val location: LatLng) : LocationEvent()
    data object ClearError : LocationEvent()
    data object GetPosts : LocationEvent()
}