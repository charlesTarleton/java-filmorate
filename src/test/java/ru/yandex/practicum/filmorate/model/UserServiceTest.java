package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.IncorrectUserExceptions.UserBirthdayException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserServiceTest {
    @Test
    void shouldCreateUser() {
        UserController userController = new UserController();
        User testUser1 = userController.createUser(new User("user1@yandex.ru", "user1Login",
                LocalDate.of(1990, 1, 1)));
        User testUser2 = userController.createUser(new User("user2@yandex.ru", "user2Login",
                LocalDate.of(1980, 1, 1)));

        List<User> userList = userController.getAllUsers();
        assertEquals(2, userList.size());
        assertTrue(userList.contains(testUser1));
        assertTrue(userList.contains(testUser2));
    }

    @Test
    void shouldNotCreateUserDate() {
        UserController userController = new UserController();

        User testUser = userController.createUser(new User("user1@yandex.ru", "user1Login",
                LocalDate.of(1990, 1, 1)));

        assertThrows(UserBirthdayException.class,
                () -> userController.createUser(new User("user1@yandex.ru", "user1Login",
                LocalDate.of(2024, 1, 1))));

        List<User> userList = userController.getAllUsers();
        assertEquals(1, userList.size());
        assertTrue(userList.contains(testUser));
    }

    @Test
    void shouldUpdateUser() {
        UserController userController = new UserController();

        userController.createUser(new User("user1@yandex.ru", "user1Login",
                LocalDate.of(1990, 1, 1)));

        User testUser2 = userController.createUser(new User("user2@yandex.ru", "user2Login",
                LocalDate.of(1980, 1, 1)));

        User updateUser = new User("newUser1@yandex.ru", "user1Login",
                LocalDate.of(1990, 1, 1));
        updateUser.setId(1);
        userController.updateUser(updateUser);

        List<User> userList = userController.getAllUsers();
        assertEquals(2, userList.size());
        assertTrue(userList.contains(updateUser));
        assertTrue(userList.contains(testUser2));
    }

    @Test
    void shouldNotUpdateUserDate() {
        UserController userController = new UserController();

        User testUser1 = userController.createUser(new User("user1@yandex.ru", "user1Login",
                LocalDate.of(1990, 1, 1)));

        User testUser2 = userController.createUser(new User("user2@yandex.ru", "user2Login",
                LocalDate.of(1980, 1, 1)));

        User localUser = new User("user2@yandex.ru", "user2Login",
                LocalDate.of(2024, 1, 1));
        localUser.setId(2);
        assertThrows(UserBirthdayException.class, () -> userController.updateUser(localUser));

        List<User> userList = userController.getAllUsers();
        assertEquals(2, userList.size());
        assertTrue(userList.contains(testUser1));
        assertTrue(userList.contains(testUser2));
    }

    @Test
    void shouldGetAllUsers() {
        UserController userController = new UserController();

        User testUser1 = userController.createUser(new User("user1@yandex.ru", "user1Login",
                LocalDate.of(1990, 1, 1)));

        User testUser2 = userController.createUser(new User("user2@yandex.ru", "user2Login",
                LocalDate.of(1980, 1, 1)));

        List<User> userList = userController.getAllUsers();
        assertEquals(2, userList.size());
        assertTrue(userList.contains(testUser1));
        assertTrue(userList.contains(testUser2));
    }
}
