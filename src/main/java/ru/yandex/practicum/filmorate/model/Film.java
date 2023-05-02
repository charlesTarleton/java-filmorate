package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

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
}
