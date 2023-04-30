package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.IncorrectFilmException;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FilmServiceTest {
    private FilmController filmController;
    private Film testFilm1;
    private Film testFilm2;
    private Film localFilm;

    @BeforeEach
    public void start() {
        filmController = new FilmController();

        testFilm1 = filmController.addFilm(new Film("На Мюнхен", "История, приключение, комедия",
                LocalDate.of(2019,4, 25), 95, 10));

        testFilm2 = filmController.addFilm(new Film("Красная перепёлка", "Шпионский триллер",
                LocalDate.of(2018,3, 2), 140, 10));
    }

    @AfterEach
    public  void end() {
        FilmController.filmsId = 1;
        FilmService.films.clear();
    }

    @Test
    void shouldCreateFilm() {
        localFilm = new Film("Секретный документ", "Триллер, документальный фильм",
                LocalDate.of(2022, 4, 1), 160, 5);
        filmController.addFilm(localFilm);

        List<Film> filmList = filmController.getAllFilms();
        assertEquals(3, filmList.size());
        assertTrue(filmList.contains(localFilm));
    }

    @Test
    void shouldNotCreateFilmDescription() {
        assertThrows(IncorrectFilmException.class,
                () -> filmController.addFilm(new Film("Непосредственно Геральт", "Этот фильм" +
                        " повествует о простом парне по имени Геральт, который жил обычной жизнью темерского" +
                        " школьника. Но однажды его засосало в какую-то дешевую японскую ММО РПГ и в этом мире" +
                        " он переродился в великого воина. И хотя его искусство в магии знаков было велико," +
                        " ему предстояло еще многому научится. Но мы верили, что Геральт спасет мир",
                LocalDate.of(2020, 1, 1), 160, 4)));

        List<Film> filmList = filmController.getAllFilms();
        assertEquals(2, filmList.size());
        assertTrue(filmList.contains(testFilm1));
        assertTrue(filmList.contains(testFilm2));
    }

    @Test
    void shouldNotCreateFilmDate() {
        assertThrows(IncorrectFilmException.class,
                () -> filmController.addFilm(new Film("Довакин меняет профессию", "Комедия, ужасы",
                                LocalDate.of(170, 5, 5), 120, 6)));

        List<Film> filmList = filmController.getAllFilms();
        assertEquals(2, filmList.size());
        assertTrue(filmList.contains(testFilm1));
        assertTrue(filmList.contains(testFilm2));
    }

    @Test
    void shouldNotCreateFilmDuration() {
        assertThrows(IncorrectFilmException.class,
                () -> filmController.addFilm(new Film("Gravity Loops", "Детское, приключение",
                LocalDate.of(2023, 5, 5), 0, 2)));
        assertThrows(IncorrectFilmException.class,
                () -> filmController.addFilm(new Film("Gravity Loops", "Детское, приключение",
                LocalDate.of(2023, 5, 5), -100, 2)));

        List<Film> filmList = filmController.getAllFilms();
        assertEquals(2, filmList.size());
        assertTrue(filmList.contains(testFilm1));
        assertTrue(filmList.contains(testFilm2));
    }

    @Test
    void shouldUpdateFilm() {
        localFilm = new Film("На Тоскану", "История, приключение, комедия",
                LocalDate.of(2019,4, 25), 95, 10);
        localFilm.setId(1);
        filmController.updateFilm(localFilm);

        List<Film> filmList = filmController.getAllFilms();
        assertEquals(2, filmList.size());
        assertTrue(filmList.contains(localFilm));
        assertTrue(filmList.contains(testFilm2));
    }

    @Test
    void shouldNotUpdateFilmDescription() {
        localFilm = new Film("Непосредственно Геральт", "Этот фильм,повествует о" +
                " простом парне по имени Геральт, который жил обычной жизнью темерского школьника. Но однажды" +
                " его засосало в какую-то дешевую японскую ММО РПГ и в этом мире он переродился в великого воина." +
                " И хотя его искусство в магии знаков было велико, ему предстояло еще многому научится." +
                "Но мы верили, что Геральт спасет мир",
                LocalDate.of(2020, 1, 1), 160, 4);
        localFilm.setId(1);
        assertThrows(IncorrectFilmException.class, () -> filmController.updateFilm(localFilm));

        List<Film> filmList = filmController.getAllFilms();
        assertEquals(2, filmList.size());
        assertTrue(filmList.contains(testFilm1));
        assertTrue(filmList.contains(testFilm2));
    }

    @Test
    void shouldNotUpdateFilmDate() {
        localFilm = new Film("Довакин меняет профессию", "Комедия, ужасы",
                LocalDate.of(170, 5, 5), 120, 6);
        localFilm.setId(2);
        assertThrows(IncorrectFilmException.class, () -> filmController.updateFilm(localFilm));

        List<Film> filmList = filmController.getAllFilms();
        assertEquals(2, filmList.size());
        assertTrue(filmList.contains(testFilm1));
        assertTrue(filmList.contains(testFilm2));
    }

    @Test
    void shouldNotUpdateFilmDuration() {
        localFilm = new Film("Gravity Loops", "Детское, приключение",
                LocalDate.of(2023, 5, 5), 0, 2);
        localFilm.setId(2);
        assertThrows(IncorrectFilmException.class, () -> filmController.updateFilm(localFilm));

        localFilm = new Film("Gravity Loops", "Детское, приключение",
                LocalDate.of(2023, 5, 5), -100, 2);
        localFilm.setId(1);
        assertThrows(IncorrectFilmException.class, () -> filmController.updateFilm(localFilm));

        List<Film> filmList = filmController.getAllFilms();
        assertEquals(2, filmList.size());
        assertTrue(filmList.contains(testFilm1));
        assertTrue(filmList.contains(testFilm2));
    }

    @Test
    void shouldGetAllFilms() {
        List<Film> filmList = filmController.getAllFilms();
        assertEquals(2, filmList.size());
        assertTrue(filmList.contains(testFilm1));
        assertTrue(filmList.contains(testFilm2));
    }
}
