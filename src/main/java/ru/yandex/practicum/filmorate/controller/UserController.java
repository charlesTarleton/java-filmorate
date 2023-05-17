package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.logEnum.UserEnums.InfoFilmEnums.InfoUserControllerEnum;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Component
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User addUserUC(@Valid @RequestBody User user) {
        log.info(InfoUserControllerEnum.REQUEST_USER_CONTROLLER_ADD_USER.getInfo(user.toString()));
        return userService.addUserUS(user);
    }

    @DeleteMapping
    public void deleteUserUC(@RequestBody String userID) {
        log.info(InfoUserControllerEnum.REQUEST_USER_CONTROLLER_DELETE_USER.getInfo(userID));
        userService.deleteUserUS(userID);
    }

    @PutMapping
    public User updateUserUC(@Valid @RequestBody User user) {
        log.info(InfoUserControllerEnum.REQUEST_USER_CONTROLLER_UPDATE_USER.getInfo(user.toString()));
        return userService.updateUserUS(user.getId(), user);
    }

    @GetMapping("/{id}")
    public User getUserUC(@PathVariable("id") String userID) {
        log.info(InfoUserControllerEnum.REQUEST_USER_CONTROLLER_GET_USER.getInfo(userID));
        return userService.getUserUS(userID);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriendUC(@PathVariable("id") String userID, @PathVariable("friendId") String friendID) {
        log.info(InfoUserControllerEnum.REQUEST_USER_CONTROLLER_ADD_FRIEND_USER
                .getInfo(userID + "/" + friendID));
        return userService.addFriendUS(userID, friendID);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriendUC(@PathVariable("id") String userID, @PathVariable("friendId") String friendID) {
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
    public List<User> getFriendsUC(@PathVariable("id") String userID) {
        log.info(InfoUserControllerEnum.REQUEST_USER_CONTROLLER_GET_FRIENDS_USER.getInfo(userID));
        return userService.getFriendsUS(userID);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriendsUC(@PathVariable("id") String userID,
                                            @PathVariable("otherId") String otherID) {
        log.info(InfoUserControllerEnum.REQUEST_USER_CONTROLLER_GET_COMMON_FRIENDS_USER
                .getInfo(userID + "/" + otherID));
        return userService.getCommonFriendsUS(userID, otherID);
    }
}