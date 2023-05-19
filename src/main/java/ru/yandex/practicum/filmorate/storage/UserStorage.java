package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

public interface UserStorage {
    public User addUser(User user);

    public void deleteUser(long userID);

    public User updateUser(long userID, User user);

    public boolean isContainsUser(long userID);

    public Map<Long, User> getUsers();

    public User getUser(long userID);
}
