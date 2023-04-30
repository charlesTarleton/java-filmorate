package ru.yandex.practicum.filmorate.model;

import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@lombok.Data
public class Film {
    private Integer id;
    @NotNull
    @NotBlank
    private final String name;
    @NotNull
    @NotBlank
    private final String description;
    @NotNull
    private final LocalDate releaseDate;
    @NotNull
    private final Integer duration; // в техзадании не указан тип duration, но судя по тестам Postman это целочисленное
    private final Integer rate; // в техзадании это поле отсутствует, но оно есть в тестах Postman, поэтому добавил

    public static Film addFilm(Film film) {
        FilmService.films.put(film.id, film);
        return film;
    }

    public static Film updateFilm(Film film) {
        FilmService.films.put(film.id, film);
        return film;
    }

    public static List<Film> getAllFilms() {
        return new ArrayList<>(FilmService.films.values());
    }
}
