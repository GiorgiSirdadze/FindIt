package com.example.findit.domain.usecase.user


import com.example.findit.domain.model.UserProfile
import com.example.findit.domain.repository.user.UpdateUserProfileRepository
import com.example.findit.domain.resource.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val repository: UpdateUserProfileRepository
) {
    suspend operator fun invoke(profile: UserProfile): Flow<Resource<Boolean>> {
        return repository.updateUserProfile(profile)
    }
}
