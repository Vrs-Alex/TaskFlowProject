package com.vrsalex.network.public.common

sealed interface NetworkResult <out T>{

    data class Success<out T>(val data: T) : NetworkResult<T>

    sealed interface Error : NetworkResult<Nothing> {
        data object NetworkError : Error
        data class HttpError(val code: Int, val message: String) : Error
        data object UnknownError : Error
    }

}