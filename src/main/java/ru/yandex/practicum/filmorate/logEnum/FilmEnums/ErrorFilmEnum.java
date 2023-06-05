package ru.yandex.practicum.filmorate.logEnum.FilmEnums;

public enum ErrorFilmEnum {
    FAIL_FILM_ID("Характеристика фильма: \"id\". Значение: "),
    FAIL_FILM_DESC("Характеристика фильма: \"description\". Значение: "),
    FAIL_FILM_RELEASE_DATE("Характеристика фильма: \"releaseDate\". Значение: "),
    FAIL_FILM_DURATION("Характеристика фильма: \"duration\". Значение: "),
    FAIL_FILM_COUNT("Характеристика полученного значения \"count\". Значение: "),
    FAIL_FILM_TABLE_VALIDATION("Характеристика пользователя: \"name\". Значение: ");
    private final String message;

    ErrorFilmEnum(String message) {
        this.message = message;
    }

    public String getFilmError(Object value) {
        if (value == null) {
            return message + null;
        } else {
            return message + value;
        }
    }
}