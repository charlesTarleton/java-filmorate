package ru.yandex.practicum.filmorate.logEnum.UserEnums.InfoFilmEnums;

public enum InfoUserStorageEnum {
    REQUEST_USER_STORAGE_ADD_USER("Хранилище пользователей получило запрос на добавление пользователя: "),
    REQUEST_USER_STORAGE_DELETE_USER("Хранилище пользователей получило запрос" +
            " на удаление пользователя с ID: "),
    REQUEST_USER_STORAGE_UPDATE_USER("Хранилище пользователей получило запрос на обновление пользователя: "),
    REQUEST_USER_STORAGE_CONTAINS_USER("Хранилище пользователей получило запрос на проверку" +
            " наличия пользователя с ID: "),
    REQUEST_USER_STORAGE_GET_USERS("Хранилище пользователей получило запрос на получение всех пользователей"),
    REQUEST_USER_STORAGE_GET_USER("Хранилище пользователей получило запрос на получение" +
            " пользователя с ID: "),
    REQUEST_USER_STORAGE_ADD_FRIEND_USER("Хранилище пользователей получило запрос на " +
            "добавление друга ID/ID: "),
    REQUEST_USER_STORAGE_DELETE_FRIEND_USER("Хранилище пользователей получило запрос на " +
            "удаление друга ID/ID: "),
    REQUEST_USER_STORAGE_GET_COMMON_FRIENDS_USER("Хранилище пользователей получило запрос на получение " +
            "общих друзей ID/ID: ");
    private final String message;

    InfoUserStorageEnum(String message) {
        this.message = message;
    }

    public String getInfo(String value) {
        return message + value;
    }

    public String getMessage() {
        return message;
    }
}
