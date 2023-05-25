package ru.yandex.practicum.filmorate.exception;

import javax.validation.ValidationException;

public class FilmorateValidationException extends ValidationException {
    public FilmorateValidationException(String message) {
        super("Ошибка валидации. Полученное значение не соответствует требованиям: " + message);
    }
}
