package ru.yandex.practicum.filmorate.service.genreService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.logEnum.genreEnums.InfoGenreEnums.InfoGenreServiceEnum;
import ru.yandex.practicum.filmorate.model.filmFields.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Slf4j
@Service
public class GenreService {
    private final GenreStorage genreStorage;

    @Autowired
    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public List<Genre> getGenresGS() {
        log.info(InfoGenreServiceEnum.GENRE_SERVICE_GET_GENRES.getMessage());
        return genreStorage.getGenres();
    }

    public Genre getGenreGS(int genreID) {
        log.info(InfoGenreServiceEnum.GENRE_SERVICE_GET_GENRE.getInfo(String.valueOf(genreID)));
        return genreStorage.getGenre(genreID);
    }
}
