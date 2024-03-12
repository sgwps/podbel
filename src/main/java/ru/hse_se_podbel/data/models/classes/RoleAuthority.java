package ru.hse_se_podbel.data.models.classes;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import ru.hse_se_podbel.data.models.enums.Role;

@AllArgsConstructor
public class RoleAuthority implements GrantedAuthority {

    private Role role;

    @Override
    public String getAuthority() {
        return role.toString();
    }
}