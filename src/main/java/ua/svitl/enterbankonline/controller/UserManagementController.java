package ua.svitl.enterbankonline.controller;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
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
import ua.svitl.enterbankonline.model.User;
import ua.svitl.enterbankonline.model.dto.UserDto;
import ua.svitl.enterbankonline.model.validation.groups.BasicUserInfo;
import ua.svitl.enterbankonline.service.UserService;


@Controller
@Validated(BasicUserInfo.class)
public class UserManagementController {

    private final UserService userService;
    private final ModelMapper modelMapper = new ModelMapper();

    public UserManagementController(UserService userService) {
        this.userService = userService;
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
    }

    @ModelAttribute(value = "user")
    public UserDto newUser() {
        return new UserDto();
    }

    @GetMapping(value={"/admin/home/show_form_for_user_update/{id}", "/admin/update_user/{id}"})
    public String showFormForUpdate(@PathVariable(value = "id") int id, Model model) {
        User user = userService.getUserByUserId(id);
        if (id != user.getUserId()) {
            model.addAttribute("stringInfo", "User not found: wrong id");
        }
        model.addAttribute("user", user);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("activeUserName", auth.getName());
        return "admin/update_user";
    }

    @PostMapping(value="/admin/update_user/save_user")
    public String updateUser(@Validated(BasicUserInfo.class) @ModelAttribute("user") UserDto userDto,
                             BindingResult bindingResult, Model model) {
        User user = modelMapper.map(userDto, User.class);
        if (bindingResult.hasErrors()) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            model.addAttribute("activeUserName", auth.getName());
            model.addAttribute("errorMessage", "{error.message}");
            return "admin/update_user";
        }
        userService.updateUser(user);
        return "redirect:/admin/home";
    }

    @GetMapping(value="/admin/home/enable/{id}")
    public String enableUser(@PathVariable(value = "id") int id, Model model){
        User user = userService.updateUserActive(id, true);
        if (id != user.getUserId()) {
            model.addAttribute("stringInfo", "User not found: wrong id");
        }
        return "redirect:/admin/home";
    }

    @GetMapping(value="/admin/home/disable/{id}")
    public String disableUser(@PathVariable(value = "id") int id, Model model){
        User user = userService.updateUserActive(id, false);
        if (id != user.getUserId()) {
            model.addAttribute("stringInfo", "User not found: wrong id");
        }
        return "redirect:/admin/home";
    }

}
