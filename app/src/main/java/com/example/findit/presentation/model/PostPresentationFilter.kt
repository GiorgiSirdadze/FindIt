package com.example.findit.presentation.model
import com.example.findit.domain.model.PostType
import com.google.android.gms.maps.model.LatLng
data class PostPresentationFilter(
    val postId: String,
    val imageUrl: String,
    val description: String,
    val userId: String,
    val timestamp: Long,
    val location: LatLng,
    val postType: PostType,
    val userFullName: String,
    val profileImageUrl: String
)
