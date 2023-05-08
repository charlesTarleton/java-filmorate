package ru.yandex.practicum.filmorate.exception.IncorrectFilmExceptions;

public class FilmReleaseDateException extends RuntimeException {
    public FilmReleaseDateException() {
        super("Ошибка даты премьеры фильма. Дата не должна быть ранее 28.12.1895");
    }
}
