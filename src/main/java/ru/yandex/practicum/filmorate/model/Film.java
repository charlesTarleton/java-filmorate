package ru.yandex.practicum.filmorate.model;

import ru.yandex.practicum.filmorate.model.filmFields.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@lombok.Data
public class Film {
    private Long id;
    @NotBlank
    private final String name;
    private final String description;
    @NotNull
    private final LocalDate releaseDate;
    @NotNull
    private final Integer duration;
    private final Integer rate;
    private Set<Genre> genres = new HashSet<>();
    private final MPA mpa;
}
