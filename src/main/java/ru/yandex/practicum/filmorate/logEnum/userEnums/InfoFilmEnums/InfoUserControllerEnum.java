package ru.yandex.practicum.filmorate.logEnum.userEnums.InfoFilmEnums;

public enum InfoUserControllerEnum {
    REQUEST_USER_CONTROLLER_ADD_USER("Контроллер пользователей получил запрос" +
            " на добавление пользователя: "),
    REQUEST_USER_CONTROLLER_DELETE_USER("Контроллер пользователей получил запрос" +
            " на удаление пользователя с ID: "),
    REQUEST_USER_CONTROLLER_UPDATE_USER("Контроллер пользователей получил запрос" +
            " на обновление пользователя: "),
    REQUEST_USER_CONTROLLER_GET_USER("Контроллер пользователей получил запрос" +
            " на получение пользователя с ID: "),
    REQUEST_USER_CONTROLLER_ADD_FRIEND_USER("Контроллер пользователей получил запрос" +
            " на добавление в друзья - кто кого ID/ID: "),
    REQUEST_USER_CONTROLLER_DELETE_FRIEND_USER("Контроллер пользователей получил запрос" +
            " на удаление из друзей - кто кого ID/ID: "),
    REQUEST_USER_CONTROLLER_GET_USERS("Контроллер пользователей получил запрос" +
            " на получение всех пользователей"),
    REQUEST_USER_CONTROLLER_GET_FRIENDS_USER("Контроллер пользователей получил запрос" +
            " на получение всех друзей пользователя с ID: "),
    REQUEST_USER_CONTROLLER_GET_COMMON_FRIENDS_USER("Контроллер пользователей получил запрос" +
            " на получение общих друзей с другим пользователем - ID/ID: ");
    private final String message;

    InfoUserControllerEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getInfo(String value) {
        return message + value;
    }
}
