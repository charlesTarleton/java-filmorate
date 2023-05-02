package ru.yandex.practicum.filmorate.exception.IncorrectUserExceptions;

public class UserWithoutIdUpdateException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Ошибка id пользователя. В списке пользователей отсутствует указанный id";
    }
}
