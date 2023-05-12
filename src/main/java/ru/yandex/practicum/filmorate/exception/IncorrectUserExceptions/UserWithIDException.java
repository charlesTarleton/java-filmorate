package ru.yandex.practicum.filmorate.exception.IncorrectUserExceptions;

public class UserWithIDException extends RuntimeException {
    public UserWithIDException() {
        super("Ошибка ID пользователя. У создаваемого пользователя не может быть присвоенного ID");
    }
}
