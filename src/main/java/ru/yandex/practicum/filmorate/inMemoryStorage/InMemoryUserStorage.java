package ru.yandex.practicum.filmorate.inMemoryStorage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.logEnum.UserEnums.InfoFilmEnums.InfoUserStorageEnum;
import ru.yandex.practicum.filmorate.logEnum.UserEnums.InfoFilmEnums.InfoUserSuccessEnum;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long globalUserID = 1;

    public User addUser(User user) {
        log.info(InfoUserStorageEnum.REQUEST_USER_STORAGE_ADD_USER.getInfo(user.toString()));
        user.setId(globalUserID);
        users.put(globalUserID++, user);
        log.info(InfoUserSuccessEnum.SUCCESS_ADD_USER.getInfo(user.getId() + "/" + user.getLogin()));
        return user; // переместил лог успеха так близко к получению результата, как мог
    }

    public void deleteUser(long userID) {
        log.info(InfoUserStorageEnum.REQUEST_USER_STORAGE_DELETE_USER.getInfo(String.valueOf(userID)));
        String userLogin = users.get(userID).getLogin();
        users.remove(userID);
        log.info(InfoUserSuccessEnum.SUCCESS_DELETE_USER
                .getInfo(userID + "/" + userLogin));
    }

    public User updateUser(long userID, User user) {
        log.info(InfoUserStorageEnum.REQUEST_USER_STORAGE_UPDATE_USER.getInfo(user.toString()));
        users.put(userID, user);
        log.info(InfoUserSuccessEnum.SUCCESS_UPDATE_USER.getInfo(user.getId() + "/" + user.getLogin()));
        return user;
    }

    public boolean isContainsUser(long userID) {
        log.info(InfoUserStorageEnum.REQUEST_USER_STORAGE_CONTAINS_USER.getInfo(String.valueOf(userID)));
        return users.containsKey(userID);
    }

    public User getUser(long userID) {
        log.info(InfoUserStorageEnum.REQUEST_USER_STORAGE_GET_USERS.getInfo(String.valueOf(userID)));
        log.info(InfoUserSuccessEnum.SUCCESS_GET_USER
                .getInfo(userID + "/" + users.get(userID).getLogin()));
        return users.get(userID);
    }

    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }
}