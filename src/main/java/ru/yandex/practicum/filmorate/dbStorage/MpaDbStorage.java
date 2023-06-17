package ru.yandex.practicum.filmorate.dbStorage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dbStorage.DatabaseEnums.TblAdltRt;
import ru.yandex.practicum.filmorate.exception.FilmorateObjectException;
import ru.yandex.practicum.filmorate.logEnum.mpaEnums.ErrorMpaEnum;
import ru.yandex.practicum.filmorate.logEnum.mpaEnums.InfoMpaEnums.InfoMpaStorageEnum;
import ru.yandex.practicum.filmorate.logEnum.mpaEnums.InfoMpaEnums.InfoMpaSuccessEnum;
import ru.yandex.practicum.filmorate.model.filmFields.MPA;
import ru.yandex.practicum.filmorate.service.genreService.GenreExceptionMessages;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Slf4j
@Component
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<MPA> getAllMpa() {
        log.info(InfoMpaStorageEnum.REQUEST_MPA_STORAGE_GET_ALL_MPA.getMessage());
        log.info(InfoMpaSuccessEnum.SUCCESS_GET_ALL_MPA.getMessage());
        return jdbcTemplate.query(
                "SELECT * " +
                        "FROM " + TblAdltRt.DB_TABLE_ADULT_RATE.getDB() + " " +
                        "ORDER BY " + TblAdltRt.DB_FIELD_MPA_ID.getDB(), (rs, rowNum) ->
                        new MPA(rs.getInt(TblAdltRt.DB_FIELD_MPA_ID.getDB()),
                                rs.getString(TblAdltRt.DB_FIELD_MPA_NAME.getDB())));
    }

    public MPA getMpa(int mpaID) {
        log.info(InfoMpaStorageEnum.REQUEST_MPA_STORAGE_GET_MPA.getInfo(String.valueOf(mpaID)));
        int ratesMpaCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) " +
                     "FROM " + TblAdltRt.DB_TABLE_ADULT_RATE.getDB(), Integer.class);
        if (mpaID > ratesMpaCount || mpaID < 1) {
            log.error(ErrorMpaEnum.FAIL_MPA_ID.getMpaError(mpaID));
            throw new FilmorateObjectException(GenreExceptionMessages
                    .FILM_GENRE_ID_NOT_CONTAINS.getMessage() + ratesMpaCount);
        }
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(
                "SELECT * " +
                        "FROM " + TblAdltRt.DB_TABLE_ADULT_RATE.getDB() + " " +
                        "WHERE " + TblAdltRt.DB_FIELD_MPA_ID.getDB() + " = ?", mpaID);
        filmRows.next();
        log.info(InfoMpaSuccessEnum.SUCCESS_GET_MPA.getInfo(String.valueOf(mpaID)));
        return new MPA(
                filmRows.getInt(TblAdltRt.DB_FIELD_MPA_ID.getDB()),
                filmRows.getString(TblAdltRt.DB_FIELD_MPA_NAME.getDB()));
    }
}
