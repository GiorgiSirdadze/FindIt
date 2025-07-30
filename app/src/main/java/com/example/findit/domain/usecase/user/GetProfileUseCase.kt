package com.example.findit.domain.usecase.user

import com.example.findit.domain.model.UserProfile
import com.example.findit.domain.repository.user.GetUserProfileRepository
import com.example.findit.domain.resource.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetProfileUseCase @Inject constructor(
    private val repository: GetUserProfileRepository
) {
    suspend operator fun invoke(userId: String): Flow<Resource<UserProfile>> {
        return repository.getUserProfile(userid = userId)
    }
}