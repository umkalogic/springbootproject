package ua.svitl.enterbankonline.controller;

import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ua.svitl.enterbankonline.model.User;
import ua.svitl.enterbankonline.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Collection;
import java.util.List;


@Controller
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value={"/", "/login"})
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @GetMapping(value="/admin/home")
    public String adminHome(@ModelAttribute("user") @Valid User user, Model model) {
        return findPaginated(user, 1, "LastName", "asc", model);
    }

    @GetMapping("/admin/home/page/{pageNo}")
    public String findPaginated(@Valid User user,
                                @PathVariable(value = "pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir,
                                Model model) {
        int pageSize = ControllerConstants.PAGE_SIZE;

        Page<User> page = userService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<User> listUsers = page.getContent();

        model.addAttribute("userName", user.getUserName() +
                        "[" + user.getEmail() + "], " +
                        user.getLastName() + " " +
                        user.getFirstName() + ".");
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("listUsers", listUsers);
        return ControllerConstants.ADMIN_HOME;
    }

//    @GetMapping(value="/admin/home")
//    public ModelAndView adminHome(){
//        ModelAndView modelAndView = new ModelAndView();
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        User user = userService.findUserByUserName(auth.getName());
//        modelAndView.addObject("userName",
//                "Welcome " + user.getUserName() +
//                        "[" + user.getEmail() + "] " +
//                        user.getLastName() + " " +
//                        user.getFirstName());
//        modelAndView.addObject("adminMessage",
//                "Content Available Only for Users with Admin Role");
//        modelAndView.setViewName(ControllerConstants.ADMIN_HOME);
//        return modelAndView;
//    }

    @GetMapping(value="/user/home")
    public ModelAndView userHome(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("userName",
                "Welcome " + user.getUserName() +
                        "[" + user.getEmail() + "] " +
                user.getLastName() + " " +
                user.getFirstName());
        modelAndView.addObject("userMessage",
            "Content Available Only for Users with User Role");
        modelAndView.setViewName(ControllerConstants.USER_HOME);

        return modelAndView;
    }

    @GetMapping(value="/default")
    public String defaultAfterLogin(HttpServletRequest request,
                                    HttpServletResponse response,
                                    Authentication authentication)
            throws IOException, ServletException {
        request.authenticate(response);
        boolean isAdmin = false;
        boolean isUser = false;
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
            if (grantedAuthority.getAuthority().equals(ControllerConstants.USER_ROLE.ADMIN.toString())) {
                isAdmin = true;
                break;
            } else if (grantedAuthority.getAuthority().equals(ControllerConstants.USER_ROLE.USER.toString())) {
                isUser = true;
            }
        }
        if (isAdmin) {
            return ControllerConstants.REDIRECT + ControllerConstants.ADMIN_HOME;
        } else if (isUser) {
            return ControllerConstants.REDIRECT + ControllerConstants.USER_HOME;
        } else {
            return ControllerConstants.REDIRECT;
        }
    }

}
