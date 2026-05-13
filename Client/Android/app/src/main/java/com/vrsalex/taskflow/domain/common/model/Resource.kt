package com.vrsalex.taskflow.domain.common.model

import com.vrsalex.network.public.common.NetworkResult

sealed interface Resource<out T> {

    data class Success<T>(val data: T) : Resource<T>

    sealed class Failure(open val message: String = "") : Resource<Nothing> {
        data object Unavailable : Failure()
        data object Unauthorized : Failure()
        data class Conflict(override val message: String) : Failure(message)
        data class Error(override val message: String) : Failure(message)
    }
}

suspend fun <T, D> NetworkResult<T>.toResource(mapper: suspend (data: T) -> D): Resource<D> =
    when (this) {
        is NetworkResult.Success -> Resource.Success(mapper(data))

        NetworkResult.Error.NetworkError,
        NetworkResult.Error.UnknownError -> Resource.Failure.Unavailable

        is NetworkResult.Error.HttpError -> when (code) {
            401 -> Resource.Failure.Unauthorized
            409 -> Resource.Failure.Conflict(message)
            in 500..599 -> Resource.Failure.Unavailable
            else -> Resource.Failure.Error(message)
        }
    }
