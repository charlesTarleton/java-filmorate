package ru.yandex.practicum.filmorate.dbStorage.DatabaseEnums;

public enum TblUsrs {
    DB_TABLE_USERS("users"),
    DB_FIELD_USER_ID("user_id"),
    DB_FIELD_USER_EMAIL("user_email"),
    DB_FIELD_USER_LOGIN("user_login"),
    DB_FIELD_USER_NAME("user_name"),
    DB_FIELD_USER_BIRTHDAY("user_birthday");
    private final String databaseObjectName;

    TblUsrs(String databaseObjectName) {
        this.databaseObjectName = databaseObjectName;
    }

    public String getDB() {
        return databaseObjectName;
    }
}
