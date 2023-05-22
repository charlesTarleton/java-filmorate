package ru.yandex.practicum.filmorate.service.userService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmorateObjectException;
import ru.yandex.practicum.filmorate.exception.FilmorateValidationException;
import ru.yandex.practicum.filmorate.logEnum.UserEnums.ErrorUserEnum;
import ru.yandex.practicum.filmorate.logEnum.UserEnums.InfoFilmEnums.InfoUserServiceEnum;
import ru.yandex.practicum.filmorate.logEnum.UserEnums.InfoFilmEnums.InfoUserSuccessEnum;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        if (user.getId() != null) {
            log.error(ErrorUserEnum.FAIL_USER_ID.getUserError(user.getId()));
            throw new FilmorateObjectException(UserExceptionMessages
                    .USER_ID_CONTAINS_EXCEPTION_MESSAGE.getMessage());
        }
        validateUser(user);
        return userStorage.addUser(user);
    }

    public void deleteUserUS(long userID) {
        log.info(InfoUserServiceEnum.REQUEST_USER_SERVICE_DELETE_USER.getInfo(String.valueOf(userID)));
        if (!userStorage.isContainsUser(userID)) {
            log.error(ErrorUserEnum.FAIL_USER_ID.getUserError(userID));
            throw new FilmorateObjectException(UserExceptionMessages
                    .USER_ID_NOT_CONTAINS_EXCEPTION_MESSAGE.getMessage());
        }
        userStorage.deleteUser(userID);
        userStorage.getUsers().forEach(user -> user.getUserFriends().remove(userID));
        filmStorage.getFilms().forEach(film -> film.getLikes().remove(userID));
    }

    public User updateUserUS(long userID, User user) {
        log.info(InfoUserServiceEnum.REQUEST_USER_SERVICE_UPDATE_USER.getInfo(user.toString()));
        if (!userStorage.isContainsUser(userID)) {
            log.error(ErrorUserEnum.FAIL_USER_ID.getUserError(userID));
            throw new FilmorateObjectException(UserExceptionMessages
                    .USER_ID_NOT_CONTAINS_EXCEPTION_MESSAGE.getMessage());
        }
        validateUser(user);
        return userStorage.updateUser(userID, user);
    }

    public User getUserUS(long userID) {
        log.info(InfoUserServiceEnum.REQUEST_USER_SERVICE_GET_USER.getInfo(String.valueOf(userID)));
        if (!userStorage.isContainsUser(userID)) {
            log.error(ErrorUserEnum.FAIL_USER_ID.getUserError(userID));
            throw new FilmorateObjectException(UserExceptionMessages
                    .USER_ID_NOT_CONTAINS_EXCEPTION_MESSAGE.getMessage());
        }
        return userStorage.getUser(userID);
    }

    public User addFriendUS(long userID, long friendID) {
        log.info(InfoUserServiceEnum.REQUEST_USER_SERVICE_ADD_FRIEND_USER
                .getInfo(userID + "/" + friendID));
        if (!userStorage.isContainsUser(userID)) {
            log.error(ErrorUserEnum.FAIL_USER_ID.getUserError(userID + " пользователя"));
            throw new FilmorateObjectException(UserExceptionMessages
                    .USER_ID_NOT_CONTAINS_EXCEPTION_MESSAGE.getMessage());
        }
        if (!userStorage.isContainsUser(friendID)) {
            log.error(ErrorUserEnum.FAIL_USER_ID.getUserError(friendID + " потенциального друга"));
            throw new FilmorateObjectException(UserExceptionMessages
                    .USER_ID_NOT_CONTAINS_EXCEPTION_MESSAGE.getMessage());
        }
        userStorage.getUser(userID).getUserFriends().add(friendID);
        userStorage.getUser(friendID).getUserFriends().add(userID);
        log.info(InfoUserSuccessEnum.SUCCESS_ADD_FRIEND_USER.getInfo(userID + "/" + friendID));
        return userStorage.getUser(friendID);
    }

    public User deleteFriendUS(long userID, long friendID) {
        log.info(InfoUserServiceEnum.REQUEST_USER_SERVICE_DELETE_FRIEND_USER
                .getInfo(userID + "/" + friendID));
        if (!userStorage.isContainsUser(userID)) {
            log.error(ErrorUserEnum.FAIL_USER_ID.getUserError(userID + " пользователя"));
            throw new FilmorateObjectException(UserExceptionMessages
                    .USER_ID_NOT_CONTAINS_EXCEPTION_MESSAGE.getMessage());
        }
        if (!userStorage.isContainsUser(friendID)) {
            log.error(ErrorUserEnum.FAIL_USER_ID.getUserError(friendID + " потенциального друга"));
            throw new FilmorateObjectException(UserExceptionMessages
                    .USER_ID_NOT_CONTAINS_EXCEPTION_MESSAGE.getMessage());
        }
        userStorage.getUser(userID).getUserFriends().remove(friendID);
        userStorage.getUser(friendID).getUserFriends().remove(userID);
        log.info(InfoUserSuccessEnum.SUCCESS_DELETE_FRIEND_USER.getInfo(userID + "/" + friendID));
        return userStorage.getUser(friendID);
    }

    public List<User> getUsersUS() {
        log.info(InfoUserServiceEnum.REQUEST_USER_SERVICE_GET_USERS.getMessage());
        log.info(InfoUserSuccessEnum.SUCCESS_GET_USERS.getMessage());
        return userStorage.getUsers();
    }

    public List<User> getFriendsUS(long userID) {
        log.info(InfoUserServiceEnum.REQUEST_USER_SERVICE_GET_FRIENDS_USER.getInfo(String.valueOf(userID)));
        if (!userStorage.isContainsUser(userID)) {
            throw new FilmorateObjectException(UserExceptionMessages
                    .USER_ID_NOT_CONTAINS_EXCEPTION_MESSAGE.getMessage());
        }
        log.info(InfoUserSuccessEnum.SUCCESS_GET_FRIENDS_USER.getInfo(String.valueOf(userID)));
        Set<Long> userFriends = userStorage.getUser(userID).getUserFriends();
        return userStorage.getUsers().stream()
                .filter(localUser -> userFriends.contains(localUser.getId())).collect(Collectors.toList());

    }

    public List<User> getCommonFriendsUS(long userID, long otherID) {
        log.info(InfoUserServiceEnum.REQUEST_USER_SERVICE_GET_COMMON_FRIENDS_USER
                .getInfo(userID + "/" + otherID));
        if (!userStorage.isContainsUser(userID) || !userStorage.isContainsUser(otherID)) {
            throw new FilmorateObjectException(UserExceptionMessages
                    .USER_ID_NOT_CONTAINS_EXCEPTION_MESSAGE.getMessage());
        }
        log.info(InfoUserSuccessEnum.SUCCESS_GET_COMMON_FRIENDS_USER.getInfo(userID + "/" + otherID));
        Set<Long> userFriends = userStorage.getUser(userID).getUserFriends();
        Set<Long> otherFriends = userStorage.getUser(otherID).getUserFriends();
        return userStorage.getUsers().stream().filter(localUser -> userFriends.contains(localUser.getId()) &&
                otherFriends.contains(localUser.getId())).collect(Collectors.toList());
    }

    private void validateUser(@Valid User user) {
        log.info(InfoUserServiceEnum.USER_SERVICE_VALIDATE_USER.getInfo(user.toString()));
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error(ErrorUserEnum.FAIL_USER_BIRTHDAY.getUserError(user.getBirthday()));
            throw new FilmorateValidationException(UserExceptionMessages
                    .USER_BIRTHDAY_EXCEPTION_MESSAGE.getMessage());
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}
