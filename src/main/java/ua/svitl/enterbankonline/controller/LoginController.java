package ua.svitl.enterbankonline.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
import ua.svitl.enterbankonline.model.Person;
import ua.svitl.enterbankonline.model.User;
import ua.svitl.enterbankonline.model.dto.UserPersonDataDto;
import ua.svitl.enterbankonline.service.PersonService;
import ua.svitl.enterbankonline.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Controller
public class LoginController {

    private final UserService userService;
    private final PersonService personService;

    public LoginController(UserService userService, PersonService personService) {
        this.userService = userService;
        this.personService = personService;
    }

    @ModelAttribute(value = "user")
    public User newUser() {
        return new User();
    }

    @ModelAttribute(value = "person")
    public Person newPerson() {
        return new Person();
    }

    @ModelAttribute(value = "userperson")
    public UserPersonDataDto newUserPerson() {
        return new UserPersonDataDto();
    }

    @GetMapping(value={"/", "/login"})
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @GetMapping(value={"/admin/home", "/admin"})
    public String adminHome(Model model) {
        return findPaginated(1, "userName", "asc", model);
    }

    @GetMapping("/admin/home/page/{pageno}")
    public String findPaginated(@PathVariable(value = "pageno") int pageNo,
                                @RequestParam("sortfield") String sortField,
                                @RequestParam("sortdir") String sortDir,
                                Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        Person person = personService.getPersonByPersonUsersContains(user);
        model.addAttribute("activeUserName", person.getLastName() + " " + person.getFirstName());
        model.addAttribute("person", person);

        int pageSize = ControllerConstants.PAGE_SIZE;
        Page<UserPersonDataDto> page = userService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<UserPersonDataDto> listUserPersonData = page.getContent();

        LoginController.modelAddAttributes(pageNo, sortField, sortDir, model,
                page.getTotalPages(), page.getTotalElements());

        model.addAttribute("listUserPersonData", listUserPersonData);

        return "admin/home";
    }

    public static Model modelAddAttributes(@PathVariable("pageno") int pageNo,
                                           @RequestParam("sortfield") String sortField,
                                           @RequestParam("sortdir") String sortDir,
                                           Model model,
                                           int totalPages,
                                           long totalElements) {
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalElements);

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        return model;
    }

    @GetMapping(value="/default")
    public String defaultAfterLogin(HttpServletRequest request,
                                    HttpServletResponse response,
                                    Authentication authentication)
            throws IOException, ServletException {
        request.authenticate(response);
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
            if (grantedAuthority.getAuthority().equals("ADMIN")) {
                return "redirect:/admin/home";
            } else if (grantedAuthority.getAuthority().equals("USER")) {
                return "redirect:/user/home";
            }
        }
        return "redirect:/";

    }

}
