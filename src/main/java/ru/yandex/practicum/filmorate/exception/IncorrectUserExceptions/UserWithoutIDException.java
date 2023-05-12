package ru.yandex.practicum.filmorate.exception.IncorrectUserExceptions;

public class UserWithoutIDException extends RuntimeException {
    public UserWithoutIDException() {
        super("Ошибка ID пользователя. В списке пользователей отсутствует указанный ID");
    }
}
