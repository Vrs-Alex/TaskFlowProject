package vrsalex.core.database.utils

import org.jetbrains.exposed.v1.exceptions.ExposedSQLException
import org.slf4j.Logger
import vrsalex.core.exception.AppException

suspend fun <R> safeQuery(error: String, logger: Logger, code: suspend () -> R): R {
    return try {
        code()
    } catch (e: AppException) {
        throw e
    } catch (e: IllegalArgumentException) {
        throw AppException.BadRequest(e.message ?: "Неверные параметры запроса")
    } catch (e: ExposedSQLException) {
        val sqlState = e.sqlState
        val message = e.message ?: ""
        logger.error("Database error [$sqlState]: $error. Details: $message", e)
        when (sqlState) {
            "23503" -> {
                val detail = when {
                    message.contains("project_id") -> "Указанный проект не найден"
                    message.contains("area_id") -> "Указанная область не найдена"
                    message.contains("parent_id") -> "Родительский элемент не существует"
                    else -> "Нарушена целостность данных: ссылка на несуществующий объект"
                }
                throw AppException.NotFound(detail)
            }
            "23505" -> {
                throw AppException.Conflict("Объект с таким идентификатором уже существует")
            }
            "23514" -> {
                throw AppException.BadRequest("Данные не прошли проверку: ${message.substringAfter("violates check constraint").trim()}")
            }
            else -> throw AppException.BadRequest(error)
        }
    }
    catch (e: Exception) {
        logger.error("Unexpected error during query: $error", e)
        throw AppException.BadRequest(error)
    }
}