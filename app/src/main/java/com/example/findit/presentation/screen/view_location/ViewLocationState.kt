package com.example.findit.presentation.screen.view_location
import com.example.findit.domain.model.PostType
import com.google.android.gms.maps.model.LatLng

data class ViewLocationState(
    val postLocation: LatLng? = null,
    val type: PostType = PostType.LOST,
    val error: String? = null,
    val isLoading: Boolean = false,
    val currentUserLocation: LatLng? = null
)
