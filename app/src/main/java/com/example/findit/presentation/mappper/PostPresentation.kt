package com.example.findit.presentation.mappper

import com.example.findit.domain.model.PostDomain
import com.example.findit.presentation.model.PostPresentation
import com.google.android.gms.maps.model.LatLng
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun PostDomain.toPresentation(): PostPresentation {
    return PostPresentation(
        postId = postId,
        imageUrl = imageUrl,
        description = description,
        userId = userId,
        timestamp = formatTimestamp(timestamp),
        location = LatLng(location.latitude, location.longitude),
        postType = postType,
        userFullName = userFullName,
        userProfilePicture = userProfilePicture
    )
}

private fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}