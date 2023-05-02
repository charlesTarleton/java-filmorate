package ru.yandex.practicum.filmorate.exception.IncorrectFilmExceptions;

public class FilmDurationException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Ошибка длительности фильма. Фильм должен длиться более 0 минут";
    }
}
