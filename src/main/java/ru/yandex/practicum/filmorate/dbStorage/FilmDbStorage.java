package ru.yandex.practicum.filmorate.dbStorage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dbStorage.DatabaseEnums.*;
import ru.yandex.practicum.filmorate.exception.FilmorateValidationException;
import ru.yandex.practicum.filmorate.logEnum.FilmEnums.ErrorFilmEnum;
import ru.yandex.practicum.filmorate.logEnum.FilmEnums.InfoFilmEnums.InfoFilmServiceEnum;
import ru.yandex.practicum.filmorate.logEnum.FilmEnums.InfoFilmEnums.InfoFilmStorageEnum;
import ru.yandex.practicum.filmorate.logEnum.FilmEnums.InfoFilmEnums.InfoFilmSuccessEnum;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.filmEnums.AdultRate;
import ru.yandex.practicum.filmorate.model.filmEnums.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }

    @Override
    public Film addFilm(Film film) {
        log.info(InfoFilmServiceEnum.REQUEST_FILM_SERVICE_ADD_FILM.getInfo(film.getName()));
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(
                "INSERT INTO " + TblFlms.DB_TABLE_FILMS.getDB() + " (" +
                        TblFlms.DB_FIELD_FILM_NAME.getDB() + ", " +
                        TblFlms.DB_FIELD_FILM_DESCRIPTION.getDB() + ", " +
                        TblFlms.DB_FIELD_FILM_RELEASE_DATE.getDB() + ", " +
                        TblFlms.DB_FIELD_FILM_DURATION.getDB() + ", " +
                        TblFlms.DB_FIELD_FILM_RATE.getDB() + ", " +
                        TblAdltRt.DB_FIELD_ADULT_RATE_ID.getDB() + ") " +
                        "VALUES(?,?,?,?,?,?,?);" +
                        "SELECT * " +
                        "FROM " + TblFlms.DB_TABLE_FILMS.getDB() + " " +
                        "WHERE " + TblFlms.DB_FIELD_FILM_NAME.getDB() + " = ?" ,
                film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getRate(), film.getAdultRate(), film.getName());
        if (filmRows.next() && filmRows.getString(TblFlms.DB_FIELD_FILM_NAME.getDB()).equals(film.getName())) {
            film.setId(filmRows.getLong(TblFlms.DB_FIELD_FILM_ID.getDB()));
            log.info(InfoFilmSuccessEnum.SUCCESS_ADD_FILM.getInfo(film.getId() + "/" + film.getName()));
            return film;
        } else {
            log.error(ErrorFilmEnum.FAIL_FILM_TABLE_VALIDATION.getFilmError(film.getName()));
            throw new FilmorateValidationException("name");
        }
    }

    @Override
    public void deleteFilm(long filmID) {
        log.info(InfoFilmStorageEnum.REQUEST_FILM_STORAGE_DELETE_FILM.getInfo(String.valueOf(filmID)));
        jdbcTemplate.queryForRowSet(
                "DELETE FROM " + TblFlms.DB_TABLE_FILMS.getDB() + " " +
                "WHERE + " + TblFlms.DB_FIELD_FILM_ID.getDB() + " = ?", filmID);
        log.info(InfoFilmSuccessEnum.SUCCESS_DELETE_FILM.getInfo(String.valueOf(filmID)));
    }

    @Override
    public Film updateFilm(long filmID, Film film) {
        log.info(InfoFilmStorageEnum.REQUEST_FILM_STORAGE_UPDATE_FILM.getInfo(String.valueOf(filmID)));
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(
                "UPDATE " + TblFlms.DB_TABLE_FILMS.getDB() + " " +
                        "SET " + TblFlms.DB_FIELD_FILM_NAME.getDB() + " = ?, " +
                        TblFlms.DB_FIELD_FILM_DESCRIPTION.getDB() + " = ?," +
                        TblFlms.DB_FIELD_FILM_RELEASE_DATE.getDB() + " = ?, " +
                        TblFlms.DB_FIELD_FILM_DURATION.getDB() + " = ? , " +
                        TblFlms.DB_FIELD_FILM_RATE.getDB() + " = ? , " +
                        TblAdltRt.DB_FIELD_ADULT_RATE_ID.getDB() + " = ? " +
                        "WHERE " + TblFlms.DB_FIELD_FILM_ID + " = ?; " +
                        "SELECT * " +
                        "FROM " + TblFlms.DB_TABLE_FILMS.getDB() + " " +
                        "WHERE " + TblFlms.DB_FIELD_FILM_ID.getDB() + " = ?",
                film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),film.getRate(),
                film.getAdultRate(), filmID, filmID);
        if (filmRows.next() && filmRows.getString(TblFlms.DB_FIELD_FILM_NAME.getDB()).equals(film.getName())) {
            log.info(InfoFilmSuccessEnum.SUCCESS_UPDATE_FILM.getInfo(film.getId() + "/" + film.getName()));
            return film;
        } else {
            log.error(ErrorFilmEnum.FAIL_FILM_TABLE_VALIDATION
                    .getFilmError(film.getId() + "/" + film.getName()));
            throw new FilmorateValidationException("name");
        }
    }

    @Override
    public boolean isContainsFilm(long filmID) {
        log.info(InfoFilmStorageEnum.REQUEST_FILM_STORAGE_CONTAINS_FILM.getInfo(String.valueOf(filmID)));
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(
                "SELECT * " +
                "FROM " + TblFlms.DB_TABLE_FILMS.getDB() + " " +
                "WHERE " + TblFlms.DB_FIELD_FILM_ID.getDB() + " = ?", filmID);
        return filmRows.next();
    }

    @Override
    public List<Film> getFilms() {
        log.info(InfoFilmStorageEnum.REQUEST_FILM_STORAGE_GET_FILMS.getMessage());
        log.info(InfoFilmSuccessEnum.SUCCESS_GET_FILMS.getMessage());
        return jdbcTemplate.query(
                "SELECT f.*, g." + TblGnr.DB_FIELD_GENRE_NAME.getDB() + ", " +
                        "ar." + TblAdltRt.DB_FIELD_ADULT_RATE_NAME.getDB() + " " +
                    "FROM " + TblFlms.DB_TABLE_FILMS.getDB() + " AS f " +
                    "LEFT OUTER JOIN " + TblFlmsGnr.DB_TABLE_FILMS_GENRE.getDB() + " AS fg " +
                    "ON fg." + TblFlms.DB_FIELD_FILM_ID.getDB() + " = f." + TblFlms.DB_FIELD_FILM_ID.getDB() + " " +
                    "LEFT OUTER JOIN " + TblGnr.DB_TABLE_GENRE.getDB() + "AS g " +
                    "ON g." + TblGnr.DB_FIELD_GENRE_ID.getDB() + " = fg." + TblGnr.DB_FIELD_GENRE_ID.getDB(),
                (rs, rowNum) -> {
                    Film film = new Film(
                            rs.getString(TblFlms.DB_FIELD_FILM_NAME.getDB()),
                            rs.getString(TblFlms.DB_FIELD_FILM_DESCRIPTION.getDB()),
                            rs.getDate(TblFlms.DB_FIELD_FILM_RELEASE_DATE.getDB()).toLocalDate(),
                            rs.getInt(TblFlms.DB_FIELD_FILM_DURATION.getDB()),
                            rs.getInt(TblFlms.DB_FIELD_FILM_RATE.getDB()),
                            Genre.valueOf(rs.getString(TblGnr.DB_FIELD_GENRE_NAME.getDB())),
                            AdultRate.valueOf(rs.getString(TblAdltRt.DB_FIELD_ADULT_RATE_NAME.getDB())));
                    film.setId(rs.getLong(TblFlms.DB_FIELD_FILM_ID.getDB()));
                    film.setLikes(getLikesUS(film.getId()));
                    return film;
                });
    }

    @Override
    public Film getFilm(long filmID) {
        log.info(InfoFilmStorageEnum.REQUEST_FILM_STORAGE_GET_FILM.getInfo(String.valueOf(filmID)));
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(
                "SELECT f.*, g." + TblGnr.DB_FIELD_GENRE_NAME.getDB() + ", " +
                "ar." + TblAdltRt.DB_FIELD_ADULT_RATE_NAME.getDB() + " " +
                "FROM " + TblFlms.DB_TABLE_FILMS.getDB() + " AS f " +
                "LEFT OUTER JOIN " + TblFlmsGnr.DB_TABLE_FILMS_GENRE.getDB() + " AS fg " +
                "ON fg." + TblFlms.DB_FIELD_FILM_ID.getDB() + " = f." + TblFlms.DB_FIELD_FILM_ID.getDB() + " " +
                "LEFT OUTER JOIN " + TblGnr.DB_TABLE_GENRE.getDB() + "AS g " +
                "ON g." + TblGnr.DB_FIELD_GENRE_ID.getDB() + " = fg." + TblGnr.DB_FIELD_GENRE_ID.getDB() + " " +
                "WHERE " + TblFlms.DB_FIELD_FILM_ID.getDB() + " = ?", filmID);
        Film film = new Film(
                filmRows.getString(TblFlms.DB_FIELD_FILM_NAME.getDB()),
                filmRows.getString(TblFlms.DB_FIELD_FILM_DESCRIPTION.getDB()),
                filmRows.getDate(TblFlms.DB_FIELD_FILM_RELEASE_DATE.getDB()).toLocalDate(),
                filmRows.getInt(TblFlms.DB_FIELD_FILM_DURATION.getDB()),
                filmRows.getInt(TblFlms.DB_FIELD_FILM_RATE.getDB()),
                Genre.valueOf(filmRows.getString(TblGnr.DB_FIELD_GENRE_NAME.getDB())),
                AdultRate.valueOf(filmRows.getString(TblAdltRt.DB_FIELD_ADULT_RATE_NAME.getDB())));
        film.setId(film.getId());
        film.setLikes(getLikesUS(film.getId()));
        log.info(InfoFilmSuccessEnum.SUCCESS_GET_FILM.getInfo(film.getId() + "/" + film.getName()));
        return film;
    }

    private Set<Long> getLikesUS(Long filmID) {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                "SELECT " + TblUsrs.DB_FIELD_USER_ID.getDB() + " " +
                "FROM " + TblFlmLks.DB_TABLE_FILM_LIKES.getDB() + " " +
                "WHERE " + TblFlms.DB_FIELD_FILM_ID.getDB() + " = ?", filmID);
       return rows.stream().map(row -> (Long) row.get(TblUsrs.DB_FIELD_USER_ID.getDB())).collect(Collectors.toSet());
    }
}
