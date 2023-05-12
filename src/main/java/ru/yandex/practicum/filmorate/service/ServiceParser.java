package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.IncorrectFilmExceptions.FilmWithoutIDException;
import ru.yandex.practicum.filmorate.exception.IncorrectUserExceptions.UserWithoutIDException;
import ru.yandex.practicum.filmorate.logEnum.FilmEnums.ErrorFilmEnum;
import ru.yandex.practicum.filmorate.logEnum.FilmEnums.InfoFilmEnums.InfoFilmServiceEnum;
import ru.yandex.practicum.filmorate.logEnum.UserEnums.ErrorUserEnum;
import ru.yandex.practicum.filmorate.logEnum.UserEnums.InfoFilmEnums.InfoUserServiceEnum;

@Slf4j
public class ServiceParser {
    public static Integer parseFilmID(String filmID) {
        log.info(InfoFilmServiceEnum.FILM_SERVICE_PARSE_FILM_ID.getMessage());
        try {
            if (filmID == null) {
                throw new FilmWithoutIDException();
            }
            return Integer.parseInt(filmID);
        } catch (NumberFormatException exception) {
            log.error(ErrorFilmEnum.FAIL_FILM_ID.getFilmError(filmID));
            throw new FilmWithoutIDException();
        }
    }

    public static Integer parseUserID(String userID) {
        log.info(InfoUserServiceEnum.USER_SERVICE_PARSE_USER_ID.getMessage());
        try {
            if (userID == null) {
                throw new UserWithoutIDException();
            }
            return Integer.parseInt(userID);
        } catch (NumberFormatException exception) {
            log.error(ErrorUserEnum.FAIL_USER_ID.getUserError(userID));
            throw new UserWithoutIDException();
        }
    }
}
