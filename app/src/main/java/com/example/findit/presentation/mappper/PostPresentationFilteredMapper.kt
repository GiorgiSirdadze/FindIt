package com.example.findit.presentation.mappper

import com.example.findit.presentation.model.PostPresentation
import com.example.findit.presentation.model.PostPresentationFilter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun PostPresentationFilter.toPresentationFiltered(): PostPresentation {
    return PostPresentation(
        postId = postId,
        imageUrl = imageUrl,
        description = description,
        userId = userId,
        timestamp = formatTimestamp(timestamp),
        location = location,
        postType = postType,
        userFullName = userFullName,
        userProfilePicture = profileImageUrl
    )
}

private fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
