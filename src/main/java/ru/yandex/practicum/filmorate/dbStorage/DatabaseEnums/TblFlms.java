package ru.yandex.practicum.filmorate.dbStorage.DatabaseEnums;

public enum TblFlms {
    DB_TABLE_FILMS("films"),
    DB_FIELD_FILM_ID("film_id"),
    DB_FIELD_FILM_NAME("film_name"),
    DB_FIELD_FILM_DESCRIPTION("film_description"),
    DB_FIELD_FILM_RELEASE_DATE("film_release_date"),
    DB_FIELD_FILM_DURATION("film_duration"),
    DB_FIELD_FILM_RATE("film_rate");
    private final String databaseObjectName;

    TblFlms(String databaseObjectName) {
        this.databaseObjectName = databaseObjectName;
    }

    public String getDB() {
        return databaseObjectName;
    }
}
