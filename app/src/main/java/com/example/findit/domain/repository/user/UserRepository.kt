package com.example.findit.domain.repository.user

interface UserRepository {
    fun getCurrentUserId(): String?
    suspend fun getUserFullName(userId: String): String
    suspend fun getUserProfile(userId: String): String
}