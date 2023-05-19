package ru.yandex.practicum.filmorate.inMemoryStorage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.logEnum.FilmEnums.InfoFilmEnums.InfoFilmStorageEnum;
import ru.yandex.practicum.filmorate.logEnum.FilmEnums.InfoFilmEnums.InfoFilmSuccessEnum;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private Long globalFilmID = 1L;

    public Film addFilm(Film film) {
        log.info(InfoFilmStorageEnum.REQUEST_FILM_STORAGE_ADD_FILM.getInfo(film.toString()));
        film.setId(globalFilmID);
        log.info(InfoFilmSuccessEnum.SUCCESS_ADD_FILM.getInfo(film.getId() + "/" + film.getName()));
        films.put(globalFilmID++, film);
        return film;
    }

    public void deleteFilm(long filmID) {
        log.info(InfoFilmStorageEnum.REQUEST_FILM_STORAGE_DELETE_FILM.getInfo(String.valueOf(filmID)));
        log.info(InfoFilmSuccessEnum.SUCCESS_DELETE_FILM.getInfo(filmID + "/" + films.get(filmID).getName()));
        films.remove(filmID);
    }

    public Film updateFilm(long filmID, Film film) {
        log.info(InfoFilmStorageEnum.REQUEST_FILM_STORAGE_UPDATE_FILM.getInfo(film.toString()));
        log.info(InfoFilmSuccessEnum.SUCCESS_UPDATE_FILM.getInfo(filmID + "/" + film.getName()));
        films.put(filmID, film);
        return film;
    }

    @Override
    public boolean isContainsFilm(long filmID) {
        return films.containsKey(filmID);
    }

    public Film getFilm(long filmID) {
        log.info(InfoFilmStorageEnum.REQUEST_FILM_STORAGE_GET_FILM.getInfo(String.valueOf(filmID)));
        log.info(InfoFilmSuccessEnum.SUCCESS_GET_FILM
                .getInfo(filmID + "/" + films.get(filmID).getName()));
        return films.get(filmID);
    }

    public Map<Long, Film> getFilms() {
        return films;
    }
}
