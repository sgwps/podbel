package ru.hse_se_podbel.forms;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NewUserForm implements Form {
    @Override
    public boolean isValid() {
        return true;
    }

    private String username;
    private String password;
    private boolean isAdmin;
}
