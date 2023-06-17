package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    public Optional<Film> addFilm(Film film);

    public void deleteFilm(long filmID);

    public Optional<Film> updateFilm(long filmID, Film film);

    public boolean isContainsFilm(long filmID);

    public List<Film> getFilms();

    public  Optional<Film> getFilm(long filmID);

    public Optional<Film> putLike(long filmID, long userID);

    public Optional<Film> deleteLike(long filmID, long userID);

    public List<Film> getMostLikedFilms(int countFilms);
}
