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

    @GetMapping(value={"/admin/home", "/admin"})
    public String adminHome(Model model) {
        return findPaginated(1, "LastName", "asc", model);
    }

    @GetMapping("/admin/home/page/{pageno}")
    public String findPaginated(@PathVariable(value = "pageno") int pageNo,
                                @RequestParam("sort_field") String sortField,
                                @RequestParam("sort_dir") String sortDir,
                                Model model) {
        UserAccountManagementController.setActiveUserName(model, userService);

        int pageSize = ControllerConstants.PAGE_SIZE;

        Page<User> page = userService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<User> listUsers = page.getContent();

        UserAccountManagementController.modelAddAttributes(pageNo, sortField, sortDir, model,
                page.getTotalPages(), page.getTotalElements());

        model.addAttribute("listUsers", listUsers);

        return "admin/home";
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
