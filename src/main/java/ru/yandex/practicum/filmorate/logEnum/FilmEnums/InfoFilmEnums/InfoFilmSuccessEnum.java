package ru.yandex.practicum.filmorate.logEnum.FilmEnums.InfoFilmEnums;

public enum InfoFilmSuccessEnum {
    SUCCESS_ADD_FILM("Успешное добавление фильма с ID/названием: "),
    SUCCESS_DELETE_FILM("Успешное удаление фильма с ID/названием: "),
    SUCCESS_UPDATE_FILM("Успешное обновление фильма с ID/названием: "),
    SUCCESS_GET_FILM("Успешное получение фильма с ID/названием: "),
    SUCCESS_LIKE_FILM("Успешное добавление лайка фильму с ID/названием: "),
    SUCCESS_DISLIKE_FILM("Успешное снятие лайка с фильма с ID/названием: "),
    SUCCESS_GET_FILMS("Успешное получение всех фильмов"),
    SUCCESS_GET_MOST_LIKED_FILMS("Успешное получение самых популярных фильмов в количестве: ");
    private final String message;
    InfoFilmSuccessEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getInfo(String value) {
        return message + value;
    }
}
