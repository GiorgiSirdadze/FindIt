package com.example.findit.domain.usecase.post

import com.example.findit.domain.model.PostDomain
import com.example.findit.domain.repository.post.UploadPostRepository
import com.example.findit.domain.resource.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class UploadPostUseCase @Inject constructor(
    private val repository: UploadPostRepository
) {
    suspend operator fun invoke(post: PostDomain): Flow<Resource<Boolean>> {
        return repository.uploadPost(post)
    }
}