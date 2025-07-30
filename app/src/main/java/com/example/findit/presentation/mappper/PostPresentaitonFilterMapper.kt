package com.example.findit.presentation.mappper

import com.example.findit.domain.model.PostDomain
import com.example.findit.presentation.model.PostPresentationFilter
import com.google.android.gms.maps.model.LatLng

fun PostDomain.toPresentationFilter(): PostPresentationFilter {
    return PostPresentationFilter(
        postId = postId,
        imageUrl = imageUrl,
        description = description,
        userId = userId,
        timestamp = timestamp,
        location = LatLng(location.latitude, location.longitude),
        postType = postType,
        userFullName = userFullName,
        profileImageUrl = userProfilePicture
    )
}
