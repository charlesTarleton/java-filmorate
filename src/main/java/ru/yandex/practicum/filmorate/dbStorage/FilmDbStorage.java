package ru.yandex.practicum.filmorate.dbStorage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dbStorage.DatabaseEnums.*;
import ru.yandex.practicum.filmorate.logEnum.FilmEnums.InfoFilmEnums.InfoFilmServiceEnum;
import ru.yandex.practicum.filmorate.logEnum.FilmEnums.InfoFilmEnums.InfoFilmStorageEnum;
import ru.yandex.practicum.filmorate.logEnum.FilmEnums.InfoFilmEnums.InfoFilmSuccessEnum;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.filmFields.Genre;
import ru.yandex.practicum.filmorate.model.filmFields.MPA;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Component
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Film> addFilm(Film film) {
        log.info(InfoFilmServiceEnum.REQUEST_FILM_SERVICE_ADD_FILM.getInfo(film.getName()));
        jdbcTemplate.update(
                "INSERT INTO " + TblFlms.DB_TABLE_FILMS.getDB() + " (" +
                        TblFlms.DB_FIELD_FILM_NAME.getDB() + ", " +
                        TblFlms.DB_FIELD_FILM_DESCRIPTION.getDB() + ", " +
                        TblFlms.DB_FIELD_FILM_RELEASE_DATE.getDB() + ", " +
                        TblFlms.DB_FIELD_FILM_DURATION.getDB() + ", " +
                        TblFlms.DB_FIELD_FILM_RATE.getDB() + ", " +
                        TblAdltRt.DB_FIELD_MPA_ID.getDB() + ") " +
                    "VALUES(?, ?, ?, ?, ?, ?)",
                film.getName(), film.getDescription(), Date.valueOf(film.getReleaseDate()),
                film.getDuration(), film.getRate(), film.getMpa().getId());
        film.setId(jdbcTemplate.queryForObject(
                "SELECT MAX(" + TblFlms.DB_FIELD_FILM_ID.getDB() + ") " +
                    "FROM " + TblFlms.DB_TABLE_FILMS.getDB(), Long.class));
        addFilmGenres(film);
        log.info(InfoFilmSuccessEnum.SUCCESS_ADD_FILM.getInfo(String.valueOf(film.getId())));
        return Optional.of(film);
    }

    @Override
    public void deleteFilm(long filmID) {
        log.info(InfoFilmStorageEnum.REQUEST_FILM_STORAGE_DELETE_FILM.getInfo(String.valueOf(filmID)));
        jdbcTemplate.update(
                "DELETE FROM " + TblFlms.DB_TABLE_FILMS.getDB() + " " +
                    "WHERE + " + TblFlms.DB_FIELD_FILM_ID.getDB() + " = ?", filmID);
        log.info(InfoFilmSuccessEnum.SUCCESS_DELETE_FILM.getInfo(String.valueOf(filmID)));
    }

    @Override
    public Optional<Film> updateFilm(long filmID, Film film) {
        log.info(InfoFilmStorageEnum.REQUEST_FILM_STORAGE_UPDATE_FILM.getInfo(String.valueOf(filmID)));
        jdbcTemplate.update(
                "UPDATE " + TblFlms.DB_TABLE_FILMS.getDB() + " " +
                    "SET " + TblFlms.DB_FIELD_FILM_NAME.getDB() + " = ?, " +
                        TblFlms.DB_FIELD_FILM_DESCRIPTION.getDB() + " = ?," +
                        TblFlms.DB_FIELD_FILM_RELEASE_DATE.getDB() + " = ?, " +
                        TblFlms.DB_FIELD_FILM_DURATION.getDB() + " = ? , " +
                        TblFlms.DB_FIELD_FILM_RATE.getDB() + " = ? , " +
                        TblAdltRt.DB_FIELD_MPA_ID.getDB() + " = ? " +
                    "WHERE " + TblFlms.DB_FIELD_FILM_ID.getDB() + " = ?",
                film.getName(), film.getDescription(), Date.valueOf(film.getReleaseDate()),
                film.getDuration(),film.getRate(), film.getMpa().getId(), filmID);
        jdbcTemplate.update(
                "DELETE FROM " + TblFlmsGnr.DB_TABLE_FILMS_GENRE.getDB() + " " +
                    "WHERE + " + TblFlms.DB_FIELD_FILM_ID.getDB() + " = ?", filmID);
        addFilmGenres(film);
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(
                "SELECT f.*, ar." + TblAdltRt.DB_FIELD_MPA_NAME.getDB() + " " +
                    "FROM " + TblFlms.DB_TABLE_FILMS.getDB() + " AS f " +
                    "LEFT OUTER JOIN " + TblAdltRt.DB_TABLE_ADULT_RATE.getDB() + " AS ar " +
                    "ON f." + TblAdltRt.DB_FIELD_MPA_ID.getDB() + " = ar." + TblAdltRt.DB_FIELD_MPA_ID.getDB() + " " +
                    "WHERE f." + TblFlms.DB_FIELD_FILM_ID.getDB() + " = ?", filmID);
        if (filmRows.next()) {
            Film localFilm = createFilm(filmRows);
            localFilm.setId(filmRows.getLong(TblFlms.DB_FIELD_FILM_ID.getDB()));
            log.info(InfoFilmSuccessEnum.SUCCESS_UPDATE_FILM.getInfo(String.valueOf(filmID)));
            return Optional.of(localFilm);
        }
        return Optional.empty();
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
                "SELECT f.*, ar." + TblAdltRt.DB_FIELD_MPA_NAME.getDB() + " " +
                    "FROM " + TblFlms.DB_TABLE_FILMS.getDB() + " AS f " +
                    "LEFT OUTER JOIN " + TblAdltRt.DB_TABLE_ADULT_RATE.getDB() + " AS ar " +
                    "ON f." + TblAdltRt.DB_FIELD_MPA_ID.getDB() + " = ar." +
                        TblAdltRt.DB_FIELD_MPA_ID.getDB(), (rs, rowNum) -> {
                    Film localFilm = new Film(rs.getString(TblFlms.DB_FIELD_FILM_NAME.getDB()),
                            rs.getString(TblFlms.DB_FIELD_FILM_DESCRIPTION.getDB()),
                            rs.getDate(TblFlms.DB_FIELD_FILM_RELEASE_DATE.getDB()).toLocalDate(),
                            rs.getInt(TblFlms.DB_FIELD_FILM_DURATION.getDB()),
                            rs.getInt(TblFlms.DB_FIELD_FILM_RATE.getDB()),
                            new MPA(rs.getInt(TblAdltRt.DB_FIELD_MPA_ID.getDB()),
                                    rs.getString(TblAdltRt.DB_FIELD_MPA_NAME.getDB())));
                    localFilm.setId(rs.getLong(TblFlms.DB_FIELD_FILM_ID.getDB()));
                    localFilm.setGenres(getGenres(localFilm.getId()));
                    localFilm.setLikes(getLikes(localFilm.getId()));
                    return localFilm;
                });
    }

    @Override
    public Optional<Film> getFilm(long filmID) {
        log.info(InfoFilmStorageEnum.REQUEST_FILM_STORAGE_GET_FILM.getInfo(String.valueOf(filmID)));
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(
                "SELECT f.*, ar." + TblAdltRt.DB_FIELD_MPA_NAME.getDB() + " " +
                    "FROM " + TblFlms.DB_TABLE_FILMS.getDB() + " AS f " +
                    "LEFT OUTER JOIN " + TblAdltRt.DB_TABLE_ADULT_RATE.getDB() + " AS ar " +
                    "ON f." + TblAdltRt.DB_FIELD_MPA_ID.getDB() + " = ar." +
                        TblAdltRt.DB_FIELD_MPA_ID.getDB() + " " +
                    "WHERE " + TblFlms.DB_FIELD_FILM_ID.getDB() + " = ?", filmID);
        if (filmRows.next()) {
            log.info(InfoFilmSuccessEnum.SUCCESS_GET_FILM.getInfo(String.valueOf(filmID)));
            return Optional.of(createFilm(filmRows));
        }
        return Optional.empty();
    }

    public Optional<Film> putLike(long filmID, long userID) {
        log.info(InfoFilmStorageEnum.REQUEST_FILM_STORAGE_LIKE_FILM.getInfo(String.valueOf(filmID)));
        jdbcTemplate.update(
                "INSERT INTO " + TblFlmLks.DB_TABLE_FILM_LIKES.getDB() + " (" +
                        TblFlms.DB_FIELD_FILM_ID.getDB() + ", " + TblUsrs.DB_FIELD_USER_ID.getDB() + ") " +
                    "VALUES (?, ?)", filmID, userID);
        log.info(InfoFilmSuccessEnum.SUCCESS_LIKE_FILM.getInfo(String.valueOf(filmID)));
        return getFilm(filmID);
    }

    public Optional<Film> deleteLike(long filmID, long userID) {
        log.info(InfoFilmStorageEnum.REQUEST_FILM_STORAGE_DISLIKE_FILM.getInfo(String.valueOf(filmID)));
        jdbcTemplate.update(
                "DELETE FROM " + TblFlmLks.DB_TABLE_FILM_LIKES.getDB() + " " +
                    "WHERE " + TblFlms.DB_FIELD_FILM_ID.getDB() + " = ? AND " +
                        TblUsrs.DB_FIELD_USER_ID.getDB() + " = ?", filmID, userID);
        log.info(InfoFilmSuccessEnum.SUCCESS_DISLIKE_FILM.getInfo(String.valueOf(filmID)));
        return getFilm(filmID);
    }

    public List<Film> getMostLikedFilms(int countFilms) {
        log.info(InfoFilmStorageEnum.REQUEST_FILM_STORAGE_MOST_LIKED_FILMS.getInfo(String.valueOf(countFilms)));
        log.info(InfoFilmSuccessEnum.SUCCESS_GET_MOST_LIKED_FILMS.getInfo(String.valueOf(countFilms)));
        return jdbcTemplate.query(
                "SELECT f.*, ar." + TblAdltRt.DB_FIELD_MPA_NAME.getDB() + " " +
                    "FROM " + TblFlms.DB_TABLE_FILMS.getDB() + " AS f " +
                    "LEFT OUTER JOIN " + TblFlmLks.DB_TABLE_FILM_LIKES.getDB() + " AS fl " +
                    "ON f." + TblFlms.DB_FIELD_FILM_ID.getDB() + " = fl." + TblFlms.DB_FIELD_FILM_ID.getDB() + " " +
                    "LEFT OUTER JOIN " + TblAdltRt.DB_TABLE_ADULT_RATE.getDB() + " AS ar " +
                    "ON f." + TblAdltRt.DB_FIELD_MPA_ID.getDB() + " = ar." +
                        TblAdltRt.DB_FIELD_MPA_ID.getDB() + " " +
                    "GROUP BY f." + TblFlms.DB_FIELD_FILM_ID.getDB() + " " +
                    "ORDER BY COUNT(fl." + TblUsrs.DB_FIELD_USER_ID.getDB() + ") DESC " +
                    "LIMIT ?", (rs, rowNum) -> {
                    Film localFilm = new Film(rs.getString(TblFlms.DB_FIELD_FILM_NAME.getDB()),
                            rs.getString(TblFlms.DB_FIELD_FILM_DESCRIPTION.getDB()),
                            rs.getDate(TblFlms.DB_FIELD_FILM_RELEASE_DATE.getDB()).toLocalDate(),
                            rs.getInt(TblFlms.DB_FIELD_FILM_DURATION.getDB()),
                            rs.getInt(TblFlms.DB_FIELD_FILM_RATE.getDB()),
                            new MPA(rs.getInt(TblAdltRt.DB_FIELD_MPA_ID.getDB()),
                                    rs.getString(TblAdltRt.DB_FIELD_MPA_NAME.getDB())));
                    localFilm.setId(rs.getLong(TblFlms.DB_FIELD_FILM_ID.getDB()));
                    localFilm.setGenres(getGenres(localFilm.getId()));
                    localFilm.setLikes(getLikes(localFilm.getId()));
                    return localFilm;
                }, countFilms);
    }

    public List<Genre> getGenres() {
        log.info(InfoFilmStorageEnum.REQUEST_FILM_STORAGE_GET_GENRES.getMessage());
        log.info(InfoFilmSuccessEnum.SUCCESS_GET_GENRES.getMessage());
        return jdbcTemplate.query(
                "SELECT * " +
                    "FROM " + TblGnr.DB_TABLE_GENRE.getDB() + " " +
                    "ORDER BY " + TblGnr.DB_FIELD_GENRE_ID.getDB(), (rs, rowNum) ->
                    new Genre (rs.getInt(TblGnr.DB_FIELD_GENRE_ID.getDB()),
                            rs.getString(TblGnr.DB_FIELD_GENRE_NAME.getDB())));
    }

    public Genre getGenre(int genreID) {
        log.info(InfoFilmStorageEnum.REQUEST_FILM_STORAGE_GET_GENRE.getInfo(String.valueOf(genreID)));
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(
                "SELECT * " +
                    "FROM " + TblGnr.DB_TABLE_GENRE.getDB() + " " +
                    "WHERE " + TblGnr.DB_FIELD_GENRE_ID.getDB() + " = ?", genreID);
        filmRows.next();
        log.info(InfoFilmSuccessEnum.SUCCESS_GET_GENRE.getInfo(String.valueOf(genreID)));
        return new Genre(
                filmRows.getInt(TblGnr.DB_FIELD_GENRE_ID.getDB()),
                filmRows.getString(TblGnr.DB_FIELD_GENRE_NAME.getDB())
        );
    }

    public List<MPA> getAllMpa() {
        log.info(InfoFilmStorageEnum.REQUEST_FILM_STORAGE_GET_ALL_MPA.getMessage());
        log.info(InfoFilmSuccessEnum.SUCCESS_GET_ALL_MPA.getMessage());
        return jdbcTemplate.query(
                "SELECT * " +
                    "FROM " + TblAdltRt.DB_TABLE_ADULT_RATE.getDB() + " " +
                    "ORDER BY " + TblAdltRt.DB_FIELD_MPA_ID.getDB(), (rs, rowNum) ->
                        new MPA (rs.getInt(TblAdltRt.DB_FIELD_MPA_ID.getDB()),
                                rs.getString(TblAdltRt.DB_FIELD_MPA_NAME.getDB())));
    }

    public MPA getMpa(int mpaID) {
        log.info(InfoFilmStorageEnum.REQUEST_FILM_STORAGE_GET_MPA.getInfo(String.valueOf(mpaID)));
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(
                "SELECT * " +
                    "FROM " + TblAdltRt.DB_TABLE_ADULT_RATE.getDB() + " " +
                    "WHERE " + TblAdltRt.DB_FIELD_MPA_ID.getDB() + " = ?", mpaID);
        filmRows.next();
        log.info(InfoFilmSuccessEnum.SUCCESS_GET_MPA.getInfo(String.valueOf(mpaID)));
        return new MPA(
                filmRows.getInt(TblAdltRt.DB_FIELD_MPA_ID.getDB()),
                filmRows.getString(TblAdltRt.DB_FIELD_MPA_NAME.getDB()));
    }

    private Film createFilm(SqlRowSet filmRows) {
        log.info(InfoFilmStorageEnum.REQUEST_FILM_STORAGE_PRIVATE_CREATE_FILM.getMessage());
        Film film = new Film(filmRows.getString(TblFlms.DB_FIELD_FILM_NAME.getDB()),
                filmRows.getString(TblFlms.DB_FIELD_FILM_DESCRIPTION.getDB()),
                filmRows.getDate(TblFlms.DB_FIELD_FILM_RELEASE_DATE.getDB()).toLocalDate(),
                filmRows.getInt(TblFlms.DB_FIELD_FILM_DURATION.getDB()),
                filmRows.getInt(TblFlms.DB_FIELD_FILM_RATE.getDB()),
                new MPA(filmRows.getInt(TblAdltRt.DB_FIELD_MPA_ID.getDB()),
                        filmRows.getString(TblAdltRt.DB_FIELD_MPA_NAME.getDB())));
        film.setId(filmRows.getLong(TblFlms.DB_FIELD_FILM_ID.getDB()));
        film.setGenres(getGenres(film.getId()));
        film.setLikes(getLikes(film.getId()));
        return film;
    }

    private Set<Genre> getGenres(long filmID) {
        log.info(InfoFilmStorageEnum.REQUEST_FILM_STORAGE_PRIVATE_GET_GENRES.getInfo(String.valueOf(filmID)));
        return new HashSet<>(jdbcTemplate.query(
                "SELECT g.* " +
                    "FROM " + TblGnr.DB_TABLE_GENRE.getDB() + " AS g " +
                    "LEFT OUTER JOIN " + TblFlmsGnr.DB_TABLE_FILMS_GENRE.getDB() + " AS fg " +
                    "ON g." + TblGnr.DB_FIELD_GENRE_ID.getDB() + " = fg." +
                        TblGnr.DB_FIELD_GENRE_ID.getDB() + " " +
                    "WHERE fg." + TblFlms.DB_FIELD_FILM_ID.getDB() + " = ? " +
                    "ORDER BY " + TblGnr.DB_FIELD_GENRE_ID.getDB(), (rs, rowNum) ->
                        new Genre (rs.getInt(TblGnr.DB_FIELD_GENRE_ID.getDB()),
                                rs.getString(TblGnr.DB_FIELD_GENRE_NAME.getDB())), filmID));
    }

    private Set<Long> getLikes(long filmID) {
        log.info(InfoFilmStorageEnum.REQUEST_FILM_STORAGE_PRIVATE_GET_LIKES.getInfo(String.valueOf(filmID)));
        return new HashSet<>(jdbcTemplate.queryForList(
                "SELECT " + TblUsrs.DB_FIELD_USER_ID.getDB() + " " +
                    "FROM " + TblFlmLks.DB_TABLE_FILM_LIKES.getDB() + " " +
                    "WHERE " + TblFlms.DB_FIELD_FILM_ID.getDB() + " = ?", Long.class, filmID));
    }

    private void addFilmGenres(Film film) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO " + TblFlmsGnr.DB_TABLE_FILMS_GENRE.getDB() + " (" +
                        TblFlms.DB_FIELD_FILM_ID.getDB() + ", " +
                        TblGnr.DB_FIELD_GENRE_ID.getDB() + ") " +
                        "VALUES (?, ?)", new BatchPreparedStatementSetter() {
                    final Iterator<Genre> iterator = film.getGenres().iterator();
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Genre genre = iterator.next();
                        ps.setLong(1, film.getId());
                        ps.setInt(2, genre.getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return film.getGenres().size();
                    }
                }
        );
    }
}