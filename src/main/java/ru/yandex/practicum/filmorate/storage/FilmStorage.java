package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    public Film addFilm(Film film);

    public void deleteFilm(long filmID);

    public Film updateFilm(long filmID, Film film);

    public boolean isContainsFilm(long filmID);

    public List<Film> getFilms();

    public  Film getFilm(long filmID);
}
