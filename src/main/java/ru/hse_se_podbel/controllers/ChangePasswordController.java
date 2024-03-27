package ru.hse_se_podbel.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import ru.hse_se_podbel.configuration.PasswordEncoderConfig;
import ru.hse_se_podbel.data.models.User;
import ru.hse_se_podbel.data.service.UserService;
import ru.hse_se_podbel.forms.ChangePasswordForm;

import javax.validation.ValidationException;

@Controller
@RequestMapping("/change_password")
@SessionAttributes("changePasswordForm")
public class ChangePasswordController {


    @Autowired
    UserService userService;

    @ModelAttribute("changePasswordForm")
    public ChangePasswordForm getForm() {
        return new ChangePasswordForm();
    }

    @GetMapping
    public String getView(Model model, @ModelAttribute("changePasswordForm") ChangePasswordForm changePasswordForm) {
        model.addAttribute("changePasswordForm", changePasswordForm);
        return "/security/change_password";
    }


    @PostMapping
    public String proceed(Model model, @ModelAttribute("changePasswordForm") ChangePasswordForm changePasswordForm, @AuthenticationPrincipal UserDetails userDetails, BindingResult result, SessionStatus sessionStatus,
                          HttpServletRequest request) throws ServletException {
        try {
            User user = userService.loadUserByUsername(userDetails.getUsername());
            userService.updatePassword(user, changePasswordForm);
            sessionStatus.setComplete();
            request.logout();
            return "redirect:/";
        } catch (ValidationException e) {
            model.addAttribute("error", e.getMessage());
            return getView(model, changePasswordForm);
        }

    }
}
