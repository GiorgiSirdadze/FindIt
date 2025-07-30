package com.example.findit.domain.repository.user

import com.example.findit.domain.resource.Resource
import kotlinx.coroutines.flow.Flow

interface UpdateProfileImageRepository {
    suspend fun updateProfileImageUrl(imageUrl: String): Flow<Resource<Unit>>
}
