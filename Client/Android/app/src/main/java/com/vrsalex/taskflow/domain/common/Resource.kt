package com.vrsalex.taskflow.domain.common

import com.vrsalex.network.public.common.NetworkResult

sealed interface Resource <out T> {

    data class Success<T>(val data: T): Resource<T>

    sealed interface Error: Resource<Nothing> {
        data object NoInternet: Error
        data object ServerError: Error
        data class HttpError(val code: Int, val msg: String): Error
    }

}

suspend fun <T, R> NetworkResult<T>.toResource(
    mapper: suspend (T) -> R
): Resource<R> = when(this) {
    is NetworkResult.Success -> Resource.Success(mapper(this.data))
    NetworkResult.Error.NetworkError -> Resource.Error.NoInternet
    is NetworkResult.Error.HttpError -> Resource.Error.HttpError(this.code, this.message)
    NetworkResult.Error.UnknownError -> Resource.Error.ServerError
}