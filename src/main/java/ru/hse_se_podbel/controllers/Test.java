package ru.hse_se_podbel.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.hse_se_podbel.data.models.User;
import ru.hse_se_podbel.data.service.UserService;

@Controller
@RequestMapping
public class Test {

    @Autowired
    private UserService userService;
    @GetMapping("/")
    public String testLogout(Model model, @AuthenticationPrincipal UserDetails userDetails) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.loadUserByUsername(username);
        String role = user.getRole().toString();
        model.addAttribute("role1", role);
        return "/index";
    }
    @PostMapping("/logout")
    public String test() {
        return "redirect:/login";
    }

}
