package ru.yandex.practicum.filmorate.logEnum.mpaEnums;

public enum ErrorMpaEnum {
    FAIL_MPA_ID("Характеристика возрастного рейтинга: \"id\". Значение: ");
    private final String message;

    ErrorMpaEnum(String message) {
        this.message = message;
    }

    public String getMpaError(Object value) {
        if (value == null) {
            return message + null;
        } else {
            return message + value;
        }
    }
}