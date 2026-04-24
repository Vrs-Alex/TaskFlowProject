package com.vrsalex.taskflow.domain.common.model

import com.vrsalex.network.public.common.NetworkResult

sealed interface Resource<out T> {

    data class Success<T>(val data: T) : Resource<T>

    data class Error(val message: String) : Resource<Nothing>
}

/**
 * Преобразует сетевой ответ в бизнес сущность.
 * Маппер [mapper] срабатывает только при успешном ответе
 */
suspend fun <T, D> NetworkResult<T>.toResource(mapper: suspend (data: T) -> D): Resource<D> {
    return when (this) {
        is NetworkResult.Success -> Resource.Success(mapper(data))
        NetworkResult.Error.NetworkError -> Resource.Error(message = "Нет сети")
        is NetworkResult.Error.HttpError -> Resource.Error(message = this.message)
        NetworkResult.Error.UnknownError -> Resource.Error(message = "Упс.. Что то сломалось")
    }
}