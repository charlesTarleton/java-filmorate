package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.logEnum.filmEnums.InfoFilmEnums.InfoFilmControllerEnum;
import ru.yandex.practicum.filmorate.service.filmService.FilmService;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;
    private static final String DEFAULT_COUNT_OF_MOST_LIKED_FILMS = "10";

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Optional<Film> addFilmFC(@Valid @RequestBody Film film) {
        log.info(InfoFilmControllerEnum.REQUEST_FILM_CONTROLLER_ADD_FILM.getInfo(film.toString()));
        return filmService.addFilmFS(film);
    }

    @DeleteMapping
    public void deleteFilmFC(@RequestBody long filmID) {
        log.info(InfoFilmControllerEnum.REQUEST_FILM_CONTROLLER_DELETE_FILM.getInfo(String.valueOf(filmID)));
        filmService.deleteFilmFS(filmID);
    }

    @PutMapping
    public Optional<Film> updateFilmFC(@Valid @RequestBody Film film) {
        log.info(InfoFilmControllerEnum.REQUEST_FILM_CONTROLLER_UPDATE_FILM.getInfo(film.toString()));
        return filmService.updateFilmFS(film.getId(), film);
    }

    @GetMapping("/{id}")
    public Optional<Film> getFilmFC(@PathVariable("id") long filmID) {
        log.info(InfoFilmControllerEnum.REQUEST_FILM_CONTROLLER_GET_FILM.getInfo(String.valueOf(filmID)));
        return filmService.getFilmFS(filmID);
    }

    @PutMapping("/{id}/like/{userId}")
    public Optional<Film> putLikeFC(@PathVariable("id") long filmID, @PathVariable("userId") long userID) {
        log.info(InfoFilmControllerEnum.REQUEST_FILM_CONTROLLER_PUT_LIKE.getInfo(filmID + "/" + userID));
        return filmService.putLikeFS(filmID, userID);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Optional<Film> deleteLikeFC(@PathVariable("id") long filmID, @PathVariable("userId") long userID) {
        log.info(InfoFilmControllerEnum.REQUEST_FILM_CONTROLLER_DELETE_LIKE.getInfo(filmID + "/" + userID));
        return filmService.deleteLikeFS(filmID, userID);
    }

    @GetMapping("/popular")
    public List<Film> getMostLikedFilmsFC(@RequestParam(
            defaultValue = DEFAULT_COUNT_OF_MOST_LIKED_FILMS) int count) {
        log.info(InfoFilmControllerEnum.REQUEST_FILM_CONTROLLER_GET_MOST_LIKED_FILMS.getInfo(String.valueOf(count)));
        return filmService.getMostLikedFilmsFS(count);
    }

    @GetMapping
    public List<Film> getFilmsFC() {
        log.info(InfoFilmControllerEnum.REQUEST_FILM_CONTROLLER_GET_FILMS.getMessage());
        return filmService.getFilmsFS();
    }
}
