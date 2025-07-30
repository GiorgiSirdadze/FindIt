package com.example.findit.domain.usecase.auth

import com.example.findit.domain.repository.auth.RegisterRepository
import com.example.findit.domain.resource.RegisterForm
import com.example.findit.domain.resource.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterUseCase@Inject constructor(
    private val repository: RegisterRepository
){
    suspend operator fun invoke(registerForm: RegisterForm): Flow<Resource<Boolean>> {
        return repository.registerUser(registerForm)
    }
}