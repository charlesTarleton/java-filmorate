package ru.yandex.practicum.filmorate.logEnum.genreEnums.InfoGenreEnums;

public enum InfoGenreControllerEnum {
    REQUEST_GENRE_CONTROLLER_GET_GENRES("Контроллер фильмов получил запрос на получение всех жанров"),
    REQUEST_GENRE_CONTROLLER_GET_GENRE("Контроллер фильмов получил запрос на получение жанра с ID: ");
    private final String message;

    InfoGenreControllerEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getInfo(String value) {
        return message + value;
    }
}
