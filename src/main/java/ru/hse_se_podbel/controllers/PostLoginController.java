package ru.hse_se_podbel.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.hse_se_podbel.data.models.User;
import ru.hse_se_podbel.data.service.UserService;

@Controller
@RequestMapping("/post_login")
public class PostLoginController {
    @Autowired
    UserService userService;

    @GetMapping
    public RedirectView getViewAfterLogin(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.loadUserByUsername(userDetails.getUsername());
        if (user.getAuthority().isNotActivated()) {
            return new RedirectView("/change_password");
        }
        return new RedirectView("/");
    }

}
