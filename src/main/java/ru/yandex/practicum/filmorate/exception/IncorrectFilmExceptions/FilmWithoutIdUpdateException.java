package ru.yandex.practicum.filmorate.exception.IncorrectFilmExceptions;

public class FilmWithoutIdUpdateException extends RuntimeException {
    public FilmWithoutIdUpdateException() {
        super("Ошибка id фильма. В библиотеке фильмов отсутствует указанный id");
    }
}
