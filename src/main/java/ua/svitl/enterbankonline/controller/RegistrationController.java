package ua.svitl.enterbankonline.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.svitl.enterbankonline.model.User;
import ua.svitl.enterbankonline.service.UserService;

import javax.validation.Valid;

@Controller
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute(value = "user")
    public User newUser() {
        return new User();
    }


    @GetMapping("/admin/registration")
    public String registration(Model model) {
        model.addAttribute("user", new User());
        return "admin/registration";
    }

    @PostMapping("/admin/registration")
    public String createNewUser(@ModelAttribute("user") @Valid User user,
                                BindingResult bindingResult,
                                Model model) {
        if(userService.findUserByUserName(user.getUserName()) != null) {
            bindingResult.rejectValue("userName", "error.message",
                    "{error.user}");
            model.addAttribute("errorMessage", "The user already exists");
        }
        if (bindingResult.hasErrors()) {
            return "admin/registration";
        }
        userService.saveUser(user);
        model.addAttribute("successMessage", "Congratulations! The new user has been added!");
        return "admin/registration";
    }

    @PostMapping("/admin/saveUser")
    public String saveUser(@ModelAttribute("user") @Valid User user) {
        userService.saveUser(user);
        return "redirect:/admin/home";
    }

}
