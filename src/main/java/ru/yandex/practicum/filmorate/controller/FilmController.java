package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.logEnum.FilmEnums.InfoFilmEnums.InfoFilmControllerEnum;
import ru.yandex.practicum.filmorate.model.filmFields.Genre;
import ru.yandex.practicum.filmorate.model.filmFields.MPA;
import ru.yandex.practicum.filmorate.service.filmService.FilmService;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping
public class FilmController {
    private final FilmService filmService;
    private static final String DEFAULT_COUNT_OF_MOST_LIKED_FILMS = "10";

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping("/films")
    public Optional<Film> addFilmFC(@Valid @RequestBody Film film) {
        log.info(InfoFilmControllerEnum.REQUEST_FILM_CONTROLLER_ADD_FILM.getInfo(film.toString()));
        return filmService.addFilmFS(film);
    }

    @DeleteMapping("/films")
    public void deleteFilmFC(@RequestBody long filmID) {
        log.info(InfoFilmControllerEnum.REQUEST_FILM_CONTROLLER_DELETE_FILM.getInfo(String.valueOf(filmID)));
        filmService.deleteFilmFS(filmID);
    }

    @PutMapping("/films")
    public Optional<Film> updateFilmFC(@Valid @RequestBody Film film) {
        log.info(InfoFilmControllerEnum.REQUEST_FILM_CONTROLLER_UPDATE_FILM.getInfo(film.toString()));
        return filmService.updateFilmFS(film.getId(), film);
    }

    @GetMapping("/films/{id}")
    public Optional<Film> getFilmFC(@PathVariable("id") long filmID) {
        log.info(InfoFilmControllerEnum.REQUEST_FILM_CONTROLLER_GET_FILM.getInfo(String.valueOf(filmID)));
        return filmService.getFilmFS(filmID);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public Optional<Film> putLikeFC(@PathVariable("id") long filmID, @PathVariable("userId") long userID) {
        log.info(InfoFilmControllerEnum.REQUEST_FILM_CONTROLLER_PUT_LIKE.getInfo(filmID + "/" + userID));
        return filmService.putLikeFS(filmID, userID);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public Optional<Film> deleteLikeFC(@PathVariable("id") long filmID, @PathVariable("userId") long userID) {
        log.info(InfoFilmControllerEnum.REQUEST_FILM_CONTROLLER_DELETE_LIKE.getInfo(filmID + "/" + userID));
        return filmService.deleteLikeFS(filmID, userID);
    }

    @GetMapping("/films/popular")
    public List<Film> getMostLikedFilmsFC(@RequestParam(
            defaultValue = DEFAULT_COUNT_OF_MOST_LIKED_FILMS) int count) {
        log.info(InfoFilmControllerEnum.REQUEST_FILM_CONTROLLER_GET_MOST_LIKED_FILMS.getInfo(String.valueOf(count)));
        return filmService.getMostLikedFilmsFS(count);
    }

    @GetMapping("/films")
    public List<Film> getFilmsFC() {
        log.info(InfoFilmControllerEnum.REQUEST_FILM_CONTROLLER_GET_FILMS.getMessage());
        return filmService.getFilmsFS();
    }

    @GetMapping("/genres")
    public List<Genre> getGenresFC() {
        log.info(InfoFilmControllerEnum.REQUEST_FILM_CONTROLLER_GET_GENRES.getMessage());
        return filmService.getGenresFS();
    }

    @GetMapping("/genres/{id}")
    public Genre getGenreFC(@PathVariable("id") int genreID) {
        log.info(InfoFilmControllerEnum.REQUEST_FILM_CONTROLLER_GET_GENRE.getInfo(String.valueOf(genreID)));
        return filmService.getGenreFS(genreID);
    }

    @GetMapping("/mpa")
    public List<MPA> getAllMpaFC() {
        log.info(InfoFilmControllerEnum.REQUEST_FILM_CONTROLLER_GET_ALL_MPA.getMessage());
        return filmService.getAllMpaFS();
    }

    @GetMapping("/mpa/{id}")
    public MPA getMpaFC(@PathVariable("id") int mpaID) {
        log.info(InfoFilmControllerEnum.REQUEST_FILM_CONTROLLER_GET_MPA.getInfo(String.valueOf(mpaID)));
        return filmService.getMpaFS(mpaID);
    }
}
