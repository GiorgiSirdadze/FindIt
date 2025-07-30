package com.example.findit.presentation.screen.mark

import com.google.android.gms.maps.model.LatLng

sealed interface MarkEffect {
    data class NavigateBack(val latLng: LatLng?) : MarkEffect
}