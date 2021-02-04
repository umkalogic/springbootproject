package ua.svitl.enterbankonline.controller;

import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ua.svitl.enterbankonline.model.User;
import ua.svitl.enterbankonline.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    public String adminHome(Model model) {
        return findPaginated(1, "LastName", "asc", model);
    }

    @GetMapping("/admin/home/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir,
                                Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        model.addAttribute("userName", user.getUserName() +
                "[" + user.getEmail() + "], " +
                user.getLastName() + " " +
                user.getFirstName() + ".");
        model.addAttribute("activeUserName", user.getUserName());

        int pageSize = ControllerConstants.PAGE_SIZE;

        Page<User> page = userService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<User> listUsers = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("listUsers", listUsers);

        return "admin/home";
    }

    @GetMapping("/admin/home/showFormForUserUpdate/{id}")
    public String showFormForUpdate(@PathVariable(value = "id") int id, Model model) {
        User user = userService.getUserId(id);
        model.addAttribute("user", user);
        return "admin/update_user";
    }

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
        modelAndView.setViewName("user/home");

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
            if (grantedAuthority.getAuthority().equals("ADMIN")) {
                isAdmin = true;
                break;
            } else if (grantedAuthority.getAuthority().equals("USER")) {
                isUser = true;
            }
        }
        if (isAdmin) {
            return "redirect:/admin/home";
        } else if (isUser) {
            return "redirect:/user/home";
        } else {
            return "redirect:/";
        }
    }

}
