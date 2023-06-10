package ru.yandex.practicum.filmorate.logEnum.mpaEnums.InfoMpaEnums;

public enum InfoMpaServiceEnum {
    MPA_SERVICE_GET_ALL_MPA("Сервис фильмов получил запрос на получение рейтингов"),
    MPA_SERVICE_GET_MPA("Сервис фильмов получил запрос на получение рейтинга с ID: ");
    private final String message;

    InfoMpaServiceEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getInfo(String value) {
        return message + value;
    }
}
