package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.IncorrectUserException;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserServiceTest {
    private UserController userController;
    private User testUser1;
    private User testUser2;
    private User localUser;


    @BeforeEach
    public void start() {
        userController = new UserController();

        testUser1 = userController.createUser(new User("user1@yandex.ru", "user1Login",
                LocalDate.of(1990, 1, 1)));

        testUser2 = userController.createUser(new User("user2@yandex.ru", "user2Login",
                LocalDate.of(1980, 1, 1)));
    }

    @AfterEach
    public  void end() {
        UserController.usersId = 1;
        UserService.users.clear();
    }

    @Test
    void shouldCreateUser() {
        localUser = userController.createUser(new User("user3@yandex.ru", "user3Login",
                LocalDate.of(1990, 1, 1)));

        List<User> userList = userController.getAllUsers();
        assertEquals(3, userList.size());
        assertTrue(userList.contains(localUser));
    }

    @Test
    void shouldNotCreateUserDate() {
        assertThrows(IncorrectUserException.class,
                () -> userController.createUser(new User("user1@yandex.ru", "user1Login",
                LocalDate.of(2024, 1, 1))));

        List<User> userList = userController.getAllUsers();
        assertEquals(2, userList.size());
        assertTrue(userList.contains(testUser1));
        assertTrue(userList.contains(testUser2));
    }

    @Test
    void shouldUpdateUser() {
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
        localUser = new User("user2@yandex.ru", "user2Login",
                LocalDate.of(2024, 1, 1));
        localUser.setId(2);
        assertThrows(IncorrectUserException.class, () -> userController.updateUser(localUser));

        List<User> userList = userController.getAllUsers();
        assertEquals(2, userList.size());
        assertTrue(userList.contains(testUser1));
        assertTrue(userList.contains(testUser2));
    }

    @Test
    void shouldGetAllUsers() {
        List<User> userList = userController.getAllUsers();
        assertEquals(2, userList.size());
        assertTrue(userList.contains(testUser1));
        assertTrue(userList.contains(testUser2));
    }
}
