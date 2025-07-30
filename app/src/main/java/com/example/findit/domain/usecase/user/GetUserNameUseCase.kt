package com.example.findit.domain.usecase.user

import com.example.findit.domain.repository.user.UserRepository
import javax.inject.Inject

class GetUserNameUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userid: String): String {
        return userRepository.getUserFullName(userid)
    }

}
