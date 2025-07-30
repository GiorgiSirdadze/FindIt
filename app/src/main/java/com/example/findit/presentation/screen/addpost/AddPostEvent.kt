package com.example.findit.presentation.screen.addpost

import android.net.Uri
import com.example.findit.domain.model.PostType
import com.google.android.gms.maps.model.LatLng

sealed class AddPostEvent {
    data object OpenDialog:AddPostEvent()
    data class AddLocation(val latLng: LatLng):AddPostEvent()
    data class AddPost(val type : PostType, val description :String):AddPostEvent()
    data object ClearError : AddPostEvent()
    data class ImageSelected(val uri: Uri) : AddPostEvent()
    data object OpenMap : AddPostEvent()
}