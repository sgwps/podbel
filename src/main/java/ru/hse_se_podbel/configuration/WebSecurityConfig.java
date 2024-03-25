package ru.hse_se_podbel.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.hse_se_podbel.data.models.enums.Role;
import ru.hse_se_podbel.data.service.UserService;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoderConfig passwordEncoderConfig;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.formLogin(form -> form
                .loginPage("/login").defaultSuccessUrl("/post_login", true))
                .authorizeHttpRequests((authorize) -> authorize
//                .requestMatchers("/users/**").hasRole(Role.ADMIN.toString())
                        .requestMatchers("/users/**").hasAuthority("ADMIN")
                .requestMatchers("/change_password").authenticated()
                .requestMatchers("/").authenticated()
                .anyRequest().permitAll())
                .csrf(csrf -> csrf.disable())
                .logout((logout) -> logout.logoutSuccessUrl("/login")).build();
    }


    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoderConfig.getPasswordEncoder());
    }
}