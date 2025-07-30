package com.example.findit.domain.usecase.auth

import com.example.findit.domain.repository.user.UserRepository
import javax.inject.Inject

class GetCurrentUserIdUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): String? {
        return userRepository.getCurrentUserId()
    }
}
