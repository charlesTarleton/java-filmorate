package ru.yandex.practicum.filmorate.logEnum.genreEnums.InfoGenreEnums;

public enum InfoGenreSuccessEnum {
    SUCCESS_GET_GENRES("Успешное получение жанров"),
    SUCCESS_GET_GENRE("Успешное получение жанра с ID: ");
    private final String message;

    InfoGenreSuccessEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getInfo(String value) {
        return message + value;
    }
}
