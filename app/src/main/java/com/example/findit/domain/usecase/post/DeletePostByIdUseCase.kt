package com.example.findit.domain.usecase.post

import com.example.findit.domain.repository.post.PostsRepository
import com.example.findit.domain.resource.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeletePostByIdUseCase @Inject
constructor(
    private val repository: PostsRepository
) {
    suspend operator fun invoke(postId: String) : Flow<Resource<Boolean>> {
        return repository.deletePost(postId)
    }
}