package ru.yandex.practicum.filmorate.service.genreService;

public enum GenreExceptionMessages {
    FILM_GENRE_ID_NOT_CONTAINS("ID запрошенного жанра не может быть меньше 1 и больше "),;
    private final String message;

    GenreExceptionMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
