package com.example.findit.data.repository.auth

import com.example.findit.domain.repository.auth.LogInRepository
import com.example.findit.domain.resource.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LogInRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : LogInRepository {

    override suspend fun loginUser(email: String, password: String): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loader(isLoading = true))

        try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            if (result.user != null) {
                emit(Resource.Success(true))
            } else {
                emit(Resource.Error("Login failed"))
            }
        } catch (e: FirebaseAuthException) {
            emit(Resource.Error(e.message ?: "Authentication error"))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Login failed"))
        }
    }
}
