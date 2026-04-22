package com.vrsalex.taskflow.domain.common

import com.vrsalex.network.public.common.NetworkResult

sealed interface Resource<out T> {

    data class Success<T>(val data: T) : Resource<T>

    data class Error(val message: String) : Resource<Nothing>
}


fun <T> NetworkResult<T>.toResource(): Resource<T> {
    return when (this) {
        is NetworkResult.Success -> Resource.Success(data)
        NetworkResult.Error.NetworkError -> Resource.Error(message = "Нет сети")
        is NetworkResult.Error.HttpError -> Resource.Error(message = this.message)
        NetworkResult.Error.UnknownError -> Resource.Error(message = "Упс.. Что то сломалось")
    }
}