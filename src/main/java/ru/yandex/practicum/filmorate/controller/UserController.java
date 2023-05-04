package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.logEnum.ErrorEnum;
import ru.yandex.practicum.filmorate.logEnum.InfoEnum;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.IncorrectUserExceptions.*;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private int usersId = 1;
    private final UserService userService = new UserService();

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info(InfoEnum.GET_NEW_USER_CREATE_REQUEST.getInfo(user.toString()));
        if (user.getId() != null) {
            log.error(ErrorEnum.FAIL_ID.getUserError(user.getId(), user.getLogin(), user.getId()));
            throw new UserWithIdCreateException();
        }
        checkUser(user);
        user.setId(usersId++);
        log.info(InfoEnum.SUCCESS_CREATE_USER.getInfo(user.getName()));
        userService.add(user.getId(), user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info(InfoEnum.GET_NEW_USER_UPDATE_REQUEST.getInfo(user.toString()));
        if (!userService.usersContains(user.getId())) {
            log.error(ErrorEnum.FAIL_ID.getUserError(user.getId(), user.getLogin(), user.getId()));
            throw new UserWithoutIdUpdateException();
        }
        checkUser(user);
        log.info(InfoEnum.SUCCESS_UPDATE_USER.getInfo(user.getName()));
        userService.add(user.getId(), user);
        return user;
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info(InfoEnum.GET_NEW_USER_GET_REQUEST.getMessage());
        return new ArrayList<>(userService.getUsers().values());
    }

    private void checkUser(@Valid User user) {
        log.info(InfoEnum.CHECK_USER.getMessage());
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error(ErrorEnum.FAIL_BIRTHDAY_USER.getUserError(user.getId(), user.getLogin(), user.getBirthday()));
            throw new UserBirthdayException();
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}