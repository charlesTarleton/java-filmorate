package ru.yandex.practicum.filmorate.dbStorage.DatabaseEnums;

public enum TblUsrFrndshp {
    DB_TABLE_USER_FRIENDSHIP("user_friendship"),
    DB_FIELD_FRIEND_USER_ID("friend_user_id");
    private final String databaseObjectName;

    TblUsrFrndshp(String databaseObjectName) {
        this.databaseObjectName = databaseObjectName;
    }

    public String getDB() {
        return databaseObjectName;
    }
}
