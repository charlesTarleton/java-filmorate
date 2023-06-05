package ru.yandex.practicum.filmorate.service.userService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dbStorage.DatabaseEnums.TblFrndshpSt;
import ru.yandex.practicum.filmorate.dbStorage.DatabaseEnums.TblUsrFrndshp;
import ru.yandex.practicum.filmorate.dbStorage.DatabaseEnums.TblUsrs;
import ru.yandex.practicum.filmorate.exception.FilmorateObjectException;
import ru.yandex.practicum.filmorate.exception.FilmorateValidationException;
import ru.yandex.practicum.filmorate.logEnum.UserEnums.ErrorUserEnum;
import ru.yandex.practicum.filmorate.logEnum.UserEnums.InfoFilmEnums.InfoUserServiceEnum;
import ru.yandex.practicum.filmorate.logEnum.UserEnums.InfoFilmEnums.InfoUserSuccessEnum;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate();

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
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
        jdbcTemplate.queryForRowSet(
                "INSERT INTO " + TblUsrFrndshp.DB_TABLE_USER_FRIENDSHIP.getDB() + " (" +
                        TblUsrs.DB_FIELD_USER_ID.getDB() + ", " +
                        TblUsrFrndshp.DB_FIELD_FRIEND_USER_ID.getDB() + ", " +
                        TblFrndshpSt.DB_FIELD_FRIENDSHIP_STATUS_ID.getDB() + ") " +
                    "VALUES ('?', '?', '2'); " +
                    "INSERT INTO " + TblUsrFrndshp.DB_TABLE_USER_FRIENDSHIP.getDB() + " (" +
                        TblUsrs.DB_FIELD_USER_ID.getDB() + ", " +
                        TblUsrFrndshp.DB_FIELD_FRIEND_USER_ID.getDB() + ", " +
                        TblFrndshpSt.DB_FIELD_FRIENDSHIP_STATUS_ID.getDB() + ") " +
                    "VALUES ('?', '?', '2')", userID, friendID, friendID, userID);
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
        jdbcTemplate.queryForRowSet(
                "DELETE FROM " + TblUsrFrndshp.DB_TABLE_USER_FRIENDSHIP.getDB() + " " +
                    "WHERE " + TblUsrs.DB_FIELD_USER_ID.getDB() + " = ? AND " +
                        TblUsrFrndshp.DB_FIELD_FRIEND_USER_ID.getDB() + " = ?; " +
                    "DELETE FROM " + TblUsrFrndshp.DB_TABLE_USER_FRIENDSHIP.getDB() + " " +
                    "WHERE " + TblUsrs.DB_FIELD_USER_ID.getDB() + " = ? AND " +
                        TblUsrFrndshp.DB_FIELD_FRIEND_USER_ID.getDB() + " = ?",
                userID, friendID, friendID, userID);
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
        return jdbcTemplate.query(
                "SELECT uf." + TblUsrFrndshp.DB_FIELD_FRIEND_USER_ID.getDB() + ", u.* " +
                "FROM " + TblUsrFrndshp.DB_TABLE_USER_FRIENDSHIP.getDB() + " AS uf " +
                "LEFT OUTER JOIN " + TblUsrs.DB_TABLE_USERS.getDB() + " AS u " +
                "ON uf." + TblUsrFrndshp.DB_FIELD_FRIEND_USER_ID.getDB() + " = u." +
                        TblUsrs.DB_FIELD_USER_ID.getDB() + " " +
                "WHERE " + TblUsrs.DB_FIELD_USER_ID.getDB() + " = ?", new Long[]{userID}, (rs, rowNum) -> {
            User user = new User(
                    rs.getString(TblUsrs.DB_FIELD_USER_EMAIL.getDB()),
                    rs.getString(TblUsrs.DB_FIELD_USER_LOGIN.getDB()),
                    rs.getDate(TblUsrs.DB_FIELD_USER_BIRTHDAY.getDB()).toLocalDate());
            user.setId(userID);
            user.setName(rs.getString(TblUsrs.DB_FIELD_USER_NAME.getDB()));
            return user;
        });
    }

    public List<User> getCommonFriendsUS(long userID, long otherID) {
        log.info(InfoUserServiceEnum.REQUEST_USER_SERVICE_GET_COMMON_FRIENDS_USER
                .getInfo(userID + "/" + otherID));
        if (!userStorage.isContainsUser(userID) || !userStorage.isContainsUser(otherID)) {
            throw new FilmorateObjectException(UserExceptionMessages
                    .USER_ID_NOT_CONTAINS_EXCEPTION_MESSAGE.getMessage());
        }
        log.info(InfoUserSuccessEnum.SUCCESS_GET_COMMON_FRIENDS_USER.getInfo(userID + "/" + otherID));
        return jdbcTemplate.query(
                "SELECT " + TblUsrFrndshp.DB_FIELD_FRIEND_USER_ID.getDB() + " " +
                "FROM " + TblUsrFrndshp.DB_TABLE_USER_FRIENDSHIP.getDB() + " " +
                "WHERE " + TblUsrFrndshp.DB_FIELD_FRIEND_USER_ID.getDB() + " IN (?, ?) " +
                "GROUP BY " + TblUsrFrndshp.DB_FIELD_FRIEND_USER_ID.getDB() + " " +
                "HAVING COUNT(*) = 2", new Long[]{userID, otherID}, (rs, rowNum) -> {
            User user = new User(
                    rs.getString(TblUsrs.DB_FIELD_USER_EMAIL.getDB()),
                    rs.getString(TblUsrs.DB_FIELD_USER_LOGIN.getDB()),
                    rs.getDate(TblUsrs.DB_FIELD_USER_BIRTHDAY.getDB()).toLocalDate());
            user.setId(rs.getLong(TblUsrs.DB_TABLE_USERS.getDB()));
            user.setName(rs.getString(TblUsrs.DB_FIELD_USER_NAME.getDB()));
            return user;
        });
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
