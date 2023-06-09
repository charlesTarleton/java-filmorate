package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.FilmorateObjectException;
import ru.yandex.practicum.filmorate.exception.FilmorateValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.filmFields.Genre;
import ru.yandex.practicum.filmorate.model.filmFields.MPA;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
    private final UserController userController;
    private final FilmController filmController;

    private final Film testFilm1 = new Film("На Мюнхен", "Комедия для всей семьи",
            LocalDate.of(2019, 4, 25), 95, 10, new MPA(1, "G"));
    private final Film testFilm2 = new Film("Красная перепёлка", "Шпионский триллер",
            LocalDate.of(2018, 3, 2), 140, 10, new MPA(2, "PG"));
    private final Film testFilm3 = new Film("Секретный документ",
            "Правда, о которой невозможно молчать",
            LocalDate.of(2022, 4, 1), 160, 5, new MPA(3, "PG-13"));

    private final User testUser1 = new User("email1@yandex.ru", "user1",
            LocalDate.of(1991, 1, 1));
    private final User testUser2 = new User("email2@yandex.ru", "user2",
            LocalDate.of(1992, 2, 2));
    private final User testUser3 = new User("email3@yandex.ru", "user3",
            LocalDate.of(1993, 3, 3));

    @Test
    public void shouldCreateFilm() {
        Set<Genre> test3Set = Set.of(new Genre(5, "Документальный"));
        testFilm3.setGenres(test3Set);
        Film testLocalFilm1 = filmController.addFilmFC(testFilm1).orElseThrow();
        Film testLocalFilm2 = filmController.addFilmFC(testFilm2).orElseThrow();
        Film testLocalFilm3 = filmController.addFilmFC(testFilm3).orElseThrow();
        List<Film> filmList = filmController.getFilmsFC();
        assertEquals(3, filmList.size());
        assertTrue(filmList.contains(testLocalFilm1));
        assertTrue(filmList.contains(testLocalFilm2));
        assertTrue(filmList.contains(testLocalFilm3));

        filmController.deleteFilmFC(testLocalFilm1.getId());
        filmController.deleteFilmFC(testLocalFilm2.getId());
        filmController.deleteFilmFC(testLocalFilm3.getId());
    }

    @Test
    public void shouldNotCreateFilmDescription() {
        Optional <Film> testFilm = filmController.addFilmFC(testFilm1);
        assertThrows(FilmorateValidationException.class,
                () -> filmController.addFilmFC(new Film("Непосредственно Геральт", "Этот фильм" +
                        " повествует о простом парне по имени Геральт, который жил обычной жизнью темерского" +
                        " школьника. Но однажды его засосало в какую-то дешевую японскую ММО РПГ и в этом мире" +
                        " он переродился в великого воина. И хотя его искусство в магии знаков было велико," +
                        " ему предстояло еще многому научится. Но мы верили, что Геральт спасет мир",
                        LocalDate.of(2020, 1, 1), 160, 4,
                        new MPA(1, "G"))));
        List<Film> filmList = filmController.getFilmsFC();
        assertEquals(1, filmList.size());
        assertTrue(filmList.contains(testFilm.orElseThrow()));
        filmController.deleteFilmFC(testFilm.orElseThrow().getId());
    }

    @Test
    public void shouldNotCreateFilmDate() {
        Optional<Film> testFilm = filmController.addFilmFC(testFilm2);
        assertThrows(FilmorateValidationException.class,
                () -> filmController.addFilmFC(new Film("Довакин меняет профессию",
                        "Приключение на 20 минут", LocalDate.of(170, 5, 5),
                        120, 6, new MPA (4, "R"))));
        List<Film> filmList = filmController.getFilmsFC();
        assertEquals(1, filmList.size());
        assertTrue(filmList.contains(testFilm.orElseThrow()));
        filmController.deleteFilmFC(testFilm.orElseThrow().getId());
    }

    @Test
    public void shouldNotCreateFilmDuration() {
        Optional <Film> testFilm = filmController.addFilmFC(testFilm3);
        assertThrows(FilmorateValidationException.class,
                () -> filmController.addFilmFC(new Film("Gravity Loops", "Детское, приключение",
                        LocalDate.of(2023, 5, 5), 0, 2,
                        new MPA(1, "G"))));
        assertThrows(FilmorateValidationException.class,
                () -> filmController.addFilmFC(new Film("Gravity Loops", "Детское, приключение",
                        LocalDate.of(2023, 5, 5), -100, 2,
                        new MPA(1, "G"))));
        List<Film> filmList = filmController.getFilmsFC();
        assertEquals(1, filmList.size());
        assertTrue(filmList.contains(testFilm.orElseThrow()));
        filmController.deleteFilmFC(testFilm.orElseThrow().getId());
    }

    @Test
    public void shouldUpdateFilm() {
        long id = filmController.addFilmFC(testFilm1).orElseThrow().getId();
        Optional <Film> testFilm = filmController.addFilmFC(testFilm2);
        Film localFilm = new Film("На Тоскану", "История, приключение, комедия",
                LocalDate.of(2019, 4, 25), 95, 10, new MPA(5, "NC-17"));
        localFilm.setId(id);
        filmController.updateFilmFC(localFilm);
        List<Film> filmList = filmController.getFilmsFC();
        assertEquals(2, filmList.size());
        assertTrue(filmList.contains(localFilm));
        assertTrue(filmList.contains(testFilm.orElseThrow()));
        filmController.deleteFilmFC(id);
        filmController.deleteFilmFC(testFilm.orElseThrow().getId());
    }

    @Test
    public void shouldNotUpdateFilmDescription() {
        Optional <Film> testFilm = filmController.addFilmFC(testFilm2);
        Film localFilm = new Film("Непосредственно Геральт", "Этот фильм,повествует о" +
                " простом парне по имени Геральт, который жил обычной жизнью темерского школьника. Но однажды" +
                " его засосало в какую-то дешевую японскую ММО РПГ и в этом мире он переродился в великого воина." +
                " И хотя его искусство в магии знаков было велико, ему предстояло еще многому научится." +
                "Но мы верили, что Геральт спасет мир",
                LocalDate.of(2020, 1, 1), 160, 4, new MPA(1, "G"));
        localFilm.setId(testFilm.orElseThrow().getId());
        assertThrows(FilmorateValidationException.class, () -> filmController.updateFilmFC(localFilm));
        List<Film> filmList = filmController.getFilmsFC();
        assertEquals(1, filmList.size());
        assertTrue(filmList.contains(testFilm.orElseThrow()));
        filmController.deleteFilmFC(testFilm.orElseThrow().getId());
    }

    @Test
    public void shouldNotUpdateFilmDate() {
        Optional <Film> testFilm = filmController.addFilmFC(testFilm1);
        Film localFilm = new Film("Довакин меняет профессию", "Комедия, ужасы",
                LocalDate.of(170, 5, 5), 120, 6, new MPA(1, "G"));
        localFilm.setId(testFilm.orElseThrow().getId());
        assertThrows(FilmorateValidationException.class, () -> filmController.updateFilmFC(localFilm));
        List<Film> filmList = filmController.getFilmsFC();
        assertEquals(1, filmList.size());
        assertTrue(filmList.contains(testFilm.orElseThrow()));
        filmController.deleteFilmFC(testFilm.orElseThrow().getId());
    }

    @Test
    public void shouldNotUpdateFilmDuration() {
        Optional <Film> testLocalFilm1 = filmController.addFilmFC(testFilm1);
        Optional <Film> testLocalFilm2 = filmController.addFilmFC(testFilm2);
        Film localFilm1 = new Film("Gravity Loops", "Детское, приключение",
                LocalDate.of(2023, 5, 5), 0, 2, new MPA(1, "G"));
        localFilm1.setId(testLocalFilm1.orElseThrow().getId());
        assertThrows(FilmorateValidationException.class, () -> filmController.updateFilmFC(localFilm1));
        Film localFilm2 = new Film("Gravity Loops", "Детское, приключение",
                LocalDate.of(2023, 5, 5), -100, 2, new MPA(1, "G"));
        localFilm2.setId(testLocalFilm2.orElseThrow().getId());
        assertThrows(FilmorateValidationException.class, () -> filmController.updateFilmFC(localFilm2));
        List<Film> filmList = filmController.getFilmsFC();
        assertEquals(2, filmList.size());
        assertTrue(filmList.contains(testLocalFilm1.orElseThrow()));
        assertTrue(filmList.contains(testLocalFilm2.orElseThrow()));
        filmController.deleteFilmFC(testLocalFilm1.orElseThrow().getId());
        filmController.deleteFilmFC(testLocalFilm2.orElseThrow().getId());
    }

    @Test
    public void shouldGetFilms() {
        Optional <Film> testLocalFilm1 = filmController.addFilmFC(testFilm1);
        Optional <Film> testLocalFilm2 = filmController.addFilmFC(testFilm3);
        List<Film> filmList = filmController.getFilmsFC();
        assertEquals(2, filmList.size());
        assertTrue(filmList.contains(testLocalFilm1.orElseThrow()));
        assertTrue(filmList.contains(testLocalFilm2.orElseThrow()));
        filmController.deleteFilmFC(testLocalFilm1.orElseThrow().getId());
        filmController.deleteFilmFC(testLocalFilm2.orElseThrow().getId());
    }

    @Test
    public void shouldDeleteFilm() {
        long id = filmController.addFilmFC(testFilm2).orElseThrow().getId();
        Optional <Film> testFilm = filmController.addFilmFC(testFilm3);
        filmController.deleteFilmFC(id);
        List<Film> filmList = filmController.getFilmsFC();
        assertEquals(1, filmList.size());
        assertTrue(filmList.contains(testFilm.orElseThrow()));
        filmController.deleteFilmFC(testFilm.orElseThrow().getId());
    }

    @Test
    public void shouldNotDeleteFilm() {
        Optional <Film> testLocalFilm1 = filmController.addFilmFC(testFilm1);
        Optional <Film> testLocalFilm2 = filmController.addFilmFC(testFilm2);
        assertThrows(FilmorateObjectException.class, () -> filmController.deleteFilmFC(4));
        List<Film> filmList = filmController.getFilmsFC();
        assertEquals(2, filmList.size());
        assertTrue(filmList.contains(testLocalFilm1.orElseThrow()));
        assertTrue(filmList.contains(testLocalFilm2.orElseThrow()));
        filmController.deleteFilmFC(testLocalFilm1.orElseThrow().getId());
        filmController.deleteFilmFC(testLocalFilm2.orElseThrow().getId());
    }

    @Test
    public void shouldGetFilm() {
        long id = filmController.addFilmFC(testFilm1).orElseThrow().getId();
        Optional <Film> testFilm = filmController.addFilmFC(testFilm3);
        assertEquals(testFilm, filmController.getFilmFC(testFilm.orElseThrow().getId()));
        filmController.deleteFilmFC(id);
        filmController.deleteFilmFC(testFilm.orElseThrow().getId());
    }

    @Test
    public void shouldNotGetFilm() {
        long id1 = filmController.addFilmFC(testFilm1).orElseThrow().getId();
        long id2 = filmController.addFilmFC(testFilm2).orElseThrow().getId();
        assertThrows(FilmorateObjectException.class, () -> filmController.getFilmFC(1000));
        filmController.deleteFilmFC(id1);
        filmController.deleteFilmFC(id2);
    }

    @Test
    public void shouldPutLike() {
        long idU = userController.addUserUC(testUser1).orElseThrow().getId();
        long idF1 = filmController.addFilmFC(testFilm1).orElseThrow().getId();
        long idF2 = filmController.addFilmFC(testFilm2).orElseThrow().getId();
        filmController.putLikeFC(idF2, idU);
        assertEquals(1, filmController.getFilmFC(idF2).orElseThrow().getLikes().size());
        assertTrue(filmController.getFilmFC(idF2).orElseThrow().getLikes().contains(idU));
        userController.deleteUserUC(idU);
        filmController.deleteFilmFC(idF1);
        filmController.deleteFilmFC(idF2);
    }

    @Test
    public void shouldNotPutLike() {
        long idF1 = filmController.addFilmFC(testFilm2).orElseThrow().getId();
        long idF2 = filmController.addFilmFC(testFilm1).orElseThrow().getId();
        assertThrows(FilmorateObjectException.class, () -> filmController.putLikeFC(idF1, 1000));
        long idU = userController.addUserUC(testUser2).orElseThrow().getId();
        assertThrows(FilmorateObjectException.class, () -> filmController.putLikeFC(1000, idU));
        filmController.deleteFilmFC(idF1);
        filmController.deleteFilmFC(idF2);
        userController.deleteUserUC(idU);
    }

    @Test
    public void shouldDeleteLike() {
        long idU = userController.addUserUC(testUser3).orElseThrow().getId();
        long idF1 = filmController.addFilmFC(testFilm1).orElseThrow().getId();
        long idF2 = filmController.addFilmFC(testFilm2).orElseThrow().getId();
        filmController.putLikeFC(idF2, idU);
        assertEquals(0, filmController.deleteLikeFC(idF2, idU).orElseThrow().getLikes().size());
        userController.deleteUserUC(idU);
        filmController.deleteFilmFC(idF1);
        filmController.deleteFilmFC(idF2);
    }

    @Test
    public void shouldNotDeleteLike() {
        long idU = userController.addUserUC(testUser1).orElseThrow().getId();
        long idF1 = filmController.addFilmFC(testFilm3).orElseThrow().getId();
        long idF2 = filmController.addFilmFC(testFilm1).orElseThrow().getId();
        filmController.putLikeFC(idF2, idU);
        assertThrows(FilmorateObjectException.class, () -> filmController.deleteLikeFC(1000, idU));
        assertThrows(FilmorateObjectException.class, () -> filmController.deleteLikeFC(idF2, 1000));
        userController.deleteUserUC(idU);
        filmController.deleteFilmFC(idF1);
        filmController.deleteFilmFC(idF2);
    }

    @Test
    public void shouldGetMostLikedFilms() {
        List<List<Long>> deleteIdList = forTestMostLikedFilmsAdd();
        assertEquals(4, filmController.getMostLikedFilmsFC(7).size());
        assertEquals("Довакин меняет профессию",
                filmController.getMostLikedFilmsFC(5).get(0).getName());
        assertEquals("Секретный документ",
                filmController.getMostLikedFilmsFC(6).get(1).getName());
        assertEquals("На Мюнхен",
                filmController.getMostLikedFilmsFC(7).get(2).getName());
        assertEquals("Красная перепёлка",
                filmController.getMostLikedFilmsFC(8).get(3).getName());
        assertEquals(2, filmController.getMostLikedFilmsFC(2).size());
        assertEquals("Довакин меняет профессию",
                filmController.getMostLikedFilmsFC(2).get(0).getName());
        assertEquals("Секретный документ",
                filmController.getMostLikedFilmsFC(2).get(1).getName());
        assertEquals(1, filmController.getMostLikedFilmsFC(1).size());
        assertEquals("Довакин меняет профессию",
                filmController.getMostLikedFilmsFC(1).get(0).getName());
        forTestMostLikedFilmsDelete(deleteIdList);
    }

    @Test
    public void shouldNotGetMostLikedFilms() {
        List<List<Long>> deleteIdList = forTestMostLikedFilmsAdd();
        assertThrows(FilmorateObjectException.class, () -> filmController.getMostLikedFilmsFC(0));
        assertThrows(FilmorateObjectException.class, () -> filmController.getMostLikedFilmsFC(-1));
        forTestMostLikedFilmsDelete(deleteIdList);
    }

    @Test
    public void shouldReturnAllGenres() {
        List<Genre> genreList = filmController.getGenresFC();
        assertTrue(genreList.contains(new Genre(1, "Комедия")));
        assertTrue(genreList.contains(new Genre(2, "Драма")));
        assertTrue(genreList.contains(new Genre(3, "Мультфильм")));
        assertTrue(genreList.contains(new Genre(4, "Триллер")));
        assertTrue(genreList.contains(new Genre(5, "Документальный")));
        assertTrue(genreList.contains(new Genre(6, "Боевик")));
    }

    @Test
    public void shouldReturnGenre() {
        assertEquals(new Genre(2, "Драма"), filmController.getGenreFC(2));
        assertEquals(new Genre(5, "Документальный"), filmController.getGenreFC(5));
    }

    @Test
    public void shouldNotReturnGenre() {
        assertThrows(FilmorateObjectException.class, () -> filmController.getGenreFC(8));
        assertThrows(FilmorateObjectException.class, () -> filmController.getGenreFC(0));
        assertThrows(FilmorateObjectException.class, () -> filmController.getGenreFC(-1));
    }

    @Test
    public void shouldReturnAllMPA() {
        List<MPA> mpaList = filmController.getAllMpaFC();
        assertTrue(mpaList.contains(new MPA(1, "G")));
        assertTrue(mpaList.contains(new MPA(2, "PG")));
        assertTrue(mpaList.contains(new MPA(3, "PG-13")));
        assertTrue(mpaList.contains(new MPA(4, "R")));
        assertTrue(mpaList.contains(new MPA(5, "NC-17")));
    }

    @Test
    public void shouldReturnMPA() {
        assertEquals(new MPA(1, "G"), filmController.getMpaFC(1));
        assertEquals(new MPA(4, "R"), filmController.getMpaFC(4));
    }

    @Test
    public void shouldNotReturnMPA() {
        assertThrows(FilmorateObjectException.class, () -> filmController.getMpaFC(6));
        assertThrows(FilmorateObjectException.class, () -> filmController.getMpaFC(0));
        assertThrows(FilmorateObjectException.class, () -> filmController.getMpaFC(-1));
    }

    private List<List<Long>> forTestMostLikedFilmsAdd() {
        List<List<Long>> deleteIdList = new ArrayList<>();
        List<Long> deleteFilmIDList = new ArrayList<>();
        deleteIdList.add(0, deleteFilmIDList);
        List<Long> deleteUserIDLIst = new ArrayList<>();
        deleteIdList.add(1, deleteUserIDLIst);
        long idU1 = userController.addUserUC(testUser1).orElseThrow().getId();
        deleteUserIDLIst.add(idU1);
        long idU2 = userController.addUserUC(testUser2).orElseThrow().getId();
        deleteUserIDLIst.add(idU2);
        long idU3 = userController.addUserUC(testUser3).orElseThrow().getId();
        deleteUserIDLIst.add(idU3);
        long idF1 = filmController.addFilmFC(testFilm1).orElseThrow().getId();
        deleteFilmIDList.add(idF1);
        long idF2 = filmController.addFilmFC(testFilm2).orElseThrow().getId();
        deleteFilmIDList.add(idF2);
        long idF3 = filmController.addFilmFC(testFilm3).orElseThrow().getId();
        deleteFilmIDList.add(idF3);
        long idF4 = filmController.addFilmFC(new Film("Довакин меняет профессию",
                "Комедия, ужасы", LocalDate.of(2017, 5, 5),
                120, 6, new MPA(2, "PG"))).orElseThrow().getId();
        deleteFilmIDList.add(idF4);
        filmController.putLikeFC(idF4, idU1);
        filmController.putLikeFC(idF4, idU2);
        filmController.putLikeFC(idF4, idU3);
        filmController.putLikeFC(idF3, idU2);
        filmController.putLikeFC(idF3, idU3);
        filmController.putLikeFC(idF1, idU3);
        return deleteIdList;
    }

    private void forTestMostLikedFilmsDelete(List<List<Long>> deleteIdList) {
        deleteIdList.get(0).forEach(filmController::deleteFilmFC);
        deleteIdList.get(1).forEach(userController::deleteUserUC);
    }

    @Test
    public void shouldCreateUser() {
        Optional<User> testLocalUser1 = userController.addUserUC(testUser1);
        Optional<User> testLocalUser2 = userController.addUserUC(testUser2);
        List<User> userList = userController.getUsersUC();
        assertEquals(2, userList.size());
        assertTrue(userList.contains(testLocalUser1.orElseThrow()));
        assertTrue(userList.contains(testLocalUser2.orElseThrow()));
        userController.deleteUserUC(testLocalUser1.orElseThrow().getId());
        userController.deleteUserUC(testLocalUser2.orElseThrow().getId());
    }

    @Test
    public void shouldNotCreateUserDate() {
        Optional<User> testUser = userController.addUserUC(testUser1);
        assertThrows(FilmorateValidationException.class,
                () -> userController.addUserUC(new User("user1@yandex.ru", "user1Login",
                        LocalDate.of(2024, 1, 1))));
        List<User> userList = userController.getUsersUC();
        assertEquals(1, userList.size());
        assertTrue(userList.contains(testUser.orElseThrow()));
        userController.deleteUserUC(testUser.orElseThrow().getId());
    }

    @Test
    public void shouldUpdateUser() {
        long idU = userController.addUserUC(testUser1).orElseThrow().getId();
        Optional<User> testUser = userController.addUserUC(testUser2);
        User updateUser = new User("newUser1@yandex.ru", "user1Login",
                LocalDate.of(1990, 1, 1));
        updateUser.setId(idU);
        userController.updateUserUC(updateUser);
        List<User> userList = userController.getUsersUC();
        assertEquals(2, userList.size());
        assertTrue(userList.contains(updateUser));
        assertTrue(userList.contains(testUser.orElseThrow()));
        userController.deleteUserUC(idU);
        userController.deleteUserUC(testUser.orElseThrow().getId());
    }

    @Test
    public void shouldNotUpdateUserDate() {
        Optional<User> testLocalUser1 = userController.addUserUC(testUser1);
        Optional<User> testLocalUser2 = userController.addUserUC(testUser2);
        User localUser = new User("user2@yandex.ru", "user2Login",
                LocalDate.of(2024, 1, 1));
        localUser.setId(testLocalUser2.orElseThrow().getId());
        assertThrows(FilmorateValidationException.class, () -> userController.updateUserUC(localUser));
        List<User> userList = userController.getUsersUC();
        assertEquals(2, userList.size());
        assertTrue(userList.contains(testLocalUser1.orElseThrow()));
        assertTrue(userList.contains(testLocalUser2.orElseThrow()));
        userController.deleteUserUC(testLocalUser1.orElseThrow().getId());
        userController.deleteUserUC(testLocalUser2.orElseThrow().getId());
    }

    @Test
    public void shouldGetUsers() {
        Optional<User> testLocalUser1 = userController.addUserUC(testUser1);
        Optional<User> testLocalUser2 = userController.addUserUC(testUser2);
        List<User> userList = userController.getUsersUC();
        assertEquals(2, userList.size());
        assertTrue(userList.contains(testLocalUser1.orElseThrow()));
        assertTrue(userList.contains(testLocalUser2.orElseThrow()));
        userController.deleteUserUC(testLocalUser1.orElseThrow().getId());
        userController.deleteUserUC(testLocalUser2.orElseThrow().getId());
    }

    @Test
    public void shouldDeleteUser() {
        long id = userController.addUserUC(testUser1).orElseThrow().getId();
        Optional<User> testUser = userController.addUserUC(testUser2);
        userController.deleteUserUC(id);
        List<User> userList = userController.getUsersUC();
        assertEquals(1, userList.size());
        assertTrue(userList.contains(testUser.orElseThrow()));
        userController.deleteUserUC(testUser.orElseThrow().getId());
    }

    @Test
    public void shouldNotDeleteUser() {
        Optional<User> testLocalUser1 = userController.addUserUC(testUser1);
        Optional<User> testLocalUser2 = userController.addUserUC(testUser2);
        assertThrows(FilmorateObjectException.class, () -> userController.deleteUserUC(1000));
        List<User> userList = userController.getUsersUC();
        assertEquals(2, userList.size());
        assertTrue(userList.contains(testLocalUser1.orElseThrow()));
        assertTrue(userList.contains(testLocalUser2.orElseThrow()));
        userController.deleteUserUC(testLocalUser1.orElseThrow().getId());
        userController.deleteUserUC(testLocalUser2.orElseThrow().getId());
    }

    @Test
    public void shouldGetUser() {
        Optional<User> testLocalUser1 = userController.addUserUC(testUser1);
        Optional<User> testLocalUser2 = userController.addUserUC(testUser2);
        assertEquals(testLocalUser1.orElseThrow(),
                userController.getUserUC(testLocalUser1.orElseThrow().getId()).orElseThrow());
        assertEquals(testLocalUser2.orElseThrow(),
                userController.getUserUC(testLocalUser2.orElseThrow().getId()).orElseThrow());
        userController.deleteUserUC(testLocalUser1.orElseThrow().getId());
        userController.deleteUserUC(testLocalUser2.orElseThrow().getId());
    }

    @Test
    public void shouldNotGetUser() {
        Optional<User> testLocalUser1 = userController.addUserUC(testUser1);
        Optional<User> testLocalUser2 = userController.addUserUC(testUser2);
        assertThrows(FilmorateObjectException.class, () -> userController.getUserUC(1000));
        userController.deleteUserUC(testLocalUser1.orElseThrow().getId());
        userController.deleteUserUC(testLocalUser2.orElseThrow().getId());
    }

    @Test
    public void shouldAddFriend() {
        Optional<User> testLocalUser1 = userController.addUserUC(testUser1);
        Optional<User> testLocalUser2 = userController.addUserUC(testUser2);
        userController.addFriendUC(testLocalUser1.orElseThrow().getId(), testLocalUser2.orElseThrow().getId());
        assertEquals(1, userController.getUserUC(testLocalUser1.orElseThrow().getId()).orElseThrow()
                .getUserFriends().size());
        assertTrue(userController.getUserUC(testLocalUser1.orElseThrow().getId()).orElseThrow()
                .getUserFriends().containsKey(testLocalUser2.orElseThrow().getId()));
        assertEquals(0, userController.getUserUC(testLocalUser2.orElseThrow().getId()).orElseThrow()
                .getUserFriends().size());
        userController.deleteUserUC(testLocalUser1.orElseThrow().getId());
        userController.deleteUserUC(testLocalUser2.orElseThrow().getId());
    }

    @Test
    public void shouldNotAddFriend() {
        long idU1 = userController.addUserUC(testUser1).orElseThrow().getId();
        long idU2 = userController.addUserUC(testUser2).orElseThrow().getId();
        assertThrows(FilmorateObjectException.class, () -> userController.addFriendUC(1000, idU2));
        assertEquals(0, userController.getUserUC(idU1).orElseThrow().getUserFriends().size());
        assertEquals(0, userController.getUserUC(idU2).orElseThrow().getUserFriends().size());
        userController.deleteUserUC(idU1);
        userController.deleteUserUC(idU2);
    }

    @Test
    public void shouldDeleteFriend() {
        long idU1 = userController.addUserUC(testUser1).orElseThrow().getId();
        long idU2 = userController.addUserUC(testUser2).orElseThrow().getId();
        userController.addFriendUC(idU1, idU2);
        assertEquals(1, userController.getUserUC(idU1).orElseThrow().getUserFriends().size());
        assertTrue(userController.getUserUC(idU1).orElseThrow().getUserFriends().containsKey(idU2));
        userController.deleteFriendUC(idU1, idU2);
        assertEquals(0, userController.getUserUC(idU1).orElseThrow().getUserFriends().size());
        assertEquals(0, userController.getUserUC(idU2).orElseThrow().getUserFriends().size());
        userController.deleteUserUC(idU1);
        userController.deleteUserUC(idU2);
    }

    @Test
    public void shouldNotDeleteFriend() {
        long idU1 = userController.addUserUC(testUser1).orElseThrow().getId();
        long idU2 = userController.addUserUC(testUser2).orElseThrow().getId();
        userController.addFriendUC(idU1, idU2);
        assertThrows(FilmorateObjectException.class, () -> userController.deleteFriendUC(1000, idU2));
        assertThrows(FilmorateObjectException.class, () -> userController.deleteFriendUC(idU1, 1000));
        assertEquals(1, userController.getUserUC(idU1).orElseThrow().getUserFriends().size());
        assertTrue(userController.getUserUC(idU1).orElseThrow().getUserFriends().containsKey(idU2));
        userController.deleteUserUC(idU1);
        userController.deleteUserUC(idU2);
    }

    @Test
    public void shouldGetFriends() {
        Optional<User> testLocalUser1 = userController.addUserUC(testUser1);
        Optional<User> testLocalUser2 = userController.addUserUC(testUser2);
        userController.addFriendUC(testLocalUser1.orElseThrow().getId(), testLocalUser2.orElseThrow().getId());
        List<User> friendsList = userController.getFriendsUC(testLocalUser1.orElseThrow().getId());
        assertEquals(1, friendsList.size());
        assertEquals(friendsList.get(0), testLocalUser2.orElseThrow());
        userController.deleteUserUC(testLocalUser1.orElseThrow().getId());
        userController.deleteUserUC(testLocalUser2.orElseThrow().getId());
    }

    @Test
    public void shouldNotGetFriends() {
        long idU1 = userController.addUserUC(testUser1).orElseThrow().getId();
        long idU2 = userController.addUserUC(testUser2).orElseThrow().getId();
        userController.addFriendUC(idU1, idU2);
        assertThrows(FilmorateObjectException.class, () -> userController.getFriendsUC(1000));
        userController.deleteUserUC(idU1);
        userController.deleteUserUC(idU2);
    }

    @Test
    public void shouldGetCommonFriends() {
        long idU1 = userController.addUserUC(testUser1).orElseThrow().getId();
        Optional<User> testLocalUser2 = userController.addUserUC(testUser2);
        long idU3 = userController.addUserUC(testUser3).orElseThrow().getId();
        long idU4 = userController.addUserUC(new User("user4@yandex.ru", "user4Login",
                LocalDate.of(1980, 1, 1))).orElseThrow().getId();
        userController.addFriendUC(idU1, testLocalUser2.orElseThrow().getId());
        userController.addFriendUC(idU3, testLocalUser2.orElseThrow().getId());
        userController.addFriendUC(idU3, idU4);
        List<User> commonFriendsList = userController.getCommonFriendsUC(idU1, idU3);
        assertEquals(1, commonFriendsList.size());
        assertTrue(commonFriendsList.contains(testLocalUser2.orElseThrow()));
        userController.deleteUserUC(idU1);
        userController.deleteUserUC(testLocalUser2.orElseThrow().getId());
        userController.deleteUserUC(idU3);
        userController.deleteUserUC(idU4);
    }

    @Test
    public void shouldNotGetCommonFriends() {
        long idU1 = userController.addUserUC(testUser1).orElseThrow().getId();
        long idU2 = userController.addUserUC(testUser2).orElseThrow().getId();
        long idU3 = userController.addUserUC(testUser3).orElseThrow().getId();
        long idU4 = userController.addUserUC(new User("user4@yandex.ru", "user4Login",
                LocalDate.of(1980, 1, 1))).orElseThrow().getId();
        userController.addFriendUC(idU1, idU2);
        userController.addFriendUC(idU3, idU2);
        userController.addFriendUC(idU3, idU4);
        assertThrows(FilmorateObjectException.class, () -> userController.getCommonFriendsUC(1000, idU3));
        userController.deleteUserUC(idU1);
        userController.deleteUserUC(idU2);
        userController.deleteUserUC(idU3);
        userController.deleteUserUC(idU4);
    }
}