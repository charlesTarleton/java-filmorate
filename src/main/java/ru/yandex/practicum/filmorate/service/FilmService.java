package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;

@Getter
public class FilmService {
    private final Map<Integer, Film> films = new HashMap<>();

    public void add(int id, Film film) {
        films.put(id, film);
    }

    public boolean filmsContains(int id) {
        return films.containsKey(id);
    }
}
