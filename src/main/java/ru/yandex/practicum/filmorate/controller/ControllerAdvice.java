package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.FilmorateObjectException;
import ru.yandex.practicum.filmorate.logEnum.ControllerLogEnum;

import javax.validation.ValidationException;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {
    private static final String ERROR_400 = "Ошибка 400";
    private static final String ERROR_400_DESCRIPTION = "Ошибка валидации";

    private static final String ERROR_404 = "Ошибка 404";
    private static final String ERROR_404_DESCRIPTION = "Искомый объект не найден";

    private static final String ERROR_500 = "Ошибка 500";
    private static final String ERROR_500_DESCRIPTION = "Возникло исключение";

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse fourHundredErrorHandle(final ValidationException exception) {
        log.info(ControllerLogEnum.FOUR_HUNDRED_ERROR_HANDLE.getInfo(exception));
        return new ErrorResponse(ERROR_400, ERROR_400_DESCRIPTION);
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse fourHundredFourErrorHandle(final FilmorateObjectException exception) {
        log.info(ControllerLogEnum.FOUR_HUNDRED_FOUR_ERROR_HANDLE.getInfo(exception));
        return new ErrorResponse(ERROR_404, ERROR_404_DESCRIPTION);
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse fiveHundredErrorHandle(final Throwable exception) {
        log.info(ControllerLogEnum.FIVE_HUNDRED_ERROR_HANDLE.getInfo(exception));
        return new ErrorResponse(ERROR_500, ERROR_500_DESCRIPTION);
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
