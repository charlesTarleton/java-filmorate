package ru.yandex.practicum.filmorate.exception.IncorrectUserExceptions;

public class UserWithIdCreateException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Ошибка id пользователя. У создаваемого пользователя не может быть присвоенного id";
    }
}
