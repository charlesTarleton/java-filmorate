package ru.yandex.practicum.filmorate.service.filmService;

public enum FilmExceptionMessages {
    FILM_DESCRIPTION_LENGTH_EXCEPTION_MESSAGE("длина описания не должна быть более 200 символов"),
    FILM_RELEASE_DATE_EXCEPTION_MESSAGE("дата премьеры фильма должна быть 28.12.1895 и позднее"),
    FILM_DURATION_EXCEPTION_MESSAGE("длительность фильма должна быть более 0 минут"),
    FILM_ID_CONTAINS_EXCEPTION_MESSAGE("в библиотеку фильмов можно добавить только фильм без ID"),
    FILM_ID_NOT_CONTAINS_EXCEPTION_MESSAGE("в библиотеке фильмов отсутствует фильм с указанным ID"),
    FILM_NOT_CONTAINS_LIKE_EXCEPTION_MESSAGE("У фильма с указанным ID отсутствует лайк от пользователя" +
            " с указанным ID"),
    FILM_COUNT_EXCEPTION_MESSAGE("количество запрошенных фильмов не может быть меньше 1"),
    FILM_GENRE_ID_NOT_CONTAINS("ID запрошенного жанра не может быть меньше 1 и больше "),
    FILM_MPA_ID_NOT_CONTAINS("ID запрошенного возрастного рейтинга не может быть меньше 1 и больше ");
    private final String message;

    FilmExceptionMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
