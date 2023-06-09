package ru.yandex.practicum.filmorate.service.filmService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmorateObjectException;
import ru.yandex.practicum.filmorate.exception.FilmorateValidationException;
import ru.yandex.practicum.filmorate.logEnum.FilmEnums.ErrorFilmEnum;
import ru.yandex.practicum.filmorate.logEnum.UserEnums.ErrorUserEnum;
import ru.yandex.practicum.filmorate.logEnum.FilmEnums.InfoFilmEnums.InfoFilmServiceEnum;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.filmFields.Genre;
import ru.yandex.practicum.filmorate.model.filmFields.MPA;
import ru.yandex.practicum.filmorate.service.userService.UserExceptionMessages;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private static final LocalDate BIRTHDAY_MOVIE = LocalDate.of(1895, 12, 28);
    private static final int GENRES_COUNT = 6;
    private static final int MPA_RATES_COUNT = 5;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Optional<Film> addFilmFS(Film film) {
        log.info(InfoFilmServiceEnum.REQUEST_FILM_SERVICE_ADD_FILM.getInfo(film.toString()));
        if (film.getId() != null) {
            log.error(ErrorFilmEnum.FAIL_FILM_ID.getFilmError(film.getId()));
            throw new FilmorateObjectException(FilmExceptionMessages
                    .FILM_ID_CONTAINS_EXCEPTION_MESSAGE.getMessage());
        }
        validateFilm(film);
        return filmStorage.addFilm(film);
    }

    public void deleteFilmFS(long filmID) {
        log.info(InfoFilmServiceEnum.REQUEST_FILM_SERVICE_DELETE_FILM.getInfo(String.valueOf(filmID)));
        if (!filmStorage.isContainsFilm(filmID)) {
            log.error(ErrorFilmEnum.FAIL_FILM_ID.getFilmError(filmID));
            throw new FilmorateObjectException(FilmExceptionMessages
                    .FILM_ID_NOT_CONTAINS_EXCEPTION_MESSAGE.getMessage());
        }
        filmStorage.deleteFilm(filmID);
    }

    public Optional<Film> updateFilmFS(long filmID, Film film) {
        log.info(InfoFilmServiceEnum.REQUEST_FILM_SERVICE_UPDATE_FILM.getInfo(film.toString()));
        if (!filmStorage.isContainsFilm(filmID)) {
            log.error(ErrorFilmEnum.FAIL_FILM_ID.getFilmError(filmID));
            throw new FilmorateObjectException(FilmExceptionMessages
                    .FILM_ID_NOT_CONTAINS_EXCEPTION_MESSAGE.getMessage());
        }
        validateFilm(film);
        return filmStorage.updateFilm(filmID, film);
    }

    public Optional<Film> getFilmFS(long filmID) {
        log.info(InfoFilmServiceEnum.REQUEST_FILM_SERVICE_GET_FILM.getInfo(String.valueOf(filmID)));
        if (!filmStorage.isContainsFilm(filmID)) {
            log.error(ErrorFilmEnum.FAIL_FILM_ID.getFilmError(filmID));
            throw new FilmorateObjectException(FilmExceptionMessages
                    .FILM_ID_NOT_CONTAINS_EXCEPTION_MESSAGE.getMessage());
        }
        return filmStorage.getFilm(filmID);
    }

    public Optional<Film> putLikeFS(long filmID, long userID) {
        log.info(InfoFilmServiceEnum.REQUEST_FILM_SERVICE_PUT_LIKE.getInfo(filmID + "/" + userID));
        if (!filmStorage.isContainsFilm(filmID)) {
            log.error(ErrorFilmEnum.FAIL_FILM_ID.getFilmError(filmID));
            throw new FilmorateObjectException(FilmExceptionMessages
                    .FILM_ID_NOT_CONTAINS_EXCEPTION_MESSAGE.getMessage());
        }
        if (!userStorage.isContainsUser(userID)) {
            log.error(ErrorUserEnum.FAIL_USER_ID.getUserError(userID));
            throw new FilmorateObjectException(UserExceptionMessages
                    .USER_ID_NOT_CONTAINS_EXCEPTION_MESSAGE.getMessage());
        }
        return filmStorage.putLike(filmID, userID);
    }

    public Optional<Film> deleteLikeFS(long filmID, long userID) {
        log.info(InfoFilmServiceEnum.REQUEST_FILM_SERVICE_DELETE_LIKE.getInfo(filmID + "/" + userID));
        if (!filmStorage.isContainsFilm(filmID)) {
            log.error(ErrorFilmEnum.FAIL_FILM_ID.getFilmError(filmID));
            throw new FilmorateObjectException(FilmExceptionMessages
                    .FILM_ID_NOT_CONTAINS_EXCEPTION_MESSAGE.getMessage());
        }
        if (!userStorage.isContainsUser(userID)) {
            log.error(ErrorUserEnum.FAIL_USER_ID.getUserError(userID));
            throw new FilmorateObjectException(UserExceptionMessages
                    .USER_ID_NOT_CONTAINS_EXCEPTION_MESSAGE.getMessage());
        }
        return filmStorage.deleteLike(filmID, userID);
    }

    public List<Film> getFilmsFS() {
        log.info(InfoFilmServiceEnum.REQUEST_FILM_SERVICE_GET_FILMS.getMessage());
        return filmStorage.getFilms();
    }

    public List<Film> getMostLikedFilmsFS(int countFilms) {
        log.info(InfoFilmServiceEnum.REQUEST_FILM_SERVICE_GET_MOST_LIKED_FILMS.getInfo(String.valueOf(countFilms)));
        if (countFilms <= 0) {
            log.error(ErrorFilmEnum.FAIL_FILM_COUNT.getFilmError(countFilms));
            throw new FilmorateObjectException(FilmExceptionMessages.FILM_COUNT_EXCEPTION_MESSAGE.getMessage());
        }
        return filmStorage.getMostLikedFilms(countFilms);
    }

    private void validateFilm(Film film) {
        log.info(InfoFilmServiceEnum.FILM_SERVICE_VALIDATE_FILM.getInfo(film.toString()));
        if (film.getDescription() != null) {
            if (film.getDescription().length() > 200) {
                log.error(ErrorFilmEnum.FAIL_FILM_DESC.getFilmError(film.getDescription()));
                throw new FilmorateValidationException(FilmExceptionMessages
                        .FILM_DESCRIPTION_LENGTH_EXCEPTION_MESSAGE.getMessage());
            }
        }
        if (film.getReleaseDate().isBefore(BIRTHDAY_MOVIE)) {
            log.error(ErrorFilmEnum.FAIL_FILM_RELEASE_DATE.getFilmError(film.getReleaseDate()));
            throw new FilmorateValidationException(FilmExceptionMessages
                    .FILM_RELEASE_DATE_EXCEPTION_MESSAGE.getMessage());
        }
        if (film.getDuration() <= 0) {
            log.error(ErrorFilmEnum.FAIL_FILM_DURATION.getFilmError(film.getDuration()));
            throw new FilmorateValidationException(FilmExceptionMessages
                    .FILM_DURATION_EXCEPTION_MESSAGE.getMessage());
        }
    }

    public List<Genre> getGenresFS() {
        log.info(InfoFilmServiceEnum.FILM_SERVICE_GET_GENRES.getMessage());
        return filmStorage.getGenres();
    }

    public Genre getGenreFS(int genreID) {
        log.info(InfoFilmServiceEnum.FILM_SERVICE_GET_GENRE.getInfo(String.valueOf(genreID)));
        if (genreID > GENRES_COUNT || genreID < 1) {
            log.error(ErrorFilmEnum.FAIL_FILM_GENRE_ID.getFilmError(genreID));
            throw new FilmorateObjectException(FilmExceptionMessages
                    .FILM_GENRE_ID_NOT_CONTAINS.getMessage() + GENRES_COUNT);
        }
        return filmStorage.getGenre(genreID);
    }

    public List<MPA> getAllMpaFS() {
        log.info(InfoFilmServiceEnum.FILM_SERVICE_GET_ALL_MPA.getMessage());
        return filmStorage.getAllMpa();
    }

    public MPA getMpaFS(int mpaID) {
        log.info(InfoFilmServiceEnum.FILM_SERVICE_GET_MPA.getInfo(String.valueOf(mpaID)));
        if (mpaID > MPA_RATES_COUNT || mpaID < 1) {
            log.error(ErrorFilmEnum.FAIL_FILM_MPA_ID.getFilmError(mpaID));
            throw new FilmorateObjectException(FilmExceptionMessages
                    .FILM_MPA_ID_NOT_CONTAINS.getMessage() + MPA_RATES_COUNT);
        }
        return filmStorage.getMpa(mpaID);
    }
}
