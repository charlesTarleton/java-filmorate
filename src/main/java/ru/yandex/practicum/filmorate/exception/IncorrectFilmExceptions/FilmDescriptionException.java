package ru.yandex.practicum.filmorate.exception.IncorrectFilmExceptions;

public class FilmDescriptionException extends RuntimeException {
    public FilmDescriptionException() {
        super("Ошибка длинны описания фильма. Описание должно содержать не более 200 символов");
    }
}
