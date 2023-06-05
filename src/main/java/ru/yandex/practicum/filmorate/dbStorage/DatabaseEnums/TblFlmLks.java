package ru.yandex.practicum.filmorate.dbStorage.DatabaseEnums;

public enum TblFlmLks {
    DB_TABLE_FILM_LIKES("film_likes");
    private final String databaseObjectName;

    TblFlmLks(String databaseObjectName) {
        this.databaseObjectName = databaseObjectName;
    }

    public String getDB() {
        return databaseObjectName;
    }
}
