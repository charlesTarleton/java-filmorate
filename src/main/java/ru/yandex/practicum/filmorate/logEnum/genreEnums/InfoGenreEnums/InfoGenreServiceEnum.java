package ru.yandex.practicum.filmorate.logEnum.genreEnums.InfoGenreEnums;

public enum InfoGenreServiceEnum {
    GENRE_SERVICE_GET_GENRES("Сервис фильмов получил запрос на получение жанров"),
    GENRE_SERVICE_GET_GENRE("Сервис фильмов получил запрос на получение жанра с ID: ");
    private final String message;

    InfoGenreServiceEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getInfo(String value) {
        return message + value;
    }
}
