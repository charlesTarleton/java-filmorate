package ru.yandex.practicum.filmorate.logEnum.FilmEnums.InfoFilmEnums;

public enum InfoFilmSuccessEnum {
    SUCCESS_ADD_FILM("Успешное добавление фильма с ID: "),
    SUCCESS_DELETE_FILM("Успешное удаление фильма с ID: "),
    SUCCESS_UPDATE_FILM("Успешное обновление фильма с ID: "),
    SUCCESS_GET_FILM("Успешное получение фильма с ID: "),
    SUCCESS_LIKE_FILM("Успешное добавление лайка фильму с ID: "),
    SUCCESS_DISLIKE_FILM("Успешное снятие лайка с фильма с ID: "),
    SUCCESS_GET_FILMS("Успешное получение всех фильмов"),
    SUCCESS_GET_MOST_LIKED_FILMS("Успешное получение самых популярных фильмов в количестве: "),
    SUCCESS_GET_GENRES("Успешное получение жанров"),
    SUCCESS_GET_GENRE("Успешное получение жанра с ID: "),
    SUCCESS_GET_ALL_MPA("Успешное получение рейтингов"),
    SUCCESS_GET_MPA("Успешное получение рейтинга с ID: ");
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
