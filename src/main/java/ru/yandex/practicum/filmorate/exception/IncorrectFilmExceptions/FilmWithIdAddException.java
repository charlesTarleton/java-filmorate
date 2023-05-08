package ru.yandex.practicum.filmorate.exception.IncorrectFilmExceptions;

public class FilmWithIdAddException extends RuntimeException {
    public FilmWithIdAddException() {
        super("Ошибка id фильма. У добавляемого фильма не может быть присвоенного id");
    }
}
