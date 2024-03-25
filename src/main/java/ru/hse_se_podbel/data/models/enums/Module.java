package ru.hse_se_podbel.data.models.enums;

import java.util.NoSuchElementException;

public enum Module {
    FIRST(1),
    SECOND(2),
    THIRD(3);


    private int number;

    Module (int number) {
        this.number = number;
    }

    public int getValue() {
        return number;
    }

    public static Module find(int value) {
        for (Module module : values()){
            if (module.getValue() == value){
                return module;
            }
        }
        throw new NoSuchElementException("Неккорректный модуль");
    }
}
