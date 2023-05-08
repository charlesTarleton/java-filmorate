package ru.yandex.practicum.filmorate.exception.IncorrectFilmExceptions;

public class FilmDurationException extends RuntimeException {
    public FilmDurationException() {
        super("Ошибка длительности фильма. Фильм должен длиться более 0 минут");
    }
}
