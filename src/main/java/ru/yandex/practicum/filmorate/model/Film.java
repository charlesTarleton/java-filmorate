package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@lombok.Data
public class Film {
    private Integer id;
    @NotBlank
    private final String name;
    private final String description;
    @NotNull
    private final LocalDate releaseDate;
    @NotNull
    private final Integer duration;
    private final Integer rate;
}
