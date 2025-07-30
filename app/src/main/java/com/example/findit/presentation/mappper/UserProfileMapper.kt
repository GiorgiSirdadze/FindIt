package com.example.findit.presentation.mappper


import com.example.findit.domain.model.UserProfile
import com.example.findit.presentation.model.UserProfilePresentation

fun UserProfile.toPresentation(): UserProfilePresentation {
    return  UserProfilePresentation(
        name = name,
        surname = surname,
        phone = phone,
        email = email,
        password = password,
        profileImageUrl = profileImageUrl
    )
}
