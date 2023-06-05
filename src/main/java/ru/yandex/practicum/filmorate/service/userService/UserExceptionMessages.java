package ru.yandex.practicum.filmorate.service.userService;

public enum UserExceptionMessages {
    USER_BIRTHDAY_EXCEPTION_MESSAGE("пользователь не мог родиться в будущем"),
    USER_ID_CONTAINS_EXCEPTION_MESSAGE("в список пользователей можно добавить только пользователя без ID"),
    USER_ID_NOT_CONTAINS_EXCEPTION_MESSAGE("в списке пользователей отсутствует пользователь с указанным ID"),
    USER_FIELD_NOT_UNIQUE("в базе данных уже содержится пользователь с указанными адресом эл.почты и/или логином");
    private final String message;

    UserExceptionMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
