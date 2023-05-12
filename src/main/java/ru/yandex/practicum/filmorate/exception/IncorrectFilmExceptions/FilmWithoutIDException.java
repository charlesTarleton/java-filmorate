package ru.yandex.practicum.filmorate.exception.IncorrectFilmExceptions;

public class FilmWithoutIDException extends RuntimeException {
    public FilmWithoutIDException() {
        super("Ошибка ID фильма. В библиотеке фильмов отсутствует указанный ID");
    }
}
