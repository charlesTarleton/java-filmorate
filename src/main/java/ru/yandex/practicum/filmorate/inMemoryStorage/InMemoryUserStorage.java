package ru.yandex.practicum.filmorate.inMemoryStorage;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.logEnum.UserEnums.InfoFilmEnums.InfoUserStorageEnum;
import ru.yandex.practicum.filmorate.logEnum.UserEnums.InfoFilmEnums.InfoUserSuccessEnum;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@Getter
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private Integer globalUserID = 1;

    public User addUser(User user) {
        log.info(InfoUserStorageEnum.REQUEST_USER_STORAGE_ADD_USER.getInfo(user.toString()));
        user.setID(globalUserID);
        users.put(globalUserID++, user);
        log.info(InfoUserSuccessEnum.SUCCESS_ADD_USER.getInfo(user.getID() + "/" + user.getLogin()));
        return user;
    }

    public void deleteUser(Integer userID) {
        log.info(InfoUserStorageEnum.REQUEST_USER_STORAGE_DELETE_USER.getInfo(String.valueOf(userID)));
        log.info(InfoUserSuccessEnum.SUCCESS_DELETE_USER
                .getInfo(users.get(userID).getID() + "/" + users.get(userID).getLogin()));
        users.remove(userID);
    }

    public User updateUser(Integer userID, User user) {
        log.info(InfoUserStorageEnum.REQUEST_USER_STORAGE_UPDATE_USER.getInfo(user.toString()));
        users.put(userID, user);
        log.info(InfoUserSuccessEnum.SUCCESS_UPDATE_USER.getInfo(user.getID() + "/" + user.getLogin()));
        return user;
    }

    public boolean isContainsUser(Integer userID) {
        log.info(InfoUserStorageEnum.REQUEST_USER_STORAGE_CONTAINS_USER.getInfo(String.valueOf(userID)));
        return users.containsKey(userID);
    }
}
