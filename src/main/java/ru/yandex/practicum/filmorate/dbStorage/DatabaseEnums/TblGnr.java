package ru.yandex.practicum.filmorate.dbStorage.DatabaseEnums;

public enum TblGnr {
    DB_TABLE_GENRE("genre"),
    DB_FIELD_GENRE_ID("genre_id"),
    DB_FIELD_GENRE_NAME("genre_name");
    private final String databaseObjectName;

    TblGnr(String databaseObjectName) {
        this.databaseObjectName = databaseObjectName;
    }

    public String getDB() {
        return databaseObjectName;
    }
}
