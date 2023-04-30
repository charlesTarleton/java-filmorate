package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    public static int usersId = 1;

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        User newUser;
        try {
            if (user.getId() != null) {
                throw new RuntimeException("Попытка создать ранее созданного пользователя");
            }
            user.setId(usersId++);
            newUser = checkUser(user);
        } catch (RuntimeException e) {
            usersId--;
            log.error(e.getMessage());
            throw new IncorrectUserException();
        }
        log.info("Успешное создание пользователя {}", user);
        return User.addUser(newUser);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        User newUser;
        try {
            if (!UserService.users.containsKey(user.getId())) {
                throw new RuntimeException("UserId=" + user.getId() +
                        ". Попытка обновить несуществующего пользователя");
            }
            newUser = checkUser(user);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            throw new IncorrectUserException();
        }
        log.info("Успешное обновление пользователя с Id {} на {}", user.getId(), user);
        return User.updateUser(newUser);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return User.getAllUsers();
    }

    private User checkUser(@Valid User user) {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new RuntimeException("UserId=" + user.getId() + ". Значение даты рождения пользователя равно \"" +
                    user.getBirthday() + "\"");
        }

        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        return user;
    }
}