package ru.yandex.practicum.filmorate.logEnum.mpaEnums.InfoMpaEnums;

public enum InfoMpaSuccessEnum {
    SUCCESS_GET_ALL_MPA("Успешное получение рейтингов"),
    SUCCESS_GET_MPA("Успешное получение рейтинга с ID: ");
    private final String message;

    InfoMpaSuccessEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getInfo(String value) {
        return message + value;
    }
}
