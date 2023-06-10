package ru.yandex.practicum.filmorate.logEnum.userEnums;

public enum ErrorUserEnum {
    FAIL_USER_ID("Характеристика пользователя: \"id\". Значение: "),
    FAIL_USER_BIRTHDAY("Характеристика пользователя: \"birthday\". Значение: "),
    FAIL_USER_TABLE_VALIDATION("Характеристика пользователя: \"email/login\". Значение: ");
    private final String message;

    ErrorUserEnum(String message) {
        this.message = message;
    }

    public String getUserError(Object value) {
        if (value == null) {
            return message + null;
        } else {
            return message + value;
        }
    }
}
