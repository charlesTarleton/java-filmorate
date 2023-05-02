package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@lombok.Data
public class User {
    private Integer id;
    @NotNull
    @Email
    @NotBlank
    private final String email;
    @NotNull
    @NotBlank
    private final String login;
    private String name;
    @NotNull
    private final LocalDate birthday;
}
