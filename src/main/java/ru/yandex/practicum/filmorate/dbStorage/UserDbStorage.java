package ru.yandex.practicum.filmorate.dbStorage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dbStorage.DatabaseEnums.TblFrndshpSt;
import ru.yandex.practicum.filmorate.dbStorage.DatabaseEnums.TblUsrFrndshp;
import ru.yandex.practicum.filmorate.dbStorage.DatabaseEnums.TblUsrs;
import ru.yandex.practicum.filmorate.exception.FilmorateValidationException;
import ru.yandex.practicum.filmorate.logEnum.UserEnums.ErrorUserEnum;
import ru.yandex.practicum.filmorate.logEnum.UserEnums.InfoFilmEnums.InfoUserStorageEnum;
import ru.yandex.practicum.filmorate.logEnum.UserEnums.InfoFilmEnums.InfoUserSuccessEnum;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }

    @Override
    public User addUser(User user) {
        log.info(InfoUserStorageEnum.REQUEST_USER_STORAGE_ADD_USER.getInfo(user.getLogin()));
        jdbcTemplate.queryForRowSet(
                "INSERT INTO " + TblUsrs.DB_TABLE_USERS.getDB() + " (" +
                        TblUsrs.DB_FIELD_USER_EMAIL.getDB() + ", " +
                        TblUsrs.DB_FIELD_USER_LOGIN.getDB() + ", " +
                        TblUsrs.DB_FIELD_USER_NAME.getDB() + ", " +
                        TblUsrs.DB_FIELD_USER_BIRTHDAY.getDB() + ") " +
                    "VALUES (?, ?, ?, ?)",
                user.getEmail(), user.getLogin(), user.getName(), Date.valueOf(user.getBirthday()));
        user.setId(jdbcTemplate.queryForObject("SELECT IDENTITY() FROM users", Long.class));
        return user;
        /*if (userRows.next()) { //Старый вариант кода
            user.setId(userRows.getLong(TblUsrs.DB_FIELD_USER_ID.getDB()));
            log.info(InfoUserSuccessEnum.SUCCESS_ADD_USER.getInfo(user.getId() + "/" + user.getLogin()));
            return user;
        } else {
            log.error(ErrorUserEnum.FAIL_USER_TABLE_VALIDATION
                    .getUserError(user.getEmail() + "/" + user.getLogin()));
            throw new FilmorateValidationException("email или login");
        }
         */
    }

    @Override
    public void deleteUser(long userID) {
        log.info(InfoUserStorageEnum.REQUEST_USER_STORAGE_DELETE_USER.getInfo(String.valueOf(userID)));
        jdbcTemplate.queryForRowSet(
                "DELETE FROM " + TblUsrs.DB_TABLE_USERS.getDB() + " " +
                "WHERE + " + TblUsrs.DB_FIELD_USER_ID.getDB() + " = ?", userID);
        log.info(InfoUserSuccessEnum.SUCCESS_DELETE_USER.getInfo(String.valueOf(userID)));
    }

    @Override
    public User updateUser(long userID, User user) {
        log.info(InfoUserStorageEnum.REQUEST_USER_STORAGE_UPDATE_USER.getInfo(String.valueOf(userID)));
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(
                "UPDATE " + TblUsrs.DB_TABLE_USERS.getDB() + " " +
                        "SET " + TblUsrs.DB_FIELD_USER_EMAIL.getDB() + " = ?, " +
                        TblUsrs.DB_FIELD_USER_LOGIN.getDB() + " = ?," +
                        TblUsrs.DB_FIELD_USER_NAME.getDB() + " = ?, " +
                        TblUsrs.DB_FIELD_USER_BIRTHDAY.getDB() + " = ? " +
                        "WHERE " + TblUsrs.DB_FIELD_USER_ID.getDB() + " = ?; " +
                        "SELECT * " +
                        "FROM " + TblUsrs.DB_TABLE_USERS.getDB() + " " +
                        "WHERE " + TblUsrs.DB_FIELD_USER_ID.getDB() + " = ?",
                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), userID, userID);
        if (userRows.next()) {
            log.info(InfoUserSuccessEnum.SUCCESS_UPDATE_USER.getInfo(user.getId() + "/" + user.getLogin()));
            return user;
        } else {
            log.error(ErrorUserEnum.FAIL_USER_TABLE_VALIDATION
                    .getUserError(user.getEmail() + "/" + user.getLogin()));
            throw new FilmorateValidationException("email или login");
        }
    }

    @Override
    public boolean isContainsUser(long userID) {
        log.info(InfoUserStorageEnum.REQUEST_USER_STORAGE_CONTAINS_USER.getInfo(String.valueOf(userID)));
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(
                "SELECT * " +
                    "FROM " + TblUsrs.DB_TABLE_USERS.getDB() + " " +
                    "WHERE " + TblUsrs.DB_FIELD_USER_ID.getDB() + " = ?", userID);
        return userRows.next();
    }

    @Override
    public List<User> getUsers() {
        log.info(InfoUserStorageEnum.REQUEST_USER_STORAGE_GET_USERS.getMessage());
        log.info(InfoUserSuccessEnum.SUCCESS_GET_USERS.getMessage());
        return jdbcTemplate.query(
                "SELECT * " +
                        "FROM " + TblUsrs.DB_TABLE_USERS, (rs, rowNum) -> {
            User user = new User(rs.getString(TblUsrs.DB_FIELD_USER_EMAIL.getDB()),
                    rs.getString(TblUsrs.DB_FIELD_USER_LOGIN.getDB()),
                    rs.getDate(TblUsrs.DB_FIELD_USER_BIRTHDAY.getDB()).toLocalDate());
            user.setId(rs.getLong(TblUsrs.DB_FIELD_USER_ID.getDB()));
            user.setName(rs.getString(TblUsrs.DB_FIELD_USER_NAME.getDB()));
            user.setUserFriends(getFriendsUS(user.getId()));
            return user;
        });
    }

    @Override
    public User getUser(long userID) {
        log.info(InfoUserStorageEnum.REQUEST_USER_STORAGE_GET_USER.getInfo(String.valueOf(userID)));
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(
                "SELECT * " +
                    "FROM " + TblUsrs.DB_TABLE_USERS.getDB() + " " +
                    "WHERE " + TblUsrs.DB_FIELD_USER_ID.getDB() + " = ?", userID);
        User user = new User(userRows.getString(TblUsrs.DB_FIELD_USER_EMAIL.getDB()),
                userRows.getString(TblUsrs.DB_FIELD_USER_LOGIN.getDB()),
                userRows.getDate(TblUsrs.DB_FIELD_USER_BIRTHDAY.getDB()).toLocalDate());
        user.setId(userID);
        user.setName(userRows.getString(TblUsrs.DB_FIELD_USER_NAME.getDB()));
        user.setUserFriends(getFriendsUS(userID));
        log.info(InfoUserSuccessEnum.SUCCESS_GET_USER.getInfo(user.getId() + "/" + user.getLogin()));
        return user;
    }

    private Map<Long, Boolean> getFriendsUS(long userId) {
        Map<Long, Boolean> localUserFriends = new LinkedHashMap<>();
        List<Map<String, Object>> friendshipTable = jdbcTemplate.queryForList(
                "SELECT " + "uf." + TblUsrFrndshp.DB_FIELD_FRIEND_USER_ID.getDB() + ", " +
                    "fs." + TblFrndshpSt.DB_FIELD_FRIENDSHIP_STATUS_NAME.getDB() + " " +
                    "FROM " + TblUsrFrndshp.DB_TABLE_USER_FRIENDSHIP.getDB() + " AS uf " +
                    "LEFT OUTER JOIN " + TblFrndshpSt.DB_TABLE_FRIENDSHIP_STATUS.getDB() + " AS fs " +
                    "ON fs." + TblFrndshpSt.DB_FIELD_FRIENDSHIP_STATUS_ID.getDB() + " = uf." +
                        TblFrndshpSt.DB_FIELD_FRIENDSHIP_STATUS_ID.getDB() + " " +
                    "WHERE " + TblUsrs.DB_FIELD_USER_ID.getDB() + " = ?", userId);
        friendshipTable.stream().map(map -> localUserFriends.put(
                (Long) map.get(TblUsrFrndshp.DB_FIELD_FRIEND_USER_ID.getDB()),
                Boolean.getBoolean((String)
                        map.get(TblFrndshpSt.DB_FIELD_FRIENDSHIP_STATUS_NAME.getDB()))));
        return localUserFriends;
    }
}