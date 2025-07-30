package com.example.findit.data.request


import com.example.findit.domain.resource.Resource
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class ApiHelper{
    suspend fun <T> handleHttpRequest(apiCall: suspend () -> Response<T>): Flow<Resource<T>> =
        flow {
            emit(Resource.Loader(isLoading = true))
            val response = apiCall.invoke()
            try {
                if (response.isSuccessful) {
                    emit( response.body()?.let {
                        Resource.Success(data = it)
                    } ?: Resource.Error(errorMessage = "Unexpected empty response"))

                } else {
                    emit(Resource.Error(errorMessage = response.message()))
                }
                emit(Resource.Loader(isLoading = false))
            } catch (throwable: Throwable) {
                val errorMessage = when (throwable) {
                    is IOException -> "Network error: Check your internet connection."
                    is HttpException -> "HTTP error: ${
                        throwable.response()?.errorBody()?.string() ?: "Unknown error"
                    }"
                    is IllegalStateException -> "Illegal state error: ${throwable.message ?: "Unknown issue"}"
                    else -> "Unexpected error occurred: ${throwable.message ?: "Unknown error"}"
                }
                emit(Resource.Error(errorMessage = errorMessage))
                emit(Resource.Loader(isLoading = false))
            }
        }

    fun <T : Any> safeFireBaseCall(call: suspend () -> Task<T>): Flow<Resource<T>> = flow {
        emit(Resource.Loader(isLoading = true))

        try {
            val task = call()
            val result = task.await()

            if (task.isSuccessful) {
                emit(Resource.Success(result))
            } else {
                emit(Resource.Error(task.exception?.localizedMessage ?: ""))
            }
        } catch (e: FirebaseAuthException) {

            emit(Resource.Error( e.message ?: ""))
        } catch (e: Throwable) {
            emit(Resource.Error(e.message ?: ""))
        }
    }.catch {
        emit(Resource.Error(it.message ?: ""))
    }
}

