package ru.yandex.practicum.filmorate.dbStorage.DatabaseEnums;

public enum TblFrndshpSt {
    DB_TABLE_FRIENDSHIP_STATUS("friendship_status"),
    DB_FIELD_FRIENDSHIP_STATUS_ID("friendship_status_id"),
    DB_FIELD_FRIENDSHIP_STATUS_NAME("friendship_status_name");
    private final String databaseObjectName;

    TblFrndshpSt(String databaseObjectName) {
        this.databaseObjectName = databaseObjectName;
    }

    public String getDB() {
        return databaseObjectName;
    }
}
