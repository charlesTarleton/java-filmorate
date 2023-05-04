package ru.yandex.practicum.filmorate.exception.IncorrectUserExceptions;

public class UserBirthdayException extends RuntimeException {
    public UserBirthdayException() {
        super("Ошибка даты рождения. Человек не может иметь дату рождения в будущем");
    }
}
