package ru.yandex.practicum.filmorate.logEnum.genreEnums;

public enum ErrorGenreEnum {
    FAIL_GENRE_ID("Характеристика жанра: \"id\". Значение: ");
    private final String message;

    ErrorGenreEnum(String message) {
        this.message = message;
    }

    public String getGenreError(Object value) {
        if (value == null) {
            return message + null;
        } else {
            return message + value;
        }
    }
}