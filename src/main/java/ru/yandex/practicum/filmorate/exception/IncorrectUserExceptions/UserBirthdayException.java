package ru.yandex.practicum.filmorate.exception.IncorrectUserExceptions;

public class UserBirthdayException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Ошибка даты рождения. Человек не может иметь дату рождения в будущем";
    }
}
