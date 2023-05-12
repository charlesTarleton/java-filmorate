package ru.yandex.practicum.filmorate.exception.IncorrectFilmExceptions;

public class FilmIncorrectCountException extends RuntimeException {
    public FilmIncorrectCountException() {
        super("Ошибка числа. Полученное значение не является целым числом более 1");
    }
}
