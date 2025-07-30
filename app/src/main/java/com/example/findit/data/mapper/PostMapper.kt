package com.example.findit.data.mapper

import com.example.findit.data.dto.PostDto
import com.example.findit.data.util.FirestoreKeys
import com.example.findit.domain.model.LocationModel
import com.example.findit.domain.model.PostDomain
import com.example.findit.domain.model.PostType
import com.google.firebase.firestore.GeoPoint

fun PostDto.toDomain(): PostDomain {
    return PostDomain(
        postId = postId,
        imageUrl = imageUrl,
        description = description,
        userId = userId,
        timestamp = timestamp,
        userProfilePicture = userProfilePicture,
        location = location.toDomain(),
        postType = postType,
        userFullName = userFullName
    )
}

fun GeoPoint.toDomain(): LocationModel {
    return LocationModel(
        latitude = latitude,
        longitude = longitude
    )
}

fun PostDomain.toDto(): PostDto {
    return PostDto(
        postId = postId,
        imageUrl = imageUrl,
        description = description,
        userId = userId,
        timestamp = timestamp,
        location = GeoPoint(location.latitude, location.longitude),
        postType = postType,
        userFullName = userFullName,
        userProfilePicture = userProfilePicture
    )
}

fun PostDto.toFirestoreMap(): Map<String, Any> {
    return mapOf(
        FirestoreKeys.POST_ID to postId,
        FirestoreKeys.IMAGE_URL to imageUrl,
        FirestoreKeys.DESCRIPTION to description,
        FirestoreKeys.USER_ID to userId,
        FirestoreKeys.TIMESTAMP to timestamp,
        FirestoreKeys.LOCATION to GeoPoint(location.latitude, location.longitude),
        FirestoreKeys.POST_TYPE to postType.toString(),
        FirestoreKeys.USER_FULL_NAME to userFullName,
        FirestoreKeys.USER_PROFILE_PICTURE to userProfilePicture
    )
}

fun Map<String, Any>.fromFirestoreMap(): PostDto {
    return PostDto(
        postId = this["postId"] as String,
        imageUrl = this["imageUrl"] as String,
        description = this["description"] as String,
        userId = this["userId"] as String,
        timestamp = this["timestamp"] as Long,
        location = this["location"] as GeoPoint,
        postType = PostType.fromString(this["postType"] as String),
        userFullName = this["userFullName"] as String,
        userProfilePicture = this["userProfilePicture"] as String
    )
}

