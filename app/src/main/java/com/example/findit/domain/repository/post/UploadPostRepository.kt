package com.example.findit.domain.repository.post

import com.example.findit.domain.model.PostDomain
import com.example.findit.domain.resource.Resource
import kotlinx.coroutines.flow.Flow

interface UploadPostRepository {
    suspend fun uploadPost(post : PostDomain) : Flow<Resource<Boolean>>
}