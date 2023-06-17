package ru.yandex.practicum.filmorate.dbStorage.DatabaseEnums;

public enum TblFlmsGnr {
    DB_TABLE_FILMS_GENRE("films_genre");
    private final String databaseObjectName;

    TblFlmsGnr(String databaseObjectName) {
        this.databaseObjectName = databaseObjectName;
    }

    public String getDB() {
        return databaseObjectName;
    }
}
