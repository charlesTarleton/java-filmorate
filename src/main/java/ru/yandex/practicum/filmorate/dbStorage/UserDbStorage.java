package ru.yandex.practicum.filmorate.dbStorage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dbStorage.DatabaseEnums.*;
import ru.yandex.practicum.filmorate.logEnum.userEnums.InfoFilmEnums.InfoUserStorageEnum;
import ru.yandex.practicum.filmorate.logEnum.userEnums.InfoFilmEnums.InfoUserSuccessEnum;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.*;

@Slf4j
@Component
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> addUser(User user) {
        log.info(InfoUserStorageEnum.REQUEST_USER_STORAGE_ADD_USER.getInfo(String.valueOf(user)));
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement prst = connection.prepareStatement(
                    "INSERT INTO " + TblUsrs.DB_TABLE_USERS.getDB() + " (" +
                            TblUsrs.DB_FIELD_USER_EMAIL.getDB() + ", " +
                            TblUsrs.DB_FIELD_USER_LOGIN.getDB() + ", " +
                            TblUsrs.DB_FIELD_USER_NAME.getDB() + ", " +
                            TblUsrs.DB_FIELD_USER_BIRTHDAY.getDB() + ") " +
                        "VALUES (?, ?, ?, ?)", new String[]{TblUsrs.DB_FIELD_USER_ID.getDB()});
            prst.setString(1, user.getEmail());
            prst.setString(2, user.getLogin());
            prst.setString(3, user.getName());
            prst.setDate(4, Date.valueOf(user.getBirthday()));
            return prst;
        }, keyHolder);
        user.setId(keyHolder.getKey().longValue());
        log.info(InfoUserSuccessEnum.SUCCESS_ADD_USER.getInfo(String.valueOf(user.getId())));
        return Optional.of(user);
    }

    @Override
    public void deleteUser(long userID) {
        log.info(InfoUserStorageEnum.REQUEST_USER_STORAGE_DELETE_USER.getInfo(String.valueOf(userID)));
        jdbcTemplate.update(
                "DELETE FROM " + TblUsrs.DB_TABLE_USERS.getDB() + " " +
                    "WHERE + " + TblUsrs.DB_FIELD_USER_ID.getDB() + " = ?", userID);
        log.info(InfoUserSuccessEnum.SUCCESS_DELETE_USER.getInfo(String.valueOf(userID)));
    }

    @Override
    public Optional<User> updateUser(long userID, User user) {
        log.info(InfoUserStorageEnum.REQUEST_USER_STORAGE_UPDATE_USER.getInfo(String.valueOf(userID)));
        jdbcTemplate.update(
                "UPDATE " + TblUsrs.DB_TABLE_USERS.getDB() + " " +
                    "SET " + TblUsrs.DB_FIELD_USER_EMAIL.getDB() + " = ?, " +
                        TblUsrs.DB_FIELD_USER_LOGIN.getDB() + " = ?," +
                        TblUsrs.DB_FIELD_USER_NAME.getDB() + " = ?, " +
                        TblUsrs.DB_FIELD_USER_BIRTHDAY.getDB() + " = ? " +
                    "WHERE " + TblUsrs.DB_FIELD_USER_ID.getDB() + " = ?",
                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), userID);
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(
                "SELECT * " +
                    "FROM " + TblUsrs.DB_TABLE_USERS.getDB() + " " +
                    "WHERE " + TblUsrs.DB_FIELD_USER_ID.getDB() + " = ?", userID);
        if (userRows.next()) {
            User localUser = new User(userRows.getString(TblUsrs.DB_FIELD_USER_EMAIL.getDB()),
                    userRows.getString(TblUsrs.DB_FIELD_USER_LOGIN.getDB()),
                    userRows.getDate(TblUsrs.DB_FIELD_USER_BIRTHDAY.getDB()).toLocalDate());
            localUser.setId(userRows.getLong(TblUsrs.DB_FIELD_USER_ID.getDB()));
            localUser.setName(userRows.getString(TblUsrs.DB_FIELD_USER_NAME.getDB()));
            log.info(InfoUserSuccessEnum.SUCCESS_UPDATE_USER.getInfo(String.valueOf(userID)));
            return Optional.of(localUser);
        }
        return Optional.empty();
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
                    "FROM " + TblUsrs.DB_TABLE_USERS.getDB(), (rs, rowNum) -> {
            User user = new User(rs.getString(TblUsrs.DB_FIELD_USER_EMAIL.getDB()),
                    rs.getString(TblUsrs.DB_FIELD_USER_LOGIN.getDB()),
                    rs.getDate(TblUsrs.DB_FIELD_USER_BIRTHDAY.getDB()).toLocalDate());
            user.setId(rs.getLong(TblUsrs.DB_FIELD_USER_ID.getDB()));
            user.setName(rs.getString(TblUsrs.DB_FIELD_USER_NAME.getDB()));
            return user;
        });
    }

    @Override
    public Optional<User> getUser(long userID) {
        log.info(InfoUserStorageEnum.REQUEST_USER_STORAGE_GET_USER.getInfo(String.valueOf(userID)));
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(
                "SELECT * " +
                    "FROM " + TblUsrs.DB_TABLE_USERS.getDB() + " " +
                    "WHERE " + TblUsrs.DB_FIELD_USER_ID.getDB() + " = ?", userID);
        if (userRows.next()) {
            User user = new User(userRows.getString(TblUsrs.DB_FIELD_USER_EMAIL.getDB()),
                    userRows.getString(TblUsrs.DB_FIELD_USER_LOGIN.getDB()),
                    userRows.getDate(TblUsrs.DB_FIELD_USER_BIRTHDAY.getDB()).toLocalDate());
            user.setId(userID);
            user.setName(userRows.getString(TblUsrs.DB_FIELD_USER_NAME.getDB()));
            log.info(InfoUserSuccessEnum.SUCCESS_GET_USER.getInfo(String.valueOf(user.getId())));
            return Optional.of(user);
        }
        return Optional.empty();
    }

    public Optional<User> addFriend(long userID, long friendID) {
        log.info(InfoUserStorageEnum.REQUEST_USER_STORAGE_ADD_FRIEND_USER.getInfo(userID + "/" + friendID));
        jdbcTemplate.update(
                "INSERT INTO " + TblUsrFrndshp.DB_TABLE_USER_FRIENDSHIP.getDB() + " (" +
                        TblUsrs.DB_FIELD_USER_ID.getDB() + ", " +
                        TblUsrFrndshp.DB_FIELD_FRIEND_USER_ID.getDB() + ", " +
                        TblFrndshpSt.DB_FIELD_FRIENDSHIP_STATUS_ID.getDB() + ") " +
                    "VALUES (?, ?, 2)", userID, friendID);
        log.info(InfoUserSuccessEnum.SUCCESS_ADD_FRIEND_USER.getInfo(String.valueOf(userID)));
        return getUser(friendID);
    }

    public Optional<User> deleteFriend(long userID, long friendID) {
        log.info(InfoUserStorageEnum.REQUEST_USER_STORAGE_DELETE_FRIEND_USER.getInfo(userID + "/" + friendID));
        jdbcTemplate.update(
                "DELETE FROM " + TblUsrFrndshp.DB_TABLE_USER_FRIENDSHIP.getDB() + " " +
                    "WHERE " + TblUsrs.DB_FIELD_USER_ID.getDB() + " = ? AND " +
                        TblUsrFrndshp.DB_FIELD_FRIEND_USER_ID.getDB() + " = ?; " +
                    "DELETE FROM " + TblUsrFrndshp.DB_TABLE_USER_FRIENDSHIP.getDB() + " " +
                    "WHERE " + TblUsrs.DB_FIELD_USER_ID.getDB() + " = ? AND " +
                        TblUsrFrndshp.DB_FIELD_FRIEND_USER_ID.getDB() + " = ?",
                userID, friendID, friendID, userID);
        log.info(InfoUserSuccessEnum.SUCCESS_DELETE_FRIEND_USER.getInfo(String.valueOf(userID)));
        return getUser(friendID);
    }

    public List<User> getFriends(long userID) {
        log.info(InfoUserSuccessEnum.SUCCESS_GET_FRIENDS_USER.getInfo(String.valueOf(userID)));
        String sql = "SELECT u.* " +
                "FROM " + TblUsrs.DB_TABLE_USERS.getDB() + " AS u " +
                "RIGHT OUTER JOIN " + TblUsrFrndshp.DB_TABLE_USER_FRIENDSHIP.getDB() + " AS uf " +
                "ON uf." + TblUsrFrndshp.DB_FIELD_FRIEND_USER_ID.getDB() + " = u." +
                TblUsrs.DB_FIELD_USER_ID.getDB() + " " +
                "WHERE uf." + TblUsrs.DB_FIELD_USER_ID.getDB() + " = ?";
        log.info(InfoUserSuccessEnum.SUCCESS_GET_FRIENDS_USER.getInfo(String.valueOf(userID)));
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            User user = new User(rs.getString(TblUsrs.DB_FIELD_USER_EMAIL.getDB()),
                    rs.getString(TblUsrs.DB_FIELD_USER_LOGIN.getDB()),
                    rs.getDate(TblUsrs.DB_FIELD_USER_BIRTHDAY.getDB()).toLocalDate());
            user.setId(rs.getLong(TblUsrs.DB_FIELD_USER_ID.getDB()));
            user.setName(rs.getString(TblUsrs.DB_FIELD_USER_NAME.getDB()));
            return user;
        }, userID);
    }

    public List<User> getCommonFriends(long userID, long otherID) {
        log.info(InfoUserStorageEnum.REQUEST_USER_STORAGE_GET_COMMON_FRIENDS_USER
                .getInfo(userID + "/" + otherID));
        String sql = "SELECT u.* " +
                "FROM " + TblUsrs.DB_TABLE_USERS.getDB() + " AS u " +
                "RIGHT OUTER JOIN " + TblUsrFrndshp.DB_TABLE_USER_FRIENDSHIP.getDB() + " AS uf1 " +
                "ON u." + TblUsrs.DB_FIELD_USER_ID.getDB() + " = uf1." +
                    TblUsrFrndshp.DB_FIELD_FRIEND_USER_ID.getDB() + " " +
                "INNER JOIN " + TblUsrFrndshp.DB_TABLE_USER_FRIENDSHIP.getDB() + " AS uf2 " +
                "ON uf1." + TblUsrFrndshp.DB_FIELD_FRIEND_USER_ID.getDB() + " = uf2." +
                TblUsrFrndshp.DB_FIELD_FRIEND_USER_ID.getDB() + " " +
                "WHERE uf1." + TblUsrs.DB_FIELD_USER_ID.getDB() + " = ? AND uf2." +
                    TblUsrs.DB_FIELD_USER_ID.getDB() + " = ?";
        log.info(InfoUserSuccessEnum.SUCCESS_GET_COMMON_FRIENDS_USER.getInfo(String.valueOf(userID)));
        return jdbcTemplate.query(sql, new Object[]{userID, otherID}, (rs, rowNum) -> {
            User user = new User(rs.getString(TblUsrs.DB_FIELD_USER_EMAIL.getDB()),
                    rs.getString(TblUsrs.DB_FIELD_USER_LOGIN.getDB()),
                    rs.getDate(TblUsrs.DB_FIELD_USER_BIRTHDAY.getDB()).toLocalDate());
                    user.setId(rs.getLong(TblUsrs.DB_FIELD_USER_ID.getDB()));
                    user.setName(rs.getString(TblUsrs.DB_FIELD_USER_NAME.getDB()));
                    return user;
        });
    }
}