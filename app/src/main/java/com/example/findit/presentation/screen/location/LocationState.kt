package com.example.findit.presentation.screen.location

import com.example.findit.presentation.model.PostPresentation
import com.google.android.gms.maps.model.LatLng

data class LocationState(
    val isLoading: Boolean = false,
    val posts: List<PostPresentation> = emptyList(),
    val error: String? = null,
    val currentUserLocation: LatLng? = null
)