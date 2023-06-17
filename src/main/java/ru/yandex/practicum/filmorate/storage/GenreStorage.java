package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.filmFields.Genre;

import java.util.List;

public interface GenreStorage {
    public List<Genre> getGenres();

    public Genre getGenre(int genreID);
}
