package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.filmFields.MPA;

import java.util.List;

public interface MpaStorage {

    public List<MPA> getAllMpa();

    public MPA getMpa(int mpaID);
}
