package ru.yandex.practicum.filmorate.logEnum.mpaEnums.InfoMpaEnums;

public enum InfoMpaControllerEnum {
    REQUEST_MPA_CONTROLLER_GET_ALL_MPA("Контроллер фильмов получил запрос на получение всех рейтингов"),
    REQUEST_MPA_CONTROLLER_GET_MPA("Контроллер фильмов получил запрос на получение рейтинга с ID: ");
    private final String message;

    InfoMpaControllerEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getInfo(String value) {
        return message + value;
    }
}
