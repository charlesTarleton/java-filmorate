package ru.yandex.practicum.filmorate.logEnum.FilmEnums.InfoFilmEnums;

public enum InfoFilmServiceEnum {
    REQUEST_FILM_SERVICE_ADD_FILM("Сервис фильмов получил запрос на добавление фильма: "),
    REQUEST_FILM_SERVICE_DELETE_FILM("Сервис фильмов получил запрос на удаление фильма с ID: "),
    REQUEST_FILM_SERVICE_UPDATE_FILM("Сервис фильмов получил запрос на обновление фильма: "),
    REQUEST_FILM_SERVICE_GET_FILM("Сервис фильмов получил запрос на получение пользователя с ID: "),
    REQUEST_FILM_SERVICE_PUT_LIKE("Сервис фильмов получил запрос на добавление лайка" +
            " ID фильма/ID пользователя: "),
    REQUEST_FILM_SERVICE_DELETE_LIKE("Сервис фильмов получил запрос на снятие лайка" +
            " ID фильма/ID пользователя: "),
    REQUEST_FILM_SERVICE_GET_FILMS("Сервис фильмов получил запрос на получение всех фильмов"),
    REQUEST_FILM_SERVICE_GET_MOST_LIKED_FILMS("Сервис фильмов получил запрос на получение" +
            " самых популярных фильмов в количестве: "),
    FILM_SERVICE_VALIDATE_FILM("Начата процедура валидации фильма: "),
    FILM_SERVICE_PARSE_FILM_ID("Получен запрос на парсинг ID фильма");
    private final String message;

    InfoFilmServiceEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getInfo(String value) {
        return message + value;
    }
}
