package com.example.findit.presentation.model

import com.example.findit.domain.model.PostType
import com.google.android.gms.maps.model.LatLng
import java.util.UUID

data class PostPresentation(
    val postId: String = UUID.randomUUID().toString(),
    val imageUrl: String = "",
    val description: String = "",
    val userProfilePicture : String = "",
    val userId: String = "",
    val timestamp: String = "0",
    val location: LatLng = LatLng(0.0, 0.0),
    val postType: PostType = PostType.LOST,
    val userFullName : String = ""
)
