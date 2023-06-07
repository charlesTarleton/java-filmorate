package ru.yandex.practicum.filmorate.logEnum.FilmEnums.InfoFilmEnums;

public enum InfoFilmStorageEnum {
    REQUEST_FILM_STORAGE_ADD_FILM("Хранилище фильмов получило запрос на добавление фильма: "),
    REQUEST_FILM_STORAGE_DELETE_FILM("Хранилище фильмов получило запрос  на удаление фильма с ID: "),
    REQUEST_FILM_STORAGE_UPDATE_FILM("Хранилище фильмов получило запрос на обновление фильма: "),
    REQUEST_FILM_STORAGE_GET_FILM("Хранилище фильмов получило запрос на получение фильма с ID: "),
    REQUEST_FILM_STORAGE_CONTAINS_FILM("Хранилище фильмов получило запрос на проверку наличия фильма с ID: "),
    REQUEST_FILM_STORAGE_GET_FILMS("Хранилище фильмов получило запрос на получение всех фильмов"),
    REQUEST_FILM_STORAGE_LIKE_FILM("Хранилище фильмов получило запрос на лайк фильма с ID: "),
    REQUEST_FILM_STORAGE_DISLIKE_FILM("Хранилище фильмов получило запрос на снятие лайка с фильма с ID: "),
    REQUEST_FILM_STORAGE_MOST_LIKED_FILMS("Хранилище фильмов получило запрос на получение фильмов "),
    REQUEST_FILM_STORAGE_GET_GENRES("Хранилище фильмов получило запрос на получение всех жанров"),
    REQUEST_FILM_STORAGE_GET_GENRE("Хранилище фильмов получило запрос на получение жанра с ID: "),
    REQUEST_FILM_STORAGE_GET_ALL_MPA("Хранилище фильмов получило запрос на получение всех рейтингов"),
    REQUEST_FILM_STORAGE_GET_MPA("Хранилище фильмов получило запрос на получение рейтинга с ID: "),
    REQUEST_FILM_STORAGE_PRIVATE_CREATE_FILM("В хранилище фильмов началось создание фильма"),
    REQUEST_FILM_STORAGE_PRIVATE_GET_GENRES("В хранилище фильмов были запрошены жанры фильма с ID: "),
    REQUEST_FILM_STORAGE_PRIVATE_GET_LIKES("В хранилище фильмов были запрошены лайки фильма с ID: ");
    private final String message;

    InfoFilmStorageEnum(String message) {
        this.message = message;
    }

    public String getInfo(String value) {
        return message + value;
    }

    public String getMessage() {
        return message;
    }
}
