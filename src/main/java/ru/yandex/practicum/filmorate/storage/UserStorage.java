package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    public Optional<User> addUser(User user);

    public void deleteUser(long userID);

    public Optional<User> updateUser(long userID, User user);

    public boolean isContainsUser(long userID);

    public List<User> getUsers();

    public Optional<User> getUser(long userID);

    public Optional<User> addFriend(long userID, long friendID);

    public Optional<User> deleteFriend(long userID, long friendID);

    public List<User> getFriends(long userID);

    public List<User> getCommonFriends(long userID, long otherID);
}
