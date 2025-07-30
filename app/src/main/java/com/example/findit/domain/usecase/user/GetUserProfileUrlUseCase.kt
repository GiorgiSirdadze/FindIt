package com.example.findit.domain.usecase.user

import com.example.findit.domain.repository.user.UserRepository
import javax.inject.Inject

class GetUserProfileUrlUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(userId: String): String {
        return repository.getUserProfile(userId)
    }
}