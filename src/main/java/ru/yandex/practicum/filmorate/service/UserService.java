package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@Getter
public class UserService {
    private final Map<Integer, User> users = new HashMap<>();

    public void add(int id, User user) {
        users.put(id, user);
    }

    public boolean usersContains(int id) {
        return users.containsKey(id);
    }
}

