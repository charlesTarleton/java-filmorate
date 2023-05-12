package ru.yandex.practicum.filmorate.exception.IncorrectFilmExceptions;

public class FilmWithIDException extends RuntimeException {
    public FilmWithIDException() {
        super("Ошибка ID фильма. У добавляемого фильма не может быть присвоенного ID");
    }
}
