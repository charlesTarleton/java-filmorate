package ru.yandex.practicum.filmorate.logEnum.UserEnums.InfoFilmEnums;

public enum InfoUserStorageEnum {
    REQUEST_USER_STORAGE_ADD_USER("Хранилище пользователей получило запрос на добавление пользователя: "),
    REQUEST_USER_STORAGE_DELETE_USER("Хранилище пользователей получило запрос" +
            " на удаление пользователя с ID: "),
    REQUEST_USER_STORAGE_UPDATE_USER("Хранилище пользователей получило запрос на обновление пользователя: "),
    REQUEST_USER_STORAGE_CONTAINS_USER("Хранилище пользователей получило запрос на проверку" +
            " наличия пользователя с ID: ");

    private final String message;

    InfoUserStorageEnum(String message) {
        this.message = message;
    }

    public String getInfo(String value) {
        return message + value;
    }

}
