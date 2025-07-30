package com.example.findit.presentation.screen.addpost

import android.graphics.Bitmap
import com.google.android.gms.maps.model.LatLng

data class AddPostState(
    val error : String? = null,
    val isLoading : Boolean = false,
    val btnEnabled : Boolean = false,
    val bitmap: Bitmap? = null,
    val location : LatLng? = null
)