package ru.yandex.practicum.filmorate.logEnum.filmEnums.InfoFilmEnums;

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
    REQUEST_FILM_STORAGE_PRIVATE_CREATE_FILM("В хранилище фильмов началось создание фильма"),
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
