package ru.yandex.practicum.filmorate.model;

import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@lombok.Data
public class User {
    private Integer id;
    @NotNull
    @Email
    @NotBlank
    private final String email;
    @NotNull
    @NotBlank
    private final String login;
    private String name;
    @NotNull
    private final LocalDate birthday;

    public static User addUser(User user) {
        UserService.users.put(user.id, user);
        return user;
    }

    public static User updateUser(User user) {
        UserService.users.put(user.id, user);
        return user;
    }

    public static List<User> getAllUsers() {
        return new ArrayList<>(UserService.users.values());
    }
}
