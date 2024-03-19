package ru.hse_se_podbel.data.models.enums;

public enum Role {
//    USER,
//    ADMIN,
//    USER_NOT_ACTIVATED,
//    ADMIN_NOT_ACTIVATED;
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN"),
    USER_NOT_ACTIVATED("ROLE_USER_NOT_ACTIVATED"),
    ADMIN_NOT_ACTIVATED("ROLE_ADMIN_NOT_ACTIVATED");

    private final String name;

    Role(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

}
