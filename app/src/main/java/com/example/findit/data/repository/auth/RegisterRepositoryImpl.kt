package com.example.findit.data.repository.auth

import com.example.findit.domain.repository.auth.RegisterRepository
import com.example.findit.domain.resource.RegisterForm
import com.example.findit.domain.resource.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : RegisterRepository {

    override suspend fun registerUser(
        registerForm: RegisterForm,
    ): Flow<Resource<Boolean>>  = flow{
        emit(Resource.Loader(isLoading = true))
        try {
            val email = registerForm.email
            val password = registerForm.password

            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val user = authResult.user ?: throw Exception("User is null")

            val userMap = hashMapOf(
                "uid" to user.uid,
                "email" to email,
                "name" to registerForm.firstName,
                "surname" to registerForm.lastName,
                "phone" to registerForm.phone,
            )
            firestore.collection("users")
                .document(user.uid)
                .set(userMap).await()
           emit( Resource.Success(true))

        } catch (e: FirebaseAuthException) {
            emit(Resource.Error(e.message ?: "Authentication error"))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Login failed"))
        }
    }
}