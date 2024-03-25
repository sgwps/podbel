package ru.hse_se_podbel.data.models.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
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

    @Override
    public String getAuthority() {
        return this.toString();
    }

    public boolean isNotActivated() {  // True - если НЕ активирован
        return this.equals(Role.ADMIN_NOT_ACTIVATED) || this.equals(Role.USER_NOT_ACTIVATED);
    }

}
