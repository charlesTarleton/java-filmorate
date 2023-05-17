package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.logEnum.FilmEnums.InfoFilmEnums.InfoFilmControllerEnum;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Component
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
    public Film addFilmFC(@Valid @RequestBody Film film) {
        log.info(InfoFilmControllerEnum.REQUEST_FILM_CONTROLLER_ADD_FILM.getInfo(film.toString()));
        return filmService.addFilmFS(film);
    }

    @DeleteMapping
    public void deleteFilmFC(@RequestBody String filmID) {
        log.info(InfoFilmControllerEnum.REQUEST_FILM_CONTROLLER_DELETE_FILM.getInfo(String.valueOf(filmID)));
        filmService.deleteFilmFS(filmID);
    }

    @PutMapping
    public Film updateFilmFC(@Valid @RequestBody Film film) {
        log.info(InfoFilmControllerEnum.REQUEST_FILM_CONTROLLER_UPDATE_FILM.getInfo(film.toString()));
        return filmService.updateFilmFS(film.getID(), film);
    }

    @GetMapping("/{id}")
    public Film getFilmFC(@PathVariable("id") String filmID) {
        log.info(InfoFilmControllerEnum.REQUEST_FILM_CONTROLLER_GET_FILM.getInfo(filmID));
        return filmService.getFilmFS(filmID);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film putLikeFC(@PathVariable("id") String filmID, @PathVariable("userId") String userID) {
        log.info(InfoFilmControllerEnum.REQUEST_FILM_CONTROLLER_PUT_LIKE.getInfo(filmID + "/" + userID));
        return filmService.putLikeFS(filmID, userID);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLikeFC(@PathVariable("id") String filmID, @PathVariable("userId") String userID) {
        log.info(InfoFilmControllerEnum.REQUEST_FILM_CONTROLLER_DELETE_LIKE.getInfo(filmID + "/" + userID));
        return filmService.deleteLikeFS(filmID, userID);
    }

    @GetMapping("/popular")
    public List<Film> getMostLikedFilmsFC(@RequestParam(
            defaultValue = DEFAULT_COUNT_OF_MOST_LIKED_FILMS) String count) {
        log.info(InfoFilmControllerEnum.REQUEST_FILM_CONTROLLER_GET_MOST_LIKED_FILMS.getInfo(count));
        return filmService.getMostLikedFilmsFS(Integer.parseInt(count));
    }

    @GetMapping
    public List<Film> getFilmsFC() {
        log.info(InfoFilmControllerEnum.REQUEST_FILM_CONTROLLER_GET_FILMS.getMessage());
        return filmService.getFilmsFS();
    }
}
