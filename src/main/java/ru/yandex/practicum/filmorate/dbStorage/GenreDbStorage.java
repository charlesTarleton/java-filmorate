package ru.yandex.practicum.filmorate.dbStorage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dbStorage.DatabaseEnums.TblGnr;
import ru.yandex.practicum.filmorate.exception.FilmorateObjectException;
import ru.yandex.practicum.filmorate.logEnum.genreEnums.ErrorGenreEnum;
import ru.yandex.practicum.filmorate.logEnum.genreEnums.InfoGenreEnums.InfoGenreStorageEnum;
import ru.yandex.practicum.filmorate.logEnum.genreEnums.InfoGenreEnums.InfoGenreSuccessEnum;
import ru.yandex.practicum.filmorate.model.filmFields.Genre;
import ru.yandex.practicum.filmorate.service.genreService.GenreExceptionMessages;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Slf4j
@Component
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Genre> getGenres() {
        log.info(InfoGenreStorageEnum.REQUEST_GENRE_STORAGE_GET_GENRES.getMessage());
        log.info(InfoGenreSuccessEnum.SUCCESS_GET_GENRES.getMessage());
        return jdbcTemplate.query(
                "SELECT * " +
                        "FROM " + TblGnr.DB_TABLE_GENRE.getDB() + " " +
                        "ORDER BY " + TblGnr.DB_FIELD_GENRE_ID.getDB(), (rs, rowNum) ->
                        new Genre(rs.getInt(TblGnr.DB_FIELD_GENRE_ID.getDB()),
                                rs.getString(TblGnr.DB_FIELD_GENRE_NAME.getDB())));
    }

    public Genre getGenre(int genreID) {
        log.info(InfoGenreStorageEnum.REQUEST_GENRE_STORAGE_GET_GENRE.getInfo(String.valueOf(genreID)));
        int genresCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM " + TblGnr.DB_TABLE_GENRE.getDB(), Integer.class);
        if (genreID > genresCount || genreID < 1) {
            log.error(ErrorGenreEnum.FAIL_GENRE_ID.getGenreError(genreID));
            throw new FilmorateObjectException(GenreExceptionMessages
                    .FILM_GENRE_ID_NOT_CONTAINS.getMessage() + genresCount);
        }
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(
                "SELECT * " +
                        "FROM " + TblGnr.DB_TABLE_GENRE.getDB() + " " +
                        "WHERE " + TblGnr.DB_FIELD_GENRE_ID.getDB() + " = ?", genreID);
        filmRows.next();
        log.info(InfoGenreSuccessEnum.SUCCESS_GET_GENRE.getInfo(String.valueOf(genreID)));
        return new Genre(
                filmRows.getInt(TblGnr.DB_FIELD_GENRE_ID.getDB()),
                filmRows.getString(TblGnr.DB_FIELD_GENRE_NAME.getDB())
        );
    }
}
