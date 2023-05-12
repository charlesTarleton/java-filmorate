package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectFilmExceptions.*;
import ru.yandex.practicum.filmorate.exception.IncorrectUserExceptions.UserWithoutIDException;
import ru.yandex.practicum.filmorate.logEnum.FilmEnums.ErrorFilmEnum;
import ru.yandex.practicum.filmorate.logEnum.UserEnums.ErrorUserEnum;
import ru.yandex.practicum.filmorate.logEnum.FilmEnums.InfoFilmEnums.InfoFilmServiceEnum;
import ru.yandex.practicum.filmorate.logEnum.FilmEnums.InfoFilmEnums.InfoFilmSuccessEnum;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    protected final FilmStorage filmStorage;
    protected final UserStorage userStorage;
    private static final LocalDate BIRTHDAY_MOVIE = LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addFilmFS(Film film) {
        log.info(InfoFilmServiceEnum.REQUEST_FILM_SERVICE_ADD_FILM.getInfo(film.toString()));
        if (film.getID() != null) {
            log.error(ErrorFilmEnum.FAIL_FILM_ID.getFilmError(film.getID()));
            throw new FilmWithIDException();
        }
        validateFilm(film);
        return filmStorage.addFilm(film);
    }

    public void deleteFilmFS(String filmIDStr) {
        log.info(InfoFilmServiceEnum.REQUEST_FILM_SERVICE_DELETE_FILM.getInfo(filmIDStr));
        Integer filmID = ServiceParser.parseFilmID(filmIDStr);
        if (!filmStorage.getFilms().containsKey(filmID)) {
            log.error(ErrorFilmEnum.FAIL_FILM_ID.getFilmError(filmID));
            throw new FilmWithoutIDException();
        }
        filmStorage.deleteFilm(filmID);
    }

    public Film updateFilmFS(Integer filmID, Film film) {
        log.info(InfoFilmServiceEnum.REQUEST_FILM_SERVICE_UPDATE_FILM.getInfo(film.toString()));
        if (!filmStorage.getFilms().containsKey(filmID)) {
            log.error(ErrorFilmEnum.FAIL_FILM_ID.getFilmError(filmID));
            throw new FilmWithoutIDException();
        }
        validateFilm(film);
        return filmStorage.updateFilm(filmID, film);
    }

    public Film getFilmFS(String filmIDStr) {
        log.info(InfoFilmServiceEnum.REQUEST_FILM_SERVICE_GET_FILM.getInfo(filmIDStr));
        Integer filmID = ServiceParser.parseFilmID(filmIDStr);
        if (!filmStorage.getFilms().containsKey(filmID)) {
            log.error(ErrorFilmEnum.FAIL_FILM_ID.getFilmError(filmIDStr));
            throw new FilmWithoutIDException();
        }
        log.info(InfoFilmSuccessEnum.SUCCESS_GET_FILM
                .getInfo(filmIDStr + "/" + filmStorage.getFilms().get(filmID).getName()));
        return filmStorage.getFilms().get(filmID);
    }

    public Film putLikeFS(String filmIDStr, String userIDStr) {
        log.info(InfoFilmServiceEnum.REQUEST_FILM_SERVICE_PUT_LIKE.getInfo(filmIDStr + "/" + userIDStr));
        Integer filmID = ServiceParser.parseFilmID(filmIDStr);
        Integer userID = ServiceParser.parseUserID(userIDStr);
        if (!filmStorage.getFilms().containsKey(filmID)) {
            log.error(ErrorFilmEnum.FAIL_FILM_ID.getFilmError(filmID));
            throw new FilmWithoutIDException();
        }
        if (!userStorage.isContainsUser(userID)) {
            log.error(ErrorUserEnum.FAIL_USER_ID.getUserError(userID));
            throw new UserWithoutIDException();
        }
        log.info(InfoFilmSuccessEnum.SUCCESS_LIKE_FILM.getInfo(filmID + "/" + filmStorage
                .getFilms().get(filmID).getName()));
        filmStorage.getFilms().get(filmID).getLikes().add(userID);
        return filmStorage.getFilms().get(filmID);
    }

    public Film deleteLikeFS(String filmIDStr, String  userIDStr) {
        log.info(InfoFilmServiceEnum.REQUEST_FILM_SERVICE_DELETE_LIKE.getInfo(filmIDStr + "/" + userIDStr));
        Integer filmID = ServiceParser.parseFilmID(filmIDStr);
        Integer userID = ServiceParser.parseUserID(userIDStr);
        if (!filmStorage.getFilms().containsKey(filmID)) {
            log.error(ErrorFilmEnum.FAIL_FILM_ID.getFilmError(filmID));
            throw new FilmWithoutIDException();
        }
        if (!filmStorage.getFilms().get(filmID).getLikes().contains(userID)) {

            throw new FilmNotContainsUserLikeException();
        }
        log.info(InfoFilmSuccessEnum.SUCCESS_DISLIKE_FILM.getInfo(filmID + "/" + filmStorage
                .getFilms().get(filmID).getName()));
        filmStorage.getFilms().get(filmID).getLikes().remove(userID);
        return filmStorage.getFilms().get(filmID);
    }

    public List<Film> getFilmsFS() {
        log.info(InfoFilmServiceEnum.REQUEST_FILM_SERVICE_GET_FILMS.getMessage());
        log.info(InfoFilmSuccessEnum.SUCCESS_GET_FILMS.getMessage());
        return new ArrayList<>(filmStorage.getFilms().values());
    }

    public List<Film> getMostLikedFilmsFS(Integer countFilms) {
        log.info(InfoFilmServiceEnum.REQUEST_FILM_SERVICE_GET_MOST_LIKED_FILMS.getInfo(String.valueOf(countFilms)));
        if (countFilms <= 0) {
            log.error(ErrorFilmEnum.FAIL_FILM_COUNT.getFilmError(countFilms));
            throw new FilmIncorrectCountException();
        }
        List<Film> filmList = new ArrayList<>(filmStorage.getFilms().values());
        filmList = filmList.stream().sorted(Comparator.comparingInt(film -> -film.getLikes()
                .size())).collect(Collectors.toList());
        log.info(InfoFilmSuccessEnum.SUCCESS_GET_MOST_LIKED_FILMS.getInfo(String.valueOf(countFilms)));
        if (filmList.size() <= countFilms) {
            return filmList;
        } else {
            return filmList.subList(0, countFilms);
        }
    }

    private void validateFilm(Film film) {
        log.info(InfoFilmServiceEnum.FILM_SERVICE_VALIDATE_FILM.getInfo(film.toString()));
        if (film.getDescription() != null) {
            if (film.getDescription().length() > 200) {
                log.error(ErrorFilmEnum.FAIL_FILM_DESC.getFilmError(film.getDescription()));
                throw new FilmDescriptionException();
            }
        }
        if (film.getReleaseDate().isBefore(BIRTHDAY_MOVIE)) {
            log.error(ErrorFilmEnum.FAIL_FILM_RELEASE_DATE.getFilmError(film.getReleaseDate()));
            throw new FilmReleaseDateException();
        }
        if (film.getDuration() <= 0) {
            log.error(ErrorFilmEnum.FAIL_FILM_DURATION.getFilmError(film.getDuration()));
            throw new FilmDurationException();
        }
    }
}
