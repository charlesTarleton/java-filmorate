package ru.yandex.practicum.filmorate.logEnum.FilmEnums.InfoFilmEnums;

public enum InfoFilmControllerEnum {
    REQUEST_FILM_CONTROLLER_ADD_FILM("Контроллер фильмов получил запрос на добавление фильма: "),
    REQUEST_FILM_CONTROLLER_DELETE_FILM("Контроллер фильмов получил запрос на удаление фильма с ID: "),
    REQUEST_FILM_CONTROLLER_UPDATE_FILM("Контроллер фильмов получил запрос на обновление фильма: "),
    REQUEST_FILM_CONTROLLER_GET_FILM("Контроллер фильмов получил запрос на получение фильма с ID: "),
    REQUEST_FILM_CONTROLLER_PUT_LIKE("Контроллер фильмов получил запрос на добавление лайка" +
            " ID фильма/ ID пользователя: "),
    REQUEST_FILM_CONTROLLER_DELETE_LIKE("Контроллер фильмов получил запрос на снятие лайка" +
            " ID фильма/ID пользователя: "),
    REQUEST_FILM_CONTROLLER_GET_FILMS("Контроллер фильмов получил запрос на получение всех фильмов"),
    REQUEST_FILM_CONTROLLER_GET_MOST_LIKED_FILMS("Контроллер фильмов получил запрос на получение" +
            " самых популярных фильмов в количестве: "),
    REQUEST_FILM_CONTROLLER_GET_GENRES("Контроллер фильмов получил запрос на получение всех жанров"),
    REQUEST_FILM_CONTROLLER_GET_GENRE("Контроллер фильмов получил запрос на получение жанра с ID: "),
    REQUEST_FILM_CONTROLLER_GET_ALL_MPA("Контроллер фильмов получил запрос на получение всех рейтингов"),
    REQUEST_FILM_CONTROLLER_GET_MPA("Контроллер фильмов получил запрос на получение рейтинга с ID: ");
    private final String message;

    InfoFilmControllerEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getInfo(String value) {
        return message + value;
    }
}
