package ru.yandex.practicum.filmorate.logEnum.UserEnums.InfoFilmEnums;

public enum InfoUserServiceEnum {
    REQUEST_USER_SERVICE_ADD_USER("Сервис пользователей получил запрос на добавление пользователя: "),
    REQUEST_USER_SERVICE_DELETE_USER("Сервис пользователей получил запрос на удаление пользователя с ID: "),
    REQUEST_USER_SERVICE_UPDATE_USER("Сервис пользователей получил запрос на обновление пользователя: "),
    REQUEST_USER_SERVICE_GET_USER("Сервис пользователей получил запрос на получение пользователя с ID: "),
    REQUEST_USER_SERVICE_ADD_FRIEND_USER("Сервис пользователей получил запрос на добавление в друзья" +
            " - кто кого ID/ID: "),
    REQUEST_USER_SERVICE_DELETE_FRIEND_USER("Сервис пользователей получил запрос на удаление из друзей" +
            " - кто кого ID/ID: "),
    REQUEST_USER_SERVICE_GET_USERS("Сервис пользователей получил запрос на получение всех пользователей"),
    REQUEST_USER_SERVICE_GET_FRIENDS_USER("Сервис пользователей получил запрос на получение" +
            " всех друзей пользователя с ID: "),
    REQUEST_USER_SERVICE_GET_COMMON_FRIENDS_USER("Сервис пользователей получил запрос на получение" +
            " общих друзей с другим пользователем ID/ID"),
    USER_SERVICE_VALIDATE_USER("Начата процедура валидации пользователя: "),
    USER_SERVICE_PARSE_USER_ID("Получен запрос на парсинг ID пользователя");

    private final String message;

    InfoUserServiceEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getInfo(String value) {
        return message + value;
    }
}
