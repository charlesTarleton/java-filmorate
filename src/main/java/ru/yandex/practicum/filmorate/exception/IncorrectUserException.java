package ru.yandex.practicum.filmorate.exception;

public class IncorrectUserException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Ошибка пользователя. Подробности в логе";
    }
}
