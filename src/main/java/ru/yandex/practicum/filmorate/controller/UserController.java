package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.logEnum.userEnums.InfoFilmEnums.InfoUserControllerEnum;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.userService.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public Optional<User> addUserUC(@Valid @RequestBody User user) {
        log.info(InfoUserControllerEnum.REQUEST_USER_CONTROLLER_ADD_USER.getInfo(user.toString()));
        return userService.addUserUS(user);
    }

    @DeleteMapping
    public void deleteUserUC(@RequestBody long userID) {
        log.info(InfoUserControllerEnum.REQUEST_USER_CONTROLLER_DELETE_USER.getInfo(String.valueOf(userID)));
        userService.deleteUserUS(userID);
    }

    @PutMapping
    public Optional<User> updateUserUC(@Valid @RequestBody User user) {
        log.info(InfoUserControllerEnum.REQUEST_USER_CONTROLLER_UPDATE_USER.getInfo(user.toString()));
        return userService.updateUserUS(user.getId(), user);
    }

    @GetMapping("/{id}")
    public Optional<User> getUserUC(@PathVariable("id") long userID) {
        log.info(InfoUserControllerEnum.REQUEST_USER_CONTROLLER_GET_USER.getInfo(String.valueOf(userID)));
        return userService.getUserUS(userID);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public Optional<User> addFriendUC(@PathVariable("id") long userID, @PathVariable("friendId") long friendID) {
        log.info(InfoUserControllerEnum.REQUEST_USER_CONTROLLER_ADD_FRIEND_USER
                .getInfo(userID + "/" + friendID));
        return userService.addFriendUS(userID, friendID);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public Optional<User> deleteFriendUC(@PathVariable("id") long userID, @PathVariable("friendId") long friendID) {
        log.info(InfoUserControllerEnum.REQUEST_USER_CONTROLLER_DELETE_FRIEND_USER
                .getInfo(userID + "/" + friendID));
        return userService.deleteFriendUS(userID, friendID);
    }

    @GetMapping
    public List<User> getUsersUC() {
        log.info(InfoUserControllerEnum.REQUEST_USER_CONTROLLER_GET_USERS.getMessage());
        return userService.getUsersUS();
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriendsUC(@PathVariable("id") long userID) {
        log.info(InfoUserControllerEnum.REQUEST_USER_CONTROLLER_GET_FRIENDS_USER.getInfo(String.valueOf(userID)));
        return userService.getFriendsUS(userID);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriendsUC(@PathVariable("id") long userID,
                                            @PathVariable("otherId") long otherID) {
        log.info(InfoUserControllerEnum.REQUEST_USER_CONTROLLER_GET_COMMON_FRIENDS_USER
                .getInfo(userID + "/" + otherID));
        return userService.getCommonFriendsUS(userID, otherID);
    }
}