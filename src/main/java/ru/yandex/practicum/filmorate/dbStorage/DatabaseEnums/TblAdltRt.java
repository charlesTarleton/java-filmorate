package ru.yandex.practicum.filmorate.dbStorage.DatabaseEnums;

public enum TblAdltRt {
    DB_TABLE_ADULT_RATE("adult_rate"),
    DB_FIELD_ADULT_RATE_ID("adult_rate_id"),
    DB_FIELD_ADULT_RATE_NAME("adult_rate_name");
    private final String databaseObjectName;

    TblAdltRt(String databaseObjectName) {
        this.databaseObjectName = databaseObjectName;
    }

    public String getDB() {
        return databaseObjectName;
    }
}
