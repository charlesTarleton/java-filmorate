package ru.yandex.practicum.filmorate.logEnum;

public enum InfoEnum {
    SUCCESS_ADD_FILM("Успешное добавление фильма "),
    SUCCESS_UPDATE_FILM("Успешное обновление фильма "),
    SUCCESS_CREATE_USER("Успешное создание пользователя "),
    SUCCESS_UPDATE_USER("Успешное обновление пользователя "),
    GET_NEW_FILM_ADD_REQUEST("Получено новое значение для добавления фильма: "),
    GET_NEW_FILM_UPDATE_REQUEST("Получено новое значение для обновления фильма: "),
    GET_NEW_FILM_GET_REQUEST("Получен запрос на получение всех фильмов"),
    GET_NEW_USER_CREATE_REQUEST("Получено новое значение для создания пользователя: "),
    GET_NEW_USER_UPDATE_REQUEST("Получено новое значение для обновления пользователя: "),
    CHECK_FILM("Начата процедура валидации значений фильма"),
    CHECK_USER("Начата процедура валидации значений пользователя"),
    CLEAR_FILMS("Список фильмов успешно очищен"),
    CLEAR_USERS("Список пользователей успешно очищен"),
    GET_NEW_USER_GET_REQUEST("Получен запрос на получение всех пользователей");

    private final String message;

    InfoEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getInfo(String value) {
        return message + value;
    }
}
