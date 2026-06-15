package vrsalex.shared.api.exception

import kotlinx.serialization.Serializable

@Serializable
enum class ErrorCode(val defaultMessage: String) {
    VALIDATION_FAILED("Данные не прошли проверку"),
    VERSION_CONFLICT("Версия устарела"),

    EMAIL_VALIDATION_FAILED("Email невалидный"),

    ITEM_NAME_BLANK("Название не может быть пустым"),
    ITEM_NAME_TOO_LONG("Название слишком длинное"),
    ITEM_DESCRIPTION_TOO_LONG("Описание слишком длинное"),

    TASK_RECURRENCE_REQUIRES_DATE("Повторяющаяся задача должна иметь дату"),
    EVENT_END_BEFORE_START("Дата окончания не может быть раньше начала"),
    EVENT_LOCATION_TOO_LONG("Название локации слишком длинное"),

    INVALID_HEX_COLOR("Некорректный цвет"),
}