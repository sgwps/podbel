package ru.hse_se_podbel.data.models.enums;

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
}
