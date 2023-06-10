package ru.yandex.practicum.filmorate.logEnum.genreEnums.InfoGenreEnums;

public enum InfoGenreStorageEnum {
    REQUEST_GENRE_STORAGE_GET_GENRES("Хранилище фильмов получило запрос на получение всех жанров"),
    REQUEST_GENRE_STORAGE_GET_GENRE("Хранилище фильмов получило запрос на получение жанра с ID: ");
    private final String message;

    InfoGenreStorageEnum(String message) {
        this.message = message;
    }

    public String getInfo(String value) {
        return message + value;
    }

    public String getMessage() {
        return message;
    }
}
