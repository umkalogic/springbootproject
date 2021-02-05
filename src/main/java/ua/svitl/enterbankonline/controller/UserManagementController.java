package ua.svitl.enterbankonline.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ua.svitl.enterbankonline.model.validation.groups.BasicUserInfo;
import ua.svitl.enterbankonline.model.User;
import ua.svitl.enterbankonline.service.UserService;


@Controller
@Validated(BasicUserInfo.class)
public class UserManagementController {

    private final UserService userService;

    public UserManagementController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute(value = "user")
    public User newUser() {
        return new User();
    }

    @GetMapping(value={"/admin/home/show_form_for_user_update/{id}", "/admin/update_user/{id}"})
    public String showFormForUpdate(@PathVariable(value = "id") int id, Model model) {
        User user = userService.getUserId(id);
        model.addAttribute("user", user);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("activeUserName", auth.getName());
        return "admin/update_user";
    }

    @PostMapping(value="/admin/update_user/save_user")
    public String updateUser(@Validated(BasicUserInfo.class) @ModelAttribute("user") User user,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            model.addAttribute("activeUserName", auth.getName());
            model.addAttribute("errorMessage", "{error.message}");
            return "admin/update_user";
        }
        userService.updateUser(user);
        return "redirect:/admin/home";
    }

}
