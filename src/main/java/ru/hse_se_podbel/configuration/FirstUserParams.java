package ru.hse_se_podbel.configuration;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FirstUserParams {
    private final String username = "admin";
    private final String password = "1234567890";

}
