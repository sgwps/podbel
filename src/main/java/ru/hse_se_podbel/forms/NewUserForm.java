package ru.hse_se_podbel.forms;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NewUserForm {

    private String username;
    private String password;
    private boolean isAdmin;
}
