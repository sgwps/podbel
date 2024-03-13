package ru.hse_se_podbel.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import ru.hse_se_podbel.data.models.User;
import ru.hse_se_podbel.data.models.enums.Role;
import ru.hse_se_podbel.data.service.UserService;
import ru.hse_se_podbel.forms.NewUserForm;
import ru.hse_se_podbel.views.UserListItemView;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
@SessionAttributes("newUserForm")
public class UserAdministrationController {
    @Autowired
    UserService userService;

    @ModelAttribute("newUserForm")
    public NewUserForm getForm() {
        return new NewUserForm();
    }

    @GetMapping("/new")
    public String newUserView(Model model, @ModelAttribute("newUserForm") NewUserForm newUserForm) {
        model.addAttribute("newUserForm", newUserForm);
        return "/security/user_creation";
    }


    @PostMapping("/new")
    public String newUser(Model model, @ModelAttribute("newUserForm") NewUserForm newUserForm, BindingResult result, SessionStatus sessionStatus) {
        User user = User.builder().username(newUserForm.getUsername()).password(newUserForm.getPassword()).role(
                newUserForm.isAdmin() == true ?
                Role.ADMIN_NOT_ACTIVATED : Role.USER_NOT_ACTIVATED).build();
        try {
            userService.save(user, true);
            sessionStatus.setComplete();
            return newUserDataView(model, newUserForm);
        } catch (ValidationException e) {
            model.addAttribute("errors", "Имя пользователя или пароль не соотсветсвют критериям");
            return newUserView(model, newUserForm);
        }

    }

    public String newUserDataView(Model model, @ModelAttribute("newUserForm") NewUserForm newUserForm) {
        model.addAttribute("newUserForm", newUserForm);
        return "/security/user_data";
    }

    @GetMapping
    public String userList(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<UserListItemView> userListItemViewList = userService.loadAll().stream().map(user -> new UserListItemView(user)).toList();
        userListItemViewList.stream().forEach(userListItemView -> {
            if (userListItemView.getUsername().equals(userDetails.getUsername())) {
                userListItemView.setCurrentUser(true);
            }
        });
        model.addAttribute("users", userListItemViewList);
        return "/security/users";
    }

    @DeleteMapping("/delete/{username}")
    public String deleteDishAdmin(@PathVariable("username") String username, @AuthenticationPrincipal UserDetails userDetails) {
        if (!userDetails.getUsername().equals(username)) {
            boolean deleted = userService.delete(username);
        }
        return "redirect:/users";
    }
}
