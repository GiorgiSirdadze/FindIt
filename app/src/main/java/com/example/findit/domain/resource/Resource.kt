package com.example.findit.domain.resource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val errorMessage: String) : Resource<Nothing>()
    data class Loader(val isLoading : Boolean) : Resource<Nothing>()
}

fun <T, R> Flow<Resource<T>>.mapResourceData(transform: suspend (T) -> R): Flow<Resource<R>> {
    return this.map { resource ->
        when (resource) {
            is Resource.Success -> Resource.Success(transform(resource.data))
            is Resource.Error -> Resource.Error(resource.errorMessage)
            is Resource.Loader -> Resource.Loader(resource.isLoading)
        }
    }
}


