package com.example.findit.domain.repository.user

import com.example.findit.domain.resource.Resource
import kotlinx.coroutines.flow.Flow

interface GetProfilePictureRepository {
    suspend fun getProfilePictureUrl(userid :String): Flow<Resource<String>>
}
