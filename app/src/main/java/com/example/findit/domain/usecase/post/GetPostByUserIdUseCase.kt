package com.example.findit.domain.usecase.post

import com.example.findit.domain.repository.post.PostsRepository
import javax.inject.Inject

class GetPostByUserIdUseCase @Inject constructor(
    private val repository: PostsRepository
) {
    suspend operator fun invoke(userId: String) = repository.getPostsByUserID(userId)
}