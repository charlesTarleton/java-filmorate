package ru.yandex.practicum.filmorate.exception.IncorrectFilmExceptions;

public class FilmNotContainsUserLikeException extends RuntimeException {
    public FilmNotContainsUserLikeException() {
        super("Фильм не содержит лайка пользователя c указанным ID");
    }
}
