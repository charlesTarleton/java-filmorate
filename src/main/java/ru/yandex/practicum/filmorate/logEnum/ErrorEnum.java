package ru.yandex.practicum.filmorate.logEnum;

public enum ErrorEnum {
    FAIL_ID(". Характеристика: \"id\". Значение: "),
    FAIL_FILM_DESC(". Характеристика: \"description\". Значение: "),
    FAIL_FILM_RELEASE_DATE(". Характеристика: \"releaseDate\". Значение: "),
    FAIL_FILM_DURATION(". Характеристика \"duration\". Значение: "),
    FAIL_BIRTHDAY_USER(". Характеристика \"birthday\". Значение: ");

    private final String message;

    ErrorEnum(String message) {
        this.message = message;
    }

    public String getFilmError(Integer id, String name, Object value) {
        if (value == null) {
            return "FilmId: " + id + ". Название фильма: " + name + message + null;
        } else {
            return "FilmId: " + id + ". Название фильма: " + name + message + value;
        }
    }

    public String getUserError(Integer id, String login, Object value) {
        if (value == null) {
            return "UserId: " + id + ". Логин пользователя: " + login + message + null;
        } else {
            return "UserId: " + id + ". Логин пользователя: " + login + message + value;
        }

    }
}
