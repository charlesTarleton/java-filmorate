package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

public interface FilmStorage {
    public Film addFilm(Film film);

    public void deleteFilm(Integer filmID);

    public Film updateFilm(Integer filmID, Film film);

    public Map<Integer, Film> getFilms();
}
