package com.example.findit.domain.repository.auth

import com.example.findit.domain.resource.RegisterForm
import com.example.findit.domain.resource.Resource
import kotlinx.coroutines.flow.Flow

interface RegisterRepository  {
    suspend fun registerUser(registerForm: RegisterForm): Flow<Resource<Boolean>>
}
