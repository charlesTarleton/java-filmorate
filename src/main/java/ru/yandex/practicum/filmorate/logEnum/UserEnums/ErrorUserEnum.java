package ru.yandex.practicum.filmorate.logEnum.UserEnums;

public enum ErrorUserEnum {
    FAIL_USER_ID("Характеристика пользователя: \"id\". Значение: "),
    FAIL_USER_BIRTHDAY("Характеристика пользователя: \"birthday\". Значение: ");
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
