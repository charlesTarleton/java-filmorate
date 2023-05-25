package ru.yandex.practicum.filmorate.model.userModel;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@lombok.Data
public class User {
    private Long id;
    @NotBlank
    @Email
    private final String email;
    @NotBlank
    @Pattern(regexp = "\\S+")
    private final String login;
    private String name;
    @NotNull
    private final LocalDate birthday;
    private final Set<Long> userFriends = new HashSet<>();
    private final HashMap<Long, Boolean> friendshipStatuses = new HashMap<>();
}
