package vrsalex.core.exception

import vrsalex.shared.api.exception.ErrorCode

/**
 * Проверка доменного инварианта с сообщением, предназначенным КЛИЕНТУ.
 *
 * В отличие от [require]/[IllegalArgumentException], чьё сообщение StatusPages намеренно скрывает
 * как «внутреннее», бросает [AppException.BadRequest] — его текст доходит до клиента как есть.
 *
 * Используй для бизнес-инвариантов модели; [require] оставляй для защиты от программерских ошибок.
 */
inline fun ensure(condition: Boolean, message: () -> String) {
    if (!condition) throw AppException.BadRequest(message())
}

fun ensure(condition: Boolean, code: ErrorCode) {
    if (!condition) throw AppException.BadRequest(code.defaultMessage, code)
}