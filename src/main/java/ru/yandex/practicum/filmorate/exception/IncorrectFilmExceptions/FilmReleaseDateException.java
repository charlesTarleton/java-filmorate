package ru.yandex.practicum.filmorate.exception.IncorrectFilmExceptions;

public class FilmReleaseDateException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Ошибка даты премьеры фильма. Дата не должна быть ранее 28.12.1895";
    }
}
