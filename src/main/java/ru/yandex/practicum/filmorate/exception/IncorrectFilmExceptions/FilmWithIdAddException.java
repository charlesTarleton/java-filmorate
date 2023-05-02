package ru.yandex.practicum.filmorate.exception.IncorrectFilmExceptions;

public class FilmWithIdAddException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Ошибка id фильма. У добавляемого фильма не может быть присвоенного id";
    }
}
