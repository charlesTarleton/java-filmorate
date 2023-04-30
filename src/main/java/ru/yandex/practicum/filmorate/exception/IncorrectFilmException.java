package ru.yandex.practicum.filmorate.exception;

public class IncorrectFilmException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Ошибка фильма. Подробности в логе";
    }
}
