package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectUserExceptions.UserBirthdayException;
import ru.yandex.practicum.filmorate.exception.IncorrectUserExceptions.UserWithIDException;
import ru.yandex.practicum.filmorate.exception.IncorrectUserExceptions.UserWithoutIDException;
import ru.yandex.practicum.filmorate.logEnum.UserEnums.ErrorUserEnum;
import ru.yandex.practicum.filmorate.logEnum.UserEnums.InfoFilmEnums.InfoUserServiceEnum;
import ru.yandex.practicum.filmorate.logEnum.UserEnums.InfoFilmEnums.InfoUserSuccessEnum;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserService {
    UserStorage userStorage;
    FilmStorage filmStorage;

    @Autowired
    public UserService(UserStorage userStorage, FilmStorage filmStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
    }

    public User addUserUS(User user) {
        log.info(InfoUserServiceEnum.REQUEST_USER_SERVICE_ADD_USER.getInfo(user.toString()));
        if (user.getID() != null) {
            log.error(ErrorUserEnum.FAIL_USER_ID.getUserError(user.getID()));
            throw new UserWithIDException();
        }
        validateUser(user);
        return userStorage.addUser(user);
    }

    public void deleteUserUS(String userIDStr) {
        log.info(InfoUserServiceEnum.REQUEST_USER_SERVICE_DELETE_USER.getInfo(userIDStr));
        Integer userID = ServiceParser.parseUserID(userIDStr);
        if (!userStorage.getUsers().containsKey(userID)) {
            log.error(ErrorUserEnum.FAIL_USER_ID.getUserError(userIDStr));
            throw new UserWithoutIDException();
        }
        userStorage.deleteUser(userID);
        for (User user : userStorage.getUsers().values()) {
            user.getUserFriends().remove(userID);
        }
        for (Film film : filmStorage.getFilms().values()) {
            film.getLikes().remove(userID);
        }
    }

    public User updateUserUS(Integer userID, User user) {
        log.info(InfoUserServiceEnum.REQUEST_USER_SERVICE_UPDATE_USER.getInfo(user.toString()));
        if (!userStorage.getUsers().containsKey(userID)) {
            log.error(ErrorUserEnum.FAIL_USER_ID.getUserError(userID));
            throw new UserWithoutIDException();
        }
        validateUser(user);
        return userStorage.updateUser(userID, user);
    }

    public User getUserUS(String userIDStr) {
        log.info(InfoUserServiceEnum.REQUEST_USER_SERVICE_GET_USER.getInfo(userIDStr));
        Integer userID = ServiceParser.parseUserID(userIDStr);
        if (!userStorage.isContainsUser(userID)) {
            log.error(ErrorUserEnum.FAIL_USER_ID.getUserError(userIDStr));
            throw new UserWithoutIDException();
        }
        log.info(InfoUserSuccessEnum.SUCCESS_GET_USERS
                .getInfo(userIDStr + "/" + userStorage.getUsers().get(userID).getLogin()));
        return userStorage.getUsers().get(userID);
    }

    public User addFriendUS(String userIDStr, String friendIDStr) {
        log.info(InfoUserServiceEnum.REQUEST_USER_SERVICE_ADD_FRIEND_USER
                .getInfo(userIDStr + "/" + friendIDStr));
        Integer userID = ServiceParser.parseUserID(userIDStr);
        Integer friendID = ServiceParser.parseUserID(friendIDStr);
        if (!userStorage.isContainsUser(userID)) {
            log.error(ErrorUserEnum.FAIL_USER_ID.getUserError(userIDStr + " пользователя"));
            throw new UserWithoutIDException();
        }
        if (!userStorage.isContainsUser(friendID)) {
            log.error(ErrorUserEnum.FAIL_USER_ID.getUserError(friendIDStr + " потенциального друга"));
            throw new UserWithoutIDException();
        }
        userStorage.getUsers().get(userID).getUserFriends().add(friendID);
        userStorage.getUsers().get(friendID).getUserFriends().add(userID);
        log.info(InfoUserSuccessEnum.SUCCESS_ADD_FRIEND_USER.getInfo(userIDStr + "/" + friendIDStr));
        return userStorage.getUsers().get(friendID);
    }

    public User deleteFriendUS(String userIDStr, String friendIDStr) {
        log.info(InfoUserServiceEnum.REQUEST_USER_SERVICE_DELETE_FRIEND_USER
                .getInfo(userIDStr + "/" + friendIDStr));
        Integer userID = ServiceParser.parseUserID(userIDStr);
        Integer friendID = ServiceParser.parseUserID(friendIDStr);
        if (!userStorage.isContainsUser(userID)) {
            log.error(ErrorUserEnum.FAIL_USER_ID.getUserError(userIDStr + " пользователя"));
            throw new UserWithoutIDException();
        }
        if (!userStorage.isContainsUser(friendID)) {
            log.error(ErrorUserEnum.FAIL_USER_ID.getUserError(friendIDStr + " потенциального друга"));
            throw new UserWithoutIDException();
        }
        userStorage.getUsers().get(userID).getUserFriends().remove(friendID);
        userStorage.getUsers().get(friendID).getUserFriends().remove(userID);
        log.info(InfoUserSuccessEnum.SUCCESS_DELETE_FRIEND_USER.getInfo(userIDStr + "/" + friendIDStr));
        return userStorage.getUsers().get(friendID);
    }

    public List<User> getUsersUS() {
        log.info(InfoUserServiceEnum.REQUEST_USER_SERVICE_GET_USERS.getMessage());
        log.info(InfoUserSuccessEnum.SUCCESS_GET_USERS.getMessage());
        return new ArrayList<>(userStorage.getUsers().values());
    }

    public List<User> getFriendsUS(String userIDStr) {
        log.info(InfoUserServiceEnum.REQUEST_USER_SERVICE_GET_FRIENDS_USER.getInfo(userIDStr));
        Integer userID = ServiceParser.parseUserID(userIDStr);
        if (!userStorage.isContainsUser(userID)) {
            throw new UserWithoutIDException();
        }
        List<User> friendsList = new ArrayList<>();
        for (Integer userFriendID : userStorage.getUsers().get(userID).getUserFriends()) {
            friendsList.add(userStorage.getUsers().get(userFriendID));
        }
        log.info(InfoUserSuccessEnum.SUCCESS_GET_FRIENDS_USER.getInfo(userIDStr));
        return friendsList;
    }

    public List<User> getCommonFriendsUS(String userIDStr, String otherIDStr) {
        log.info(InfoUserServiceEnum.REQUEST_USER_SERVICE_GET_COMMON_FRIENDS_USER
                .getInfo(userIDStr + "/" + otherIDStr));
        Integer userID = ServiceParser.parseUserID(userIDStr);
        Integer otherID = ServiceParser.parseUserID(otherIDStr);
        if (!userStorage.isContainsUser(userID) || !userStorage.isContainsUser(otherID)) {
            throw new UserWithoutIDException();
        }
        List<User> commonFriendsList = new ArrayList<>();
        for (Integer friendID : userStorage.getUsers().get(userID).getUserFriends()) {
            if (userStorage.getUsers().get(otherID).getUserFriends().contains(friendID)) {
                commonFriendsList.add(userStorage.getUsers().get(friendID));
            }
        }
        log.info(InfoUserSuccessEnum.SUCCESS_GET_COMMON_FRIENDS_USER.getInfo(userIDStr + "/" + otherIDStr));
        return commonFriendsList;
    }

    private void validateUser(@Valid User user) {
        log.info(InfoUserServiceEnum.USER_SERVICE_VALIDATE_USER.getInfo(user.toString()));
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error(ErrorUserEnum.FAIL_USER_BIRTHDAY.getUserError(user.getBirthday()));
            throw new UserBirthdayException();
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}
