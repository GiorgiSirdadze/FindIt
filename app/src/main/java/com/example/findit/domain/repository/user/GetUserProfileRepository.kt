package com.example.findit.domain.repository.user

import com.example.findit.domain.model.UserProfile
import com.example.findit.domain.resource.Resource
import kotlinx.coroutines.flow.Flow

interface GetUserProfileRepository {
    suspend fun getUserProfile(userid:String): Flow<Resource<UserProfile>>
}