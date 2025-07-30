package com.example.findit.presentation.screen.view_location

import com.google.android.gms.maps.model.LatLng

sealed class ViewLocationEvent {
    data class GetPost(val postId: String) : ViewLocationEvent()
    data class SetCurrentUserLocation(val location: LatLng) : ViewLocationEvent()
    data object ClearError : ViewLocationEvent()
}