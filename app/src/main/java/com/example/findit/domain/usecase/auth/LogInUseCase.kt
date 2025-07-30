package com.example.findit.domain.usecase.auth

import com.example.findit.domain.repository.auth.LogInRepository
import com.example.findit.domain.resource.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LogInUseCase@Inject constructor(
    private val repository: LogInRepository
){

    suspend operator fun invoke(email: String, password: String): Flow<Resource<Boolean>> {

        return repository.loginUser(email, password)
        
    }

}

