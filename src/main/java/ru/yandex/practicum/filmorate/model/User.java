package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@lombok.Data
public class User {
    private Long id;
    @NotBlank
    @Pattern(regexp = "\\S+")
    @Email
    private final String email;
    @NotBlank
    @Pattern(regexp = "\\S+")
    private final String login;
    private String name;
    @NotNull
    private final LocalDate birthday;
    private Set<Long> userFriends = new HashSet<>();
}
