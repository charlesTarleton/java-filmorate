package ru.yandex.practicum.filmorate.logEnum.UserEnums.InfoFilmEnums;

public enum InfoUserSuccessEnum {
    SUCCESS_ADD_USER("Успешное добавление пользователя с ID/логином: "),
    SUCCESS_DELETE_USER("Успешное удаление пользователя с ID: "),
    SUCCESS_UPDATE_USER("Успешное обновление пользователя с ID/логином: "),
    SUCCESS_GET_USER("Успешное получение пользователя с ID/логином: "),
    SUCCESS_ADD_FRIEND_USER("Успешное добавление в друзья - кто кого ID/ID: "),
    SUCCESS_DELETE_FRIEND_USER("Успешное удаление из друзей - кто кого ID/ID: "),
    SUCCESS_GET_USERS("Успешное получение всех пользователей"),
    SUCCESS_GET_FRIENDS_USER("Успешное получение всех друзей пользователя с ID: "),
    SUCCESS_GET_COMMON_FRIENDS_USER("Успешное получение общих друзей пользователей ID/ID: ");
    private final String message;

    InfoUserSuccessEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getInfo(String value) {
        return message + value;
    }
}
