package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.IncorrectFilmExceptions.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FilmServiceTest {
    @Test
    void shouldCreateFilm() {
        FilmController filmController = new FilmController();

        Film testFilm1 = filmController.addFilm(new Film("На Мюнхен", "История, приключение, комедия",
                LocalDate.of(2019,4, 25), 95, 10));

        Film testFilm2 = filmController.addFilm(new Film("Красная перепёлка", "Шпионский триллер",
                LocalDate.of(2018,3, 2), 140, 10));

        Film testFilm3 = filmController.addFilm(new Film("Секретный документ",
                "Триллер, документальный фильм", LocalDate.of(2022, 4, 1),
                160, 5));

        List<Film> filmList = filmController.getAllFilms();
        assertEquals(3, filmList.size());
        assertTrue(filmList.contains(testFilm1));
        assertTrue(filmList.contains(testFilm2));
        assertTrue(filmList.contains(testFilm3));
    }

    @Test
    void shouldNotCreateFilmDescription() {
        FilmController filmController = new FilmController();

        Film testFilm = filmController.addFilm(new Film("На Мюнхен", "История, приключение, комедия",
                LocalDate.of(2019,4, 25), 95, 10));

        assertThrows(FilmDescriptionException.class,
                () -> filmController.addFilm(new Film("Непосредственно Геральт", "Этот фильм" +
                        " повествует о простом парне по имени Геральт, который жил обычной жизнью темерского" +
                        " школьника. Но однажды его засосало в какую-то дешевую японскую ММО РПГ и в этом мире" +
                        " он переродился в великого воина. И хотя его искусство в магии знаков было велико," +
                        " ему предстояло еще многому научится. Но мы верили, что Геральт спасет мир",
                LocalDate.of(2020, 1, 1), 160, 4)));

        List<Film> filmList = filmController.getAllFilms();
        assertEquals(1, filmList.size());
        assertTrue(filmList.contains(testFilm));
    }

    @Test
    void shouldNotCreateFilmDate() {
        FilmController filmController = new FilmController();

        Film testFilm = filmController.addFilm(new Film("Красная перепёлка", "Шпионский триллер",
                LocalDate.of(2018,3, 2), 140, 10));

        assertThrows(FilmReleaseDateException.class,
                () -> filmController.addFilm(new Film("Довакин меняет профессию", "Комедия, ужасы",
                                LocalDate.of(170, 5, 5), 120, 6)));

        List<Film> filmList = filmController.getAllFilms();
        assertEquals(1, filmList.size());
        assertTrue(filmList.contains(testFilm));
    }

    @Test
    void shouldNotCreateFilmDuration() {
        FilmController filmController = new FilmController();

        Film testFilm = filmController.addFilm(new Film("На Мюнхен", "История, приключение, комедия",
                LocalDate.of(2019,4, 25), 95, 10));

        assertThrows(FilmDurationException.class,
                () -> filmController.addFilm(new Film("Gravity Loops", "Детское, приключение",
                LocalDate.of(2023, 5, 5), 0, 2)));
        assertThrows(FilmDurationException.class,
                () -> filmController.addFilm(new Film("Gravity Loops", "Детское, приключение",
                LocalDate.of(2023, 5, 5), -100, 2)));

        List<Film> filmList = filmController.getAllFilms();
        assertEquals(1, filmList.size());
        assertTrue(filmList.contains(testFilm));
    }

    @Test
    void shouldUpdateFilm() {
        FilmController filmController = new FilmController();

        filmController.addFilm(new Film("На Мюнхен", "История, приключение, комедия",
                LocalDate.of(2019,4, 25), 95, 10));

        Film testFilm = filmController.addFilm(new Film("Красная перепёлка", "Шпионский триллер",
                LocalDate.of(2018,3, 2), 140, 10));

        Film localFilm = new Film("На Тоскану", "История, приключение, комедия",
                LocalDate.of(2019,4, 25), 95, 10);
        localFilm.setId(1);
        filmController.updateFilm(localFilm);

        List<Film> filmList = filmController.getAllFilms();
        assertEquals(2, filmList.size());
        assertTrue(filmList.contains(localFilm));
        assertTrue(filmList.contains(testFilm));
    }

    @Test
    void shouldNotUpdateFilmDescription() {
        FilmController filmController = new FilmController();

        Film testFilm = filmController.addFilm(new Film("Красная перепёлка", "Шпионский триллер",
                LocalDate.of(2018,3, 2), 140, 10));

        Film localFilm = new Film("Непосредственно Геральт", "Этот фильм,повествует о" +
                " простом парне по имени Геральт, который жил обычной жизнью темерского школьника. Но однажды" +
                " его засосало в какую-то дешевую японскую ММО РПГ и в этом мире он переродился в великого воина." +
                " И хотя его искусство в магии знаков было велико, ему предстояло еще многому научится." +
                "Но мы верили, что Геральт спасет мир",
                LocalDate.of(2020, 1, 1), 160, 4);
        localFilm.setId(1);
        assertThrows(FilmDescriptionException.class, () -> filmController.updateFilm(localFilm));

        List<Film> filmList = filmController.getAllFilms();
        assertEquals(1, filmList.size());
        assertTrue(filmList.contains(testFilm));
    }

    @Test
    void shouldNotUpdateFilmDate() {
        FilmController filmController = new FilmController();

        Film testFilm = filmController.addFilm(new Film("На Мюнхен", "История, приключение, комедия",
                LocalDate.of(2019,4, 25), 95, 10));

        Film localFilm = new Film("Довакин меняет профессию", "Комедия, ужасы",
                LocalDate.of(170, 5, 5), 120, 6);
        localFilm.setId(1);
        assertThrows(FilmReleaseDateException.class, () -> filmController.updateFilm(localFilm));

        List<Film> filmList = filmController.getAllFilms();
        assertEquals(1, filmList.size());
        assertTrue(filmList.contains(testFilm));
    }

    @Test
    void shouldNotUpdateFilmDuration() {
        FilmController filmController = new FilmController();

        Film testFilm1 = filmController.addFilm(new Film("На Мюнхен", "История, приключение, комедия",
                LocalDate.of(2019,4, 25), 95, 10));

        Film testFilm2 = filmController.addFilm(new Film("Красная перепёлка", "Шпионский триллер",
                LocalDate.of(2018,3, 2), 140, 10));

        Film localFilm1 = new Film("Gravity Loops", "Детское, приключение",
                LocalDate.of(2023, 5, 5), 0, 2);
        localFilm1.setId(1);
        assertThrows(FilmDurationException.class, () -> filmController.updateFilm(localFilm1));

        Film localFilm2 = new Film("Gravity Loops", "Детское, приключение",
                LocalDate.of(2023, 5, 5), -100, 2);
        localFilm2.setId(2);
        assertThrows(FilmDurationException.class, () -> filmController.updateFilm(localFilm2));

        List<Film> filmList = filmController.getAllFilms();
        assertEquals(2, filmList.size());
        assertTrue(filmList.contains(testFilm1));
        assertTrue(filmList.contains(testFilm2));
    }

    @Test
    void shouldGetAllFilms() {
        FilmController filmController = new FilmController();

        Film testFilm1 = filmController.addFilm(new Film("На Мюнхен", "История, приключение, комедия",
                LocalDate.of(2019,4, 25), 95, 10));

        Film testFilm2 = filmController.addFilm(new Film("Красная перепёлка", "Шпионский триллер",
                LocalDate.of(2018,3, 2), 140, 10));

        List<Film> filmList = filmController.getAllFilms();
        assertEquals(2, filmList.size());
        assertTrue(filmList.contains(testFilm1));
        assertTrue(filmList.contains(testFilm2));
    }
}
