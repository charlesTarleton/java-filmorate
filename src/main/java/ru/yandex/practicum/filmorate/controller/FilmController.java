package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.logEnum.ErrorEnum;
import ru.yandex.practicum.filmorate.logEnum.InfoEnum;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.exception.IncorrectFilmExceptions.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService = new FilmService();
    private static final LocalDate BIRTHDAY_MOVIE = LocalDate.of(1895, 12, 28);
    private int filmsId = 1;

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info(InfoEnum.GET_NEW_FILM_ADD_REQUEST.getInfo(film.toString()));
        if (film.getId() != null) {
            log.error(ErrorEnum.FAIL_ID.getFilmError(film.getId(), film.getName(), film.getId()));
            throw new FilmWithIdAddException();
        }
        checkFilm(film);
        film.setId(filmsId++);
        log.info(InfoEnum.SUCCESS_ADD_FILM.getInfo(film.getName()));
        filmService.films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info(InfoEnum.GET_NEW_FILM_UPDATE_REQUEST.getInfo(film.toString()));
        if (!filmService.films.containsKey(film.getId())) {
            log.error(ErrorEnum.FAIL_ID.getFilmError(film.getId(), film.getName(), film.getId()));
            throw new FilmWithoutIdUpdateException();
        }
        checkFilm(film);
        log.info(InfoEnum.SUCCESS_UPDATE_FILM.getInfo(film.getName()));
        filmService.films.put(film.getId(), film);
        return film;
    }

    @GetMapping()
    public List<Film> getAllFilms() {
        log.info(InfoEnum.GET_NEW_FILM_GET_REQUEST.getMessage());
        return new ArrayList<>(filmService.films.values());
    }

    private void checkFilm(Film film) {
        log.info(InfoEnum.CHECK_FILM.getMessage());
        if (film.getDescription().length() > 200) {
            log.error(ErrorEnum.FAIL_FILM_DESC
                    .getFilmError(film.getId(), film.getName(), film.getDescription()));
            throw new FilmDescriptionException();
        } else if (film.getReleaseDate().isBefore(BIRTHDAY_MOVIE)) {
            log.error(ErrorEnum.FAIL_FILM_RELEASE_DATE
                    .getFilmError(film.getId(), film.getName(), film.getReleaseDate()));
            throw new FilmReleaseDateException();
        } else if (film.getDuration() <= 0) {
            log.error(ErrorEnum.FAIL_FILM_DURATION.getFilmError(film.getId(), film.getName(), film.getDuration()));
            throw new FilmDurationException();
        }
    }

    public void clearFilms() {
        log.info(InfoEnum.CLEAR_FILMS.getMessage());
        filmService.films.clear();
    }
}
