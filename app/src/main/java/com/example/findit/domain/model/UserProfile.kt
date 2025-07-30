package com.example.findit.domain.model

data class UserProfile(
    val name: String,
    val surname: String,
    val phone: String,
    val email: String,
    val password: String,
    val profileImageUrl: String
)