package com.example.findit.domain.model

import androidx.annotation.Keep

//@Keep
data class PostDomain(
    val postId: String = "",
    val imageUrl: String = "",
    val description: String = "",
    val userId: String = "",
    val timestamp: Long = 0,
    val userProfilePicture : String = "",
    val location: LocationModel = LocationModel(),
    val postType: PostType = PostType.FOUND,
    val userFullName : String = ""
)