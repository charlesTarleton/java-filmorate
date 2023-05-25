package ru.yandex.practicum.filmorate.logEnum;

import java.util.Arrays;

public enum ControllerLogEnum {
    FOUR_HUNDRED_ERROR_HANDLE("Произошла ошибка валидации(400): "),
    FOUR_HUNDRED_FOUR_ERROR_HANDLE("Произошла ошибка искомого объекта(404): "),
    FIVE_HUNDRED_ERROR_HANDLE("Произошла неизвестная ошибка(500): ");
    private final String message;

    ControllerLogEnum(String message) {
        this.message = message;
    }

    public String getInfo(Throwable value) {
        return message + Arrays.toString(value.getStackTrace());
    }
}
