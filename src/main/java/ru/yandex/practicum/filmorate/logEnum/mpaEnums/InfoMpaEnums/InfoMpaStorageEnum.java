package ru.yandex.practicum.filmorate.logEnum.mpaEnums.InfoMpaEnums;

public enum InfoMpaStorageEnum {
    REQUEST_MPA_STORAGE_GET_ALL_MPA("Хранилище фильмов получило запрос на получение всех рейтингов"),
    REQUEST_MPA_STORAGE_GET_MPA("Хранилище фильмов получило запрос на получение рейтинга с ID: ");
    private final String message;

    InfoMpaStorageEnum(String message) {
        this.message = message;
    }

    public String getInfo(String value) {
        return message + value;
    }

    public String getMessage() {
        return message;
    }
}
