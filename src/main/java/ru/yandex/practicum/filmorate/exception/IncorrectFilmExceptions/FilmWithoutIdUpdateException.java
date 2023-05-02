package ru.yandex.practicum.filmorate.exception.IncorrectFilmExceptions;

public class FilmWithoutIdUpdateException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Ошибка id фильма. В библиотеке фильмов отсутствует указанный id";
    }
}
