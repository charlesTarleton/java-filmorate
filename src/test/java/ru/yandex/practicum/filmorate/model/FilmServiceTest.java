package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.IncorrectFilmExceptions.*;
import ru.yandex.practicum.filmorate.exception.IncorrectUserExceptions.UserWithoutIDException;
import ru.yandex.practicum.filmorate.inMemoryStorage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.inMemoryStorage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FilmServiceTest {
    private FilmController filmController;
    UserStorage userStorage;

    @BeforeEach
    public void createUserController() {
        userStorage = new InMemoryUserStorage();
        FilmStorage filmStorage = new InMemoryFilmStorage();
        FilmService filmService = new FilmService(filmStorage, userStorage);
        filmController = new FilmController(filmService);
    }

    @Test
    void shouldCreateFilm() {
        Film testFilm1 = filmController.addFilmFC(new Film("На Мюнхен", "История, приключение, комедия",
                LocalDate.of(2019,4, 25), 95, 10));

        Film testFilm2 = filmController.addFilmFC(new Film("Красная перепёлка", "Шпионский триллер",
                LocalDate.of(2018,3, 2), 140, 10));

        Film testFilm3 = filmController.addFilmFC(new Film("Секретный документ",
                "Триллер, документальный фильм", LocalDate.of(2022, 4, 1),
                160, 5));

        List<Film> filmList = filmController.getFilmsFC();
        assertEquals(3, filmList.size());
        assertTrue(filmList.contains(testFilm1));
        assertTrue(filmList.contains(testFilm2));
        assertTrue(filmList.contains(testFilm3));
    }

    @Test
    void shouldNotCreateFilmDescription() {
        Film testFilm = filmController.addFilmFC(new Film("На Мюнхен", "История, приключение, комедия",
                LocalDate.of(2019,4, 25), 95, 10));

        assertThrows(FilmDescriptionException.class,
                () -> filmController.addFilmFC(new Film("Непосредственно Геральт", "Этот фильм" +
                        " повествует о простом парне по имени Геральт, который жил обычной жизнью темерского" +
                        " школьника. Но однажды его засосало в какую-то дешевую японскую ММО РПГ и в этом мире" +
                        " он переродился в великого воина. И хотя его искусство в магии знаков было велико," +
                        " ему предстояло еще многому научится. Но мы верили, что Геральт спасет мир",
                LocalDate.of(2020, 1, 1), 160, 4)));

        List<Film> filmList = filmController.getFilmsFC();
        assertEquals(1, filmList.size());
        assertTrue(filmList.contains(testFilm));
    }

    @Test
    void shouldNotCreateFilmDate() {
        Film testFilm = filmController.addFilmFC(new Film("Красная перепёлка", "Шпионский триллер",
                LocalDate.of(2018,3, 2), 140, 10));

        assertThrows(FilmReleaseDateException.class,
                () -> filmController.addFilmFC(new Film("Довакин меняет профессию", "Комедия, ужасы",
                                LocalDate.of(170, 5, 5), 120, 6)));

        List<Film> filmList = filmController.getFilmsFC();
        assertEquals(1, filmList.size());
        assertTrue(filmList.contains(testFilm));
    }

    @Test
    void shouldNotCreateFilmDuration() {
        Film testFilm = filmController.addFilmFC(new Film("На Мюнхен", "История, приключение, комедия",
                LocalDate.of(2019,4, 25), 95, 10));

        assertThrows(FilmDurationException.class,
                () -> filmController.addFilmFC(new Film("Gravity Loops", "Детское, приключение",
                LocalDate.of(2023, 5, 5), 0, 2)));
        assertThrows(FilmDurationException.class,
                () -> filmController.addFilmFC(new Film("Gravity Loops", "Детское, приключение",
                LocalDate.of(2023, 5, 5), -100, 2)));

        List<Film> filmList = filmController.getFilmsFC();
        assertEquals(1, filmList.size());
        assertTrue(filmList.contains(testFilm));
    }

    @Test
    void shouldUpdateFilm() {
        filmController.addFilmFC(new Film("На Мюнхен", "История, приключение, комедия",
                LocalDate.of(2019,4, 25), 95, 10));

        Film testFilm = filmController.addFilmFC(new Film("Красная перепёлка", "Шпионский триллер",
                LocalDate.of(2018,3, 2), 140, 10));

        Film localFilm = new Film("На Тоскану", "История, приключение, комедия",
                LocalDate.of(2019,4, 25), 95, 10);
        localFilm.setId(1);
        filmController.updateFilmFC(localFilm);

        List<Film> filmList = filmController.getFilmsFC();
        assertEquals(2, filmList.size());
        assertTrue(filmList.contains(localFilm));
        assertTrue(filmList.contains(testFilm));
    }

    @Test
    void shouldNotUpdateFilmDescription() {
        Film testFilm = filmController.addFilmFC(new Film("Красная перепёлка", "Шпионский триллер",
                LocalDate.of(2018,3, 2), 140, 10));

        Film localFilm = new Film("Непосредственно Геральт", "Этот фильм,повествует о" +
                " простом парне по имени Геральт, который жил обычной жизнью темерского школьника. Но однажды" +
                " его засосало в какую-то дешевую японскую ММО РПГ и в этом мире он переродился в великого воина." +
                " И хотя его искусство в магии знаков было велико, ему предстояло еще многому научится." +
                "Но мы верили, что Геральт спасет мир",
                LocalDate.of(2020, 1, 1), 160, 4);
        localFilm.setId(1);
        assertThrows(FilmDescriptionException.class, () -> filmController.updateFilmFC(localFilm));

        List<Film> filmList = filmController.getFilmsFC();
        assertEquals(1, filmList.size());
        assertTrue(filmList.contains(testFilm));
    }

    @Test
    void shouldNotUpdateFilmDate() {
        Film testFilm = filmController.addFilmFC(new Film("На Мюнхен", "История, приключение, комедия",
                LocalDate.of(2019,4, 25), 95, 10));

        Film localFilm = new Film("Довакин меняет профессию", "Комедия, ужасы",
                LocalDate.of(170, 5, 5), 120, 6);
        localFilm.setId(1);
        assertThrows(FilmReleaseDateException.class, () -> filmController.updateFilmFC(localFilm));

        List<Film> filmList = filmController.getFilmsFC();
        assertEquals(1, filmList.size());
        assertTrue(filmList.contains(testFilm));
    }

    @Test
    void shouldNotUpdateFilmDuration() {
        Film testFilm1 = filmController.addFilmFC(new Film("На Мюнхен", "История, приключение, комедия",
                LocalDate.of(2019,4, 25), 95, 10));

        Film testFilm2 = filmController.addFilmFC(new Film("Красная перепёлка", "Шпионский триллер",
                LocalDate.of(2018,3, 2), 140, 10));

        Film localFilm1 = new Film("Gravity Loops", "Детское, приключение",
                LocalDate.of(2023, 5, 5), 0, 2);
        localFilm1.setId(1);
        assertThrows(FilmDurationException.class, () -> filmController.updateFilmFC(localFilm1));

        Film localFilm2 = new Film("Gravity Loops", "Детское, приключение",
                LocalDate.of(2023, 5, 5), -100, 2);
        localFilm2.setId(2);
        assertThrows(FilmDurationException.class, () -> filmController.updateFilmFC(localFilm2));

        List<Film> filmList = filmController.getFilmsFC();
        assertEquals(2, filmList.size());
        assertTrue(filmList.contains(testFilm1));
        assertTrue(filmList.contains(testFilm2));
    }

    @Test
    void shouldGetFilms() {
        Film testFilm1 = filmController.addFilmFC(new Film("На Мюнхен", "История, приключение, комедия",
                LocalDate.of(2019,4, 25), 95, 10));

        Film testFilm2 = filmController.addFilmFC(new Film("Красная перепёлка", "Шпионский триллер",
                LocalDate.of(2018,3, 2), 140, 10));

        List<Film> filmList = filmController.getFilmsFC();
        assertEquals(2, filmList.size());
        assertTrue(filmList.contains(testFilm1));
        assertTrue(filmList.contains(testFilm2));
    }

    @Test
    void shouldDeleteFilm() {
        filmController.addFilmFC(new Film("На Мюнхен", "История," +
                " приключение, комедия", LocalDate.of(2019,4, 25), 95, 10));

        Film testFilm2 = filmController.addFilmFC(new Film("Красная перепёлка", "Шпионский триллер",
                LocalDate.of(2018,3, 2), 140, 10));

        filmController.deleteFilmFC("1");
        List<Film> filmList = filmController.getFilmsFC();
        assertEquals(1, filmList.size());
        assertTrue(filmList.contains(testFilm2));
    }

    @Test
    void shouldNotDeleteFilm() {
        Film testFilm1 = filmController.addFilmFC(new Film("На Мюнхен", "История," +
                " приключение, комедия", LocalDate.of(2019,4, 25), 95, 10));

        Film testFilm2 = filmController.addFilmFC(new Film("Красная перепёлка", "Шпионский триллер",
                LocalDate.of(2018,3, 2), 140, 10));

        assertThrows(FilmWithoutIDException.class, () -> filmController.deleteFilmFC("4"));
        List<Film> filmList = filmController.getFilmsFC();
        assertEquals(2, filmList.size());
        assertTrue(filmList.contains(testFilm1));
        assertTrue(filmList.contains(testFilm2));

        assertThrows(FilmWithoutIDException.class, () -> filmController.deleteFilmFC(null));
        filmList = filmController.getFilmsFC();
        assertEquals(2, filmList.size());
        assertTrue(filmList.contains(testFilm1));
        assertTrue(filmList.contains(testFilm2));
    }

    @Test
    void shouldGetFilm() {
        filmController.addFilmFC(new Film("На Мюнхен", "История," +
                " приключение, комедия", LocalDate.of(2019,4, 25), 95, 10));

        Film testFilm2 = filmController.addFilmFC(new Film("Красная перепёлка", "Шпионский триллер",
                LocalDate.of(2018,3, 2), 140, 10));
        testFilm2.setId(2);

        assertEquals(testFilm2, filmController.getFilmFC("2"));
    }

    @Test
    void shouldNotGetFilm() {
        filmController.addFilmFC(new Film("На Мюнхен", "История," +
                " приключение, комедия", LocalDate.of(2019,4, 25), 95, 10));

        filmController.addFilmFC(new Film("Красная перепёлка", "Шпионский триллер",
                LocalDate.of(2018,3, 2), 140, 10));

        assertThrows(FilmWithoutIDException.class, () -> filmController.getFilmFC("5"));

        assertThrows(FilmWithoutIDException.class, () -> filmController.getFilmFC(null));
    }

    @Test
    void shouldPutLike() {
        userStorage.addUser(new User("email@yandex.ru", "user1",
                LocalDate.of(1990, 1, 1)));

        filmController.addFilmFC(new Film("На Мюнхен", "История," +
                " приключение, комедия", LocalDate.of(2019,4, 25), 95, 10));

        filmController.addFilmFC(new Film("Красная перепёлка", "Шпионский триллер",
                LocalDate.of(2018,3, 2), 140, 10));
        filmController.putLikeFC("2", "1");

        assertEquals(1, filmController.getFilmFC("2").getLikes().size());
        assertTrue(filmController.getFilmFC("2").getLikes().contains(1));
    }

    @Test
    void shouldNotPutLike() {
        filmController.addFilmFC(new Film("На Мюнхен", "История," +
                " приключение, комедия", LocalDate.of(2019,4, 25), 95, 10));

        filmController.addFilmFC(new Film("Красная перепёлка", "Шпионский триллер",
                LocalDate.of(2018,3, 2), 140, 10));

        assertThrows(UserWithoutIDException.class, () -> filmController.putLikeFC("1", "1"));

        userStorage.addUser(new User("email@yandex.ru", "user1",
                LocalDate.of(1990, 1, 1)));

        assertThrows(FilmWithoutIDException.class, () -> filmController.putLikeFC("5", "1"));
        assertThrows(FilmWithoutIDException.class, () -> filmController.putLikeFC(null, "1"));
    }

    @Test
    void shouldDeleteLike() {
        userStorage.addUser(new User("email@yandex.ru", "user1",
                LocalDate.of(1990, 1, 1)));

        filmController.addFilmFC(new Film("На Мюнхен", "История," +
                " приключение, комедия", LocalDate.of(2019,4, 25), 95, 10));

        filmController.addFilmFC(new Film("Красная перепёлка", "Шпионский триллер",
                LocalDate.of(2018,3, 2), 140, 10));

        filmController.putLikeFC("2", "1");
        assertEquals(0, filmController.deleteLikeFC("2", "1").getLikes().size());
    }

    @Test
    void shouldNotDeleteLike() {
        userStorage.addUser(new User("email@yandex.ru", "user1",
                LocalDate.of(1990, 1, 1)));

        filmController.addFilmFC(new Film("На Мюнхен", "История," +
                " приключение, комедия", LocalDate.of(2019,4, 25), 95, 10));

        filmController.addFilmFC(new Film("Красная перепёлка", "Шпионский триллер",
                LocalDate.of(2018,3, 2), 140, 10));

        assertThrows(FilmWithoutIDException.class, () -> filmController.deleteLikeFC("5", "1"));
        assertThrows(FilmWithoutIDException.class, () -> filmController.deleteLikeFC(null, "1"));
    }

    @Test
    void shouldGetMostLikedFilms() {
        forTestMostLikedFilms();

        assertEquals(4, filmController.getMostLikedFilmsFC("7").size());
        assertEquals("Довакин меняет профессию",
                filmController.getMostLikedFilmsFC("5").get(0).getName());
        assertEquals("Gravity Loops",
                filmController.getMostLikedFilmsFC("6").get(1).getName());
        assertEquals("На Мюнхен",
                filmController.getMostLikedFilmsFC("7").get(2).getName());
        assertEquals("Красная перепёлка",
                filmController.getMostLikedFilmsFC("8").get(3).getName());

        assertEquals(2, filmController.getMostLikedFilmsFC("2").size());
        assertEquals("Довакин меняет профессию",
                filmController.getMostLikedFilmsFC("2").get(0).getName());
        assertEquals("Gravity Loops",
                filmController.getMostLikedFilmsFC("2").get(1).getName());

        assertEquals(1, filmController.getMostLikedFilmsFC("1").size());
        assertEquals("Довакин меняет профессию",
                filmController.getMostLikedFilmsFC("1").get(0).getName());
    }

    @Test
    void shouldNotGetMostLikedFilms() {
        forTestMostLikedFilms();

        assertThrows(FilmIncorrectCountException.class, () -> filmController.getMostLikedFilmsFC("0"));
        assertThrows(FilmIncorrectCountException.class, () -> filmController.getMostLikedFilmsFC("-1"));
    }

    private void forTestMostLikedFilms() {
        userStorage.addUser(new User("email1@yandex.ru", "user1",
                LocalDate.of(1990, 1, 1)));

        userStorage.addUser(new User("email2@yandex.ru", "user2",
                LocalDate.of(1990, 1, 1)));

        userStorage.addUser(new User("email3@yandex.ru", "user3",
                LocalDate.of(1990, 1, 1)));

        filmController.addFilmFC(new Film("На Мюнхен", "История," +
                " приключение, комедия", LocalDate.of(2019,4, 25), 95, 10));

        filmController.addFilmFC(new Film("Красная перепёлка", "Шпионский триллер",
                LocalDate.of(2018,3, 2), 140, 10));

        filmController.addFilmFC(new Film("Gravity Loops", "Детское, приключение",
                LocalDate.of(2023, 5, 5), 50, 2));

        filmController.addFilmFC(new Film("Довакин меняет профессию", "Комедия, ужасы",
                LocalDate.of(2017, 5, 5), 120, 6));

        filmController.putLikeFC("4","1");
        filmController.putLikeFC("4","2");
        filmController.putLikeFC("4","3");
        filmController.putLikeFC("3","2");
        filmController.putLikeFC("3","3");
        filmController.putLikeFC("1","3");
    }
}