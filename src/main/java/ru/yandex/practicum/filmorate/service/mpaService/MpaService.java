package ru.yandex.practicum.filmorate.service.mpaService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.logEnum.mpaEnums.InfoMpaEnums.InfoMpaServiceEnum;
import ru.yandex.practicum.filmorate.model.filmFields.MPA;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Slf4j
@Service
public class MpaService {
    private final MpaStorage mpaStorage;

    @Autowired
    public MpaService(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public List<MPA> getAllMpaMS() {
        log.info(InfoMpaServiceEnum.MPA_SERVICE_GET_ALL_MPA.getMessage());
        return mpaStorage.getAllMpa();
    }

    public MPA getMpaMS(int mpaID) {
        log.info(InfoMpaServiceEnum.MPA_SERVICE_GET_MPA.getInfo(String.valueOf(mpaID)));
        return mpaStorage.getMpa(mpaID);
    }
}
