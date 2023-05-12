package ru.yandex.practicum.filmorate.inMemoryStorage;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.logEnum.FilmEnums.InfoFilmEnums.InfoFilmStorageEnum;
import ru.yandex.practicum.filmorate.logEnum.FilmEnums.InfoFilmEnums.InfoFilmSuccessEnum;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Getter
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private Integer globalFilmID = 1;

    public Film addFilm(Film film) {
        log.info(InfoFilmStorageEnum.REQUEST_FILM_STORAGE_ADD_FILM.getInfo(film.toString()));
        film.setID(globalFilmID);
        log.info(InfoFilmSuccessEnum.SUCCESS_ADD_FILM.getInfo(film.getID() + "/" + film.getName()));
        films.put(globalFilmID++, film);
        return film;
    }

    public void deleteFilm(Integer filmID) {
        log.info(InfoFilmStorageEnum.REQUEST_FILM_STORAGE_DELETE_FILM.getInfo(String.valueOf(filmID)));
        log.info(InfoFilmSuccessEnum.SUCCESS_DELETE_FILM.getInfo(filmID + "/" + films.get(filmID).getName()));
        films.remove(filmID);
    }

    public Film updateFilm(Integer filmID, Film film) {
        log.info(InfoFilmStorageEnum.REQUEST_FILM_STORAGE_UPDATE_FILM.getInfo(film.toString()));
        log.info(InfoFilmSuccessEnum.SUCCESS_UPDATE_FILM.getInfo(filmID + "/" + film.getName()));
        films.put(filmID, film);
        return film;
    }
}
