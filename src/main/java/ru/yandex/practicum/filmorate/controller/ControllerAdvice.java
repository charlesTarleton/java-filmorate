package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.IncorrectFilmExceptions.*;
import ru.yandex.practicum.filmorate.exception.IncorrectUserExceptions.UserBirthdayException;
import ru.yandex.practicum.filmorate.exception.IncorrectUserExceptions.UserWithIDException;
import ru.yandex.practicum.filmorate.exception.IncorrectUserExceptions.UserWithoutIDException;

import javax.validation.ValidationException;
import java.io.IOException;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler({ValidationException.class, FilmDescriptionException.class,
            FilmReleaseDateException.class, FilmDurationException.class, UserBirthdayException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse fourHundredErrorHandle(final RuntimeException exception) {
        return new ErrorResponse("Ошибка 400", "Ошибка валидации");
    }

    @ExceptionHandler({FilmWithIDException.class, FilmWithoutIDException.class, UserWithoutIDException.class,
            FilmNotContainsUserLikeException.class, UserWithIDException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse fourHundredFourErrorHandle(final RuntimeException exception) {
        return new ErrorResponse("Ошибка 404", "Искомый объект не найден");
    }

    @ExceptionHandler({IOException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse fiveHundredErrorHandle(final RuntimeException exception) {
        return new ErrorResponse("Ошибка 500", "Возникло исключение");
    }

    @Getter
    class ErrorResponse {
        private final String error;
        private final String description;

        public ErrorResponse(String error, String description) {
            this.error = error;
            this.description = description;
        }
    }
}
