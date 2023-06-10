package ru.yandex.practicum.filmorate.dbStorage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dbStorage.DatabaseEnums.*;
import ru.yandex.practicum.filmorate.logEnum.filmEnums.InfoFilmEnums.InfoFilmServiceEnum;
import ru.yandex.practicum.filmorate.logEnum.filmEnums.InfoFilmEnums.InfoFilmStorageEnum;
import ru.yandex.practicum.filmorate.logEnum.filmEnums.InfoFilmEnums.InfoFilmSuccessEnum;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.filmFields.Genre;
import ru.yandex.practicum.filmorate.model.filmFields.MPA;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private static final String requestGetFilmSql = "SELECT f.*, " +
            "ar." + TblAdltRt.DB_FIELD_MPA_NAME.getDB() + " " +
            "FROM " + TblFlms.DB_TABLE_FILMS.getDB() + " AS f " +
            "LEFT OUTER JOIN " + TblAdltRt.DB_TABLE_ADULT_RATE.getDB() + " AS ar " +
            "ON f." + TblAdltRt.DB_FIELD_MPA_ID.getDB() + " = ar." +
            TblAdltRt.DB_FIELD_MPA_ID.getDB() + " " +
            "LEFT OUTER JOIN " + TblFlmsGnr.DB_TABLE_FILMS_GENRE.getDB() + " AS fg " +
            "ON f." + TblFlms.DB_FIELD_FILM_ID.getDB() + " = fg." +
            TblFlms.DB_FIELD_FILM_ID.getDB() + " " +
            "LEFT OUTER JOIN " + TblFlmLks.DB_TABLE_FILM_LIKES.getDB() + " AS fl " +
            "ON f." + TblFlms.DB_FIELD_FILM_ID.getDB() + " = fl." + TblFlms.DB_FIELD_FILM_ID.getDB();

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Film> addFilm(Film film) {
        log.info(InfoFilmServiceEnum.REQUEST_FILM_SERVICE_ADD_FILM.getInfo(film.getName()));
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement prst = connection.prepareStatement(
                    "INSERT INTO " + TblFlms.DB_TABLE_FILMS.getDB() + " (" +
                            TblFlms.DB_FIELD_FILM_NAME.getDB() + ", " +
                            TblFlms.DB_FIELD_FILM_DESCRIPTION.getDB() + ", " +
                            TblFlms.DB_FIELD_FILM_RELEASE_DATE.getDB() + ", " +
                            TblFlms.DB_FIELD_FILM_DURATION.getDB() + ", " +
                            TblFlms.DB_FIELD_FILM_RATE.getDB() + ", " +
                            TblAdltRt.DB_FIELD_MPA_ID.getDB() + ") " +
                            "VALUES(?, ?, ?, ?, ?, ?)", new String[]{TblFlms.DB_FIELD_FILM_ID.getDB()});
            prst.setString(1, film.getName());
            prst.setString(2, film.getDescription());
            prst.setDate(3, Date.valueOf(film.getReleaseDate()));
            prst.setInt(4, film.getDuration());
            if (film.getRate() == null) {
                prst.setNull(5, Types.INTEGER);
            } else {
                prst.setInt(5, film.getRate());
            }
            if (film.getMpa().getId() == null){
                prst.setNull(6, Types.INTEGER);
            } else {
                prst.setInt(6, film.getMpa().getId());
            }
            return prst;
        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());
        addFilmGenres(film);
        log.info(InfoFilmSuccessEnum.SUCCESS_ADD_FILM.getInfo(String.valueOf(film.getId())));
        return getFilm(film.getId());
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
                "DELETE FROM " + TblFlmsGnr.DB_TABLE_FILMS_GENRE.getDB() + " " +
                        "WHERE + " + TblFlms.DB_FIELD_FILM_ID.getDB() + " = ?; " +
                        "UPDATE " + TblFlms.DB_TABLE_FILMS.getDB() + " " +
                        "SET " + TblFlms.DB_FIELD_FILM_NAME.getDB() + " = ?, " +
                        TblFlms.DB_FIELD_FILM_DESCRIPTION.getDB() + " = ?," +
                        TblFlms.DB_FIELD_FILM_RELEASE_DATE.getDB() + " = ?, " +
                        TblFlms.DB_FIELD_FILM_DURATION.getDB() + " = ? , " +
                        TblFlms.DB_FIELD_FILM_RATE.getDB() + " = ? , " +
                        TblAdltRt.DB_FIELD_MPA_ID.getDB() + " = ? " +
                        "WHERE " + TblFlms.DB_FIELD_FILM_ID.getDB() + " = ?",
                filmID, film.getName(), film.getDescription(), Date.valueOf(film.getReleaseDate()),
                film.getDuration(), film.getRate(), film.getMpa().getId(), filmID);
        addFilmGenres(film);
        log.info(InfoFilmSuccessEnum.SUCCESS_UPDATE_FILM.getInfo(String.valueOf(filmID)));
        return getFilm(filmID);
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
        Map<Long, Film> filmMap = jdbcTemplate.query(
                requestGetFilmSql + " " +
                    "GROUP BY f." + TblFlms.DB_FIELD_FILM_ID.getDB(), this::createFilmForGetList)
                .stream().collect(Collectors.toMap(Film::getId, Function.identity()));
        return getGenreForMoreFilms(filmMap);
    }

    @Override
    public Optional<Film> getFilm(long filmID) {
        log.info(InfoFilmStorageEnum.REQUEST_FILM_STORAGE_GET_FILM.getInfo(String.valueOf(filmID)));
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(requestGetFilmSql + " " +
                "WHERE f." + TblFlms.DB_FIELD_FILM_ID.getDB() + " = ? " +
                "GROUP BY f." + TblFlms.DB_FIELD_FILM_ID.getDB(), filmID);
        if (filmRows.next()) {
            log.info(InfoFilmSuccessEnum.SUCCESS_GET_FILM.getInfo(String.valueOf(filmID)));
            return Optional.of(createFilmForGetOne(filmRows));
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
        List<Film> filmList = jdbcTemplate.query(
                requestGetFilmSql + " " +
                        "GROUP BY f." + TblFlms.DB_FIELD_FILM_ID.getDB() + " " +
                        "ORDER BY COUNT(fl." + TblUsrs.DB_FIELD_USER_ID.getDB() + ") DESC " +
                        "LIMIT ?", this::createFilmForGetList, countFilms);
        Map<Long, Film> filmMap = new LinkedHashMap<>();
        for (Film film : filmList) { // Через stream() Collectors.toMap нарушается порядок
            filmMap.put(film.getId(), film);
        }
        return getGenreForMoreFilms(filmMap);
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

    private Film createFilmForGetOne(SqlRowSet filmRows) {
        log.info(InfoFilmStorageEnum.REQUEST_FILM_STORAGE_PRIVATE_CREATE_FILM.getMessage());
        Film film = new Film(filmRows.getString(TblFlms.DB_FIELD_FILM_NAME.getDB()),
                filmRows.getString(TblFlms.DB_FIELD_FILM_DESCRIPTION.getDB()),
                filmRows.getDate(TblFlms.DB_FIELD_FILM_RELEASE_DATE.getDB()).toLocalDate(),
                filmRows.getInt(TblFlms.DB_FIELD_FILM_DURATION.getDB()),
                filmRows.getInt(TblFlms.DB_FIELD_FILM_RATE.getDB()),
                new MPA(filmRows.getInt(TblAdltRt.DB_FIELD_MPA_ID.getDB()),
                        filmRows.getString(TblAdltRt.DB_FIELD_MPA_NAME.getDB())));
        film.setId(filmRows.getLong(TblFlms.DB_FIELD_FILM_ID.getDB()));
        film.setGenres((jdbcTemplate.query(
                "SELECT g.* " +
                    "FROM " + TblGnr.DB_TABLE_GENRE.getDB() + " AS g " +
                    "LEFT OUTER JOIN " + TblFlmsGnr.DB_TABLE_FILMS_GENRE.getDB() + " AS fg " +
                    "ON g." + TblGnr.DB_FIELD_GENRE_ID.getDB() + " = fg." + TblGnr.DB_FIELD_GENRE_ID.getDB() + " " +
                    "WHERE fg." + TblFlms.DB_FIELD_FILM_ID.getDB() + " = ?",
                this::getGenreForOneFilm, film.getId())).stream().sorted(Comparator.comparing(Genre::getId))
                .collect(Collectors.toSet()));
        return film;
    }

    private Genre getGenreForOneFilm(ResultSet rs, int rowNumber) throws SQLException {
        return new Genre(rs.getInt(TblGnr.DB_FIELD_GENRE_ID.getDB()),
                rs.getString(TblGnr.DB_FIELD_GENRE_NAME.getDB()));
    }

    private Film createFilmForGetList(ResultSet rs, int rowNumber) throws SQLException {
        Film film = new Film(
                rs.getString(TblFlms.DB_FIELD_FILM_NAME.getDB()),
                rs.getString(TblFlms.DB_FIELD_FILM_DESCRIPTION.getDB()),
                rs.getDate(TblFlms.DB_FIELD_FILM_RELEASE_DATE.getDB()).toLocalDate(),
                rs.getInt(TblFlms.DB_FIELD_FILM_DURATION.getDB()),
                rs.getInt(TblFlms.DB_FIELD_FILM_RATE.getDB()),
                new MPA(rs.getInt(TblAdltRt.DB_FIELD_MPA_ID.getDB()),
                        rs.getString(TblAdltRt.DB_FIELD_MPA_NAME.getDB())));
        film.setId(rs.getLong(TblFlms.DB_FIELD_FILM_ID.getDB()));
        return film;
    }

    private List<Film> getGenreForMoreFilms(Map<Long, Film> filmMap) {
        Long[] filmsID = filmMap.keySet().toArray(new Long[0]);
        jdbcTemplate.query(
                "SELECT g.*, fg." + TblFlms.DB_FIELD_FILM_ID.getDB() + " " +
                    "FROM " + TblGnr.DB_TABLE_GENRE.getDB() + " AS g " +
                    "LEFT OUTER JOIN " + TblFlmsGnr.DB_TABLE_FILMS_GENRE.getDB() + " AS fg " +
                    "ON g." + TblGnr.DB_FIELD_GENRE_ID.getDB() + " = fg." + TblGnr.DB_FIELD_GENRE_ID.getDB() + " " +
                    "WHERE fg." + TblFlms.DB_FIELD_FILM_ID.getDB() + " IN (" +
                        String.join(", ", Collections.nCopies(filmsID.length, "?")) + ")", filmsID,
                (RowMapper<Film>) (rs, rowNum) -> {
                    filmMap.get(rs.getLong(TblFlms.DB_FIELD_FILM_ID.getDB())).getGenres().add(new Genre(
                            rs.getInt(TblGnr.DB_FIELD_GENRE_ID.getDB()),
                            rs.getString(TblGnr.DB_FIELD_GENRE_NAME.getDB())));
                    return null;
                });
        return new ArrayList<>(filmMap.values());
    }
}