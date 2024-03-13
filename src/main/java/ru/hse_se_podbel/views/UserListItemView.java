package ru.hse_se_podbel.views;

import lombok.Getter;
import lombok.Setter;
import ru.hse_se_podbel.data.models.User;
import ru.hse_se_podbel.data.models.enums.Role;

import java.util.UUID;

@Getter
public class UserListItemView {
    private final String admin = "Администратор";
    private final String not_activated = "Активирован";

    private String username;
    private String role;
    private String activationStatus;

    @Setter
    private boolean currentUser = false;

    public UserListItemView(User user) {
        this.username = user.getUsername();
        this.role = user.getRole() == Role.ADMIN_NOT_ACTIVATED || user.getRole() == Role.ADMIN ? admin : "";
        this.activationStatus = user.getRole() == Role.ADMIN_NOT_ACTIVATED || user.getRole() == Role.USER_NOT_ACTIVATED ? not_activated : "";
    }


}
