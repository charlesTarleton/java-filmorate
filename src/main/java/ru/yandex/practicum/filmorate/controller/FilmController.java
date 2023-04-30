package ru.yandex.practicum.filmorate.controller;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectFilmException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController @Slf4j @RequestMapping("/films")
public class FilmController {
    public static int filmsId = 1;

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        try {
            if (film.getId() != null) {
                throw new RuntimeException("Попытка добавить ранее добавленный фильм");
            }
            film.setId(filmsId++);
            checkFilm(film);
        } catch (RuntimeException e) {
            filmsId--;
            log.error(e.getMessage());
            throw new IncorrectFilmException();
        }
        log.info("Успешное добавление фильма {}", film);
        return Film.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        try {
            if (!FilmService.films.containsKey(film.getId())) {
                throw new RuntimeException("FilmId=" + film.getId() + ". Попытка обновить не добавленный фильм");
            }
            checkFilm(film);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            throw new IncorrectFilmException();
        }
        log.info("Успешное обновление фильма с Id {} на {}", film.getId(), film);
        return Film.updateFilm(film);
    }

    @GetMapping()
    public List<Film> getAllFilms() {
        return Film.getAllFilms();
    }

    private void checkFilm(Film film) {
        if (film.getDescription().length() > 200) {
            throw new RuntimeException("FilmId=" + film.getId() +
                    ". Значение длинны описания фильма равно \"" + film.getDescription().length() + "\"");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new RuntimeException("FilmId=" + film.getId() +
                    ". Значение даты выпуска фильма равно \"" + film.getReleaseDate() + "\"");
        } else if (film.getDuration() <= 0) {
            throw new RuntimeException("FilmId=" + film.getId() +
                    ". Значение длительности фильма равно \"" + film.getDuration() + "\"");
        }
    }
}
