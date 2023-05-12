package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

public interface UserStorage {
    public User addUser(User user);

    public void deleteUser(Integer userID);

    public User updateUser(Integer userID, User user);

    public boolean isContainsUser(Integer userID);

    public Map<Integer, User> getUsers();
}
