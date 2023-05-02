package ru.yandex.practicum.filmorate.exception.IncorrectFilmExceptions;

public class FilmDescriptionException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Ошибка длинны описания фильма. Описание должно содержать не более 200 символов";
    }
}
