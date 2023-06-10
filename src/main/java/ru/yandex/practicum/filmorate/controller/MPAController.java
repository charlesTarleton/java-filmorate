package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.logEnum.mpaEnums.InfoMpaEnums.InfoMpaControllerEnum;
import ru.yandex.practicum.filmorate.model.filmFields.MPA;
import ru.yandex.practicum.filmorate.service.mpaService.MpaService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mpa")
public class MPAController {
    private final MpaService mpaService;

    @Autowired
    public MPAController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping
    public List<MPA> getAllMpaMC() {
        log.info(InfoMpaControllerEnum.REQUEST_MPA_CONTROLLER_GET_ALL_MPA.getMessage());
        return mpaService.getAllMpaMS();
    }

    @GetMapping("/{id}")
    public MPA getMpaMC(@PathVariable("id") int mpaID) {
        log.info(InfoMpaControllerEnum.REQUEST_MPA_CONTROLLER_GET_MPA.getInfo(String.valueOf(mpaID)));
        return mpaService.getMpaMS(mpaID);
    }
}
