package com.example.findit.domain.usecase.post

import android.graphics.Bitmap
import com.example.findit.domain.repository.user.UploadImageRepository
import com.example.findit.domain.resource.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UploadPostPhotoUseCase @Inject constructor(
    private val uploadRepository: UploadImageRepository
) {
    suspend operator fun invoke(bitmap: Bitmap): Flow<Resource<String>> {
    return uploadRepository.uploadPostImage(bitmap)
    }
}