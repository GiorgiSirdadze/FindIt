package com.example.findit.domain.repository.auth

import com.example.findit.domain.resource.Resource
import kotlinx.coroutines.flow.Flow

interface LogInRepository {

    suspend fun loginUser(email: String, password: String): Flow<Resource<Boolean>>

}