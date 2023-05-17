package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.IncorrectUserExceptions.UserBirthdayException;
import ru.yandex.practicum.filmorate.exception.IncorrectUserExceptions.UserWithoutIDException;
import ru.yandex.practicum.filmorate.inMemoryStorage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.inMemoryStorage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    private UserController userController;

    @BeforeEach
    public void createUserController() {
        UserStorage userStorage = new InMemoryUserStorage();
        FilmStorage filmStorage = new InMemoryFilmStorage();
        UserService userService = new UserService(userStorage, filmStorage);
        userController = new UserController(userService);
    }

    @Test
    void shouldCreateUser() {
        User testUser1 = userController.addUserUC(new User("user1@yandex.ru", "user1Login",
                LocalDate.of(1990, 1, 1)));
        User testUser2 = userController.addUserUC(new User("user2@yandex.ru", "user2Login",
                LocalDate.of(1980, 1, 1)));

        List<User> userList = userController.getUsersUC();
        assertEquals(2, userList.size());
        assertTrue(userList.contains(testUser1));
        assertTrue(userList.contains(testUser2));
    }

    @Test
    void shouldNotCreateUserDate() {
        User testUser = userController.addUserUC(new User("user1@yandex.ru", "user1Login",
                LocalDate.of(1990, 1, 1)));

        assertThrows(UserBirthdayException.class,
                () -> userController.addUserUC(new User("user1@yandex.ru", "user1Login",
                LocalDate.of(2024, 1, 1))));

        List<User> userList = userController.getUsersUC();
        assertEquals(1, userList.size());
        assertTrue(userList.contains(testUser));
    }

    @Test
    void shouldUpdateUser() {
        userController.addUserUC(new User("user1@yandex.ru", "user1Login",
                LocalDate.of(1990, 1, 1)));

        User testUser2 = userController.addUserUC(new User("user2@yandex.ru", "user2Login",
                LocalDate.of(1980, 1, 1)));

        User updateUser = new User("newUser1@yandex.ru", "user1Login",
                LocalDate.of(1990, 1, 1));
        updateUser.setId(1);
        userController.updateUserUC(updateUser);

        List<User> userList = userController.getUsersUC();
        assertEquals(2, userList.size());
        assertTrue(userList.contains(updateUser));
        assertTrue(userList.contains(testUser2));
    }

    @Test
    void shouldNotUpdateUserDate() {
        User testUser1 = userController.addUserUC(new User("user1@yandex.ru", "user1Login",
                LocalDate.of(1990, 1, 1)));

        User testUser2 = userController.addUserUC(new User("user2@yandex.ru", "user2Login",
                LocalDate.of(1980, 1, 1)));

        User localUser = new User("user2@yandex.ru", "user2Login",
                LocalDate.of(2024, 1, 1));
        localUser.setId(2);
        assertThrows(UserBirthdayException.class, () -> userController.updateUserUC(localUser));

        List<User> userList = userController.getUsersUC();
        assertEquals(2, userList.size());
        assertTrue(userList.contains(testUser1));
        assertTrue(userList.contains(testUser2));
    }

    @Test
    void shouldGetUsers() {
        User testUser1 = userController.addUserUC(new User("user1@yandex.ru", "user1Login",
                LocalDate.of(1990, 1, 1)));

        User testUser2 = userController.addUserUC(new User("user2@yandex.ru", "user2Login",
                LocalDate.of(1980, 1, 1)));

        List<User> userList = userController.getUsersUC();
        assertEquals(2, userList.size());
        assertTrue(userList.contains(testUser1));
        assertTrue(userList.contains(testUser2));
    }

    @Test
    void shouldDeleteUser() {
        userController.addUserUC(new User("user1@yandex.ru", "user1Login",
                LocalDate.of(1990, 1, 1)));

        User testUser2 = userController.addUserUC(new User("user2@yandex.ru", "user2Login",
                LocalDate.of(1980, 1, 1)));
        userController.deleteUserUC("1");
        List<User> userList = userController.getUsersUC();
        assertEquals(1, userList.size());
        assertTrue(userList.contains(testUser2));
    }

    @Test
    void shouldNotDeleteUser() {
        User testUser1 = userController.addUserUC(new User("user1@yandex.ru", "user1Login",
                LocalDate.of(1990, 1, 1)));

        User testUser2 = userController.addUserUC(new User("user2@yandex.ru", "user2Login",
                LocalDate.of(1980, 1, 1)));
        assertThrows(UserWithoutIDException.class, () -> userController.deleteUserUC("3"));
        assertThrows(UserWithoutIDException.class, () -> userController.deleteUserUC(null));

        List<User> userList = userController.getUsersUC();
        assertEquals(2, userList.size());
        assertTrue(userList.contains(testUser1));
        assertTrue(userList.contains(testUser2));
    }

    @Test
    void shouldGetUser() {
        User testUser1 = userController.addUserUC(new User("user1@yandex.ru", "user1Login",
                LocalDate.of(1990, 1, 1)));

        User testUser2 = userController.addUserUC(new User("user2@yandex.ru", "user2Login",
                LocalDate.of(1980, 1, 1)));
        testUser1.setId(1);
        testUser2.setId(2);
        assertEquals(testUser1, userController.getUserUC("1"));
        assertEquals(testUser2, userController.getUserUC("2"));
    }

    @Test
    void shouldNotGetUser() {
        User testUser1 = userController.addUserUC(new User("user1@yandex.ru", "user1Login",
                LocalDate.of(1990, 1, 1)));

        User testUser2 = userController.addUserUC(new User("user2@yandex.ru", "user2Login",
                LocalDate.of(1980, 1, 1)));
        testUser1.setId(1);
        testUser2.setId(2);
        assertThrows(UserWithoutIDException.class, () -> userController.getUserUC("3"));
        assertThrows(UserWithoutIDException.class, () -> userController.getUserUC(null));
    }

    @Test
    void shouldAddFriend() {
        User testUser1 = userController.addUserUC(new User("user1@yandex.ru", "user1Login",
                LocalDate.of(1990, 1, 1)));

        User testUser2 = userController.addUserUC(new User("user2@yandex.ru", "user2Login",
                LocalDate.of(1980, 1, 1)));
        testUser1.setId(1);
        testUser1.getUserFriends().add(2);
        testUser2.setId(2);
        testUser2.getUserFriends().add(1);

        userController.addFriendUC("1", "2");
        assertEquals(1, userController.getUserUC("1").getUserFriends().size());
        assertTrue(userController.getUserUC("1").getUserFriends().contains(2));
        assertEquals(1, userController.getUserUC("2").getUserFriends().size());
        assertTrue(userController.getUserUC("2").getUserFriends().contains(1));
    }

    @Test
    void shouldNotAddFriend() {
        userController.addUserUC(new User("user1@yandex.ru", "user1Login",
                LocalDate.of(1990, 1, 1)));

        userController.addUserUC(new User("user2@yandex.ru", "user2Login",
                LocalDate.of(1980, 1, 1)));

        assertThrows(UserWithoutIDException.class, () -> userController.addFriendUC("3", "2"));
        assertEquals(0, userController.getUserUC("1").getUserFriends().size());
        assertFalse(userController.getUserUC("1").getUserFriends().contains(2));
        assertEquals(0, userController.getUserUC("2").getUserFriends().size());
        assertFalse(userController.getUserUC("2").getUserFriends().contains(1));

        assertThrows(UserWithoutIDException.class, () -> userController.addFriendUC(null, "2"));
        assertEquals(0, userController.getUserUC("1").getUserFriends().size());
        assertFalse(userController.getUserUC("1").getUserFriends().contains(2));
        assertEquals(0, userController.getUserUC("2").getUserFriends().size());
        assertFalse(userController.getUserUC("2").getUserFriends().contains(1));
    }

    @Test
    void shouldDeleteFriend() {
        userController.addUserUC(new User("user1@yandex.ru", "user1Login",
                LocalDate.of(1990, 1, 1)));

        userController.addUserUC(new User("user2@yandex.ru", "user2Login",
                LocalDate.of(1980, 1, 1)));
        userController.addFriendUC("1", "2");

        userController.deleteFriendUC("2", "1");
        assertEquals(0, userController.getUserUC("1").getUserFriends().size());
        assertFalse(userController.getUserUC("1").getUserFriends().contains(2));
        assertEquals(0, userController.getUserUC("2").getUserFriends().size());
        assertFalse(userController.getUserUC("2").getUserFriends().contains(1));
    }

    @Test
    void shouldNotDeleteFriend() {
        userController.addUserUC(new User("user1@yandex.ru", "user1Login",
                LocalDate.of(1990, 1, 1)));

        userController.addUserUC(new User("user2@yandex.ru", "user2Login",
                LocalDate.of(1980, 1, 1)));
        userController.addFriendUC("1", "2");

        assertThrows(UserWithoutIDException.class, () -> userController.deleteFriendUC(null, "1"));
        assertEquals(1, userController.getUserUC("1").getUserFriends().size());
        assertTrue(userController.getUserUC("1").getUserFriends().contains(2));
        assertEquals(1, userController.getUserUC("2").getUserFriends().size());
        assertTrue(userController.getUserUC("2").getUserFriends().contains(1));

        assertThrows(UserWithoutIDException.class, () -> userController.deleteFriendUC("4", "1"));
        assertEquals(1, userController.getUserUC("1").getUserFriends().size());
        assertTrue(userController.getUserUC("1").getUserFriends().contains(2));
        assertEquals(1, userController.getUserUC("2").getUserFriends().size());
        assertTrue(userController.getUserUC("2").getUserFriends().contains(1));
    }

    @Test
    void shouldGetFriends() {
        User testUser1 = userController.addUserUC(new User("user1@yandex.ru", "user1Login",
                LocalDate.of(1990, 1, 1)));

        User testUser2 = userController.addUserUC(new User("user2@yandex.ru", "user2Login",
                LocalDate.of(1980, 1, 1)));
        userController.addFriendUC("1", "2");
        testUser1.setId(1);
        testUser2.setId(2);

        List<User> friendsList = userController.getFriendsUC("1");
        assertEquals(1, friendsList.size());
        assertEquals(friendsList.get(0), testUser2);

        friendsList = userController.getFriendsUC("2");
        assertEquals(1, friendsList.size());
        assertEquals(friendsList.get(0), testUser1);
    }

    @Test
    void shouldNotGetFriends() {
        User testUser1 = userController.addUserUC(new User("user1@yandex.ru", "user1Login",
                LocalDate.of(1990, 1, 1)));

        User testUser2 = userController.addUserUC(new User("user2@yandex.ru", "user2Login",
                LocalDate.of(1980, 1, 1)));
        userController.addFriendUC("1", "2");
        testUser1.setId(1);
        testUser2.setId(2);

        assertThrows(UserWithoutIDException.class, () -> userController.getFriendsUC("4"));
        assertThrows(UserWithoutIDException.class, () -> userController.getFriendsUC(null));
    }

    @Test
    void shouldGetCommonFriends() {
        userController.addUserUC(new User("user1@yandex.ru", "user1Login",
                LocalDate.of(1990, 1, 1)));

        User testUser2 = userController.addUserUC(new User("user2@yandex.ru", "user2Login",
                LocalDate.of(1980, 1, 1)));

        userController.addUserUC(new User("user3@yandex.ru", "user3Login",
                LocalDate.of(1990, 1, 1)));

        userController.addUserUC(new User("user4@yandex.ru", "user4Login",
                LocalDate.of(1980, 1, 1)));
        testUser2.setId(2);
        userController.addFriendUC("1", "2");
        userController.addFriendUC("3", "2");
        userController.addFriendUC("3", "4");

        List<User> commonFriendsList = userController.getCommonFriendsUC("1", "3");
        assertEquals(1, commonFriendsList.size());
        assertTrue(commonFriendsList.contains(testUser2));
    }

    @Test
    void shouldNotGetCommonFriends() {
        userController.addUserUC(new User("user1@yandex.ru", "user1Login",
                LocalDate.of(1990, 1, 1)));

        userController.addUserUC(new User("user2@yandex.ru", "user2Login",
                LocalDate.of(1980, 1, 1)));

        userController.addUserUC(new User("user3@yandex.ru", "user3Login",
                LocalDate.of(1990, 1, 1)));

        userController.addUserUC(new User("user4@yandex.ru", "user4Login",
                LocalDate.of(1980, 1, 1)));
        userController.addFriendUC("1", "2");
        userController.addFriendUC("3", "2");
        userController.addFriendUC("3", "4");

        assertThrows(UserWithoutIDException.class, () -> userController.getCommonFriendsUC("5", "3"));
        assertThrows(UserWithoutIDException.class, () -> userController.getCommonFriendsUC(null, "3"));
    }
}