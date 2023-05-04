package ru.yandex.practicum.filmorate.exception.IncorrectUserExceptions;

public class UserWithIdCreateException extends RuntimeException {
    public UserWithIdCreateException() {
        super("Ошибка id пользователя. У создаваемого пользователя не может быть присвоенного id");
    }
}
