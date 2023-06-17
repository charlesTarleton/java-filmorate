package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.logEnum.genreEnums.InfoGenreEnums.InfoGenreControllerEnum;
import ru.yandex.practicum.filmorate.model.filmFields.Genre;
import ru.yandex.practicum.filmorate.service.genreService.GenreService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public List<Genre> getGenresGC() {
        log.info(InfoGenreControllerEnum.REQUEST_GENRE_CONTROLLER_GET_GENRES.getMessage());
        return genreService.getGenresGS();
    }

    @GetMapping("/{id}")
    public Genre getGenreGC(@PathVariable("id") int genreID) {
        log.info(InfoGenreControllerEnum.REQUEST_GENRE_CONTROLLER_GET_GENRE.getInfo(String.valueOf(genreID)));
        return genreService.getGenreGS(genreID);
    }
}
