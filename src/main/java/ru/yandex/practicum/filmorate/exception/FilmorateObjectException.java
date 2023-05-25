package ru.yandex.practicum.filmorate.exception;

public class FilmorateObjectException extends RuntimeException {
    public FilmorateObjectException(String message) {
        super("Ошибка ID. Полученное ID не соответствует требованиям: " + message);
    }
}
