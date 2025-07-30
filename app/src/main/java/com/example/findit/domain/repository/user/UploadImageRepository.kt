package com.example.findit.domain.repository.user

import android.graphics.Bitmap
import com.example.findit.domain.resource.Resource
import kotlinx.coroutines.flow.Flow

interface UploadImageRepository {
    suspend fun uploadPostImage(bitmap: Bitmap): Flow<Resource<String>>
    suspend fun uploadProfileImage(bitmap: Bitmap): Flow<Resource<String>>
}
