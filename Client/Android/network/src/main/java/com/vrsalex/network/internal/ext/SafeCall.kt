package com.vrsalex.network.internal.ext

import android.util.Log
import com.vrsalex.network.public.common.NetworkResult
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlinx.coroutines.CancellationException
import kotlinx.io.IOException
import vrsalex.shared.api.exception.ServerErrorResponse

internal suspend inline fun <reified T> safeCall(
    block: suspend () -> HttpResponse
): NetworkResult<T> {
    return try {
        val response = block()
        if (response.status.isSuccess()){
            NetworkResult.Success(response.body<T>())
        } else {
            val error = runCatching { response.body<ServerErrorResponse>() }.getOrNull()
            if (error != null) NetworkResult.Error.HttpError(error.status.value, error.message ?: "Ошибка. Попробуйте позже")
            else NetworkResult.Error.HttpError(response.status.value, response.status.description)
        }
    } catch (e: IOException) {
        NetworkResult.Error.NetworkError
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        NetworkResult.Error.UnknownError
    }

}
