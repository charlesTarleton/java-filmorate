package ru.yandex.practicum.filmorate.exception.IncorrectUserExceptions;

public class UserWithoutIdUpdateException extends RuntimeException {
    public UserWithoutIdUpdateException() {
        super("Ошибка id пользователя. В списке пользователей отсутствует указанный id");
    }
}
