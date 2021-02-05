package ua.svitl.enterbankonline.controller;

import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ua.svitl.enterbankonline.model.BankAccount;
import ua.svitl.enterbankonline.model.User;
import ua.svitl.enterbankonline.service.BankAccountService;
import ua.svitl.enterbankonline.service.UserService;

import java.util.List;

@Controller
public class UserAccountManagementController {
    private final UserService userService;
    private final BankAccountService bankAccountService;

    public UserAccountManagementController(UserService userService, BankAccountService bankAccountService) {
        this.userService = userService;
        this.bankAccountService = bankAccountService;
    }

    @ModelAttribute(value = "user")
    public User newUser() {
        return new User();
    }

    @GetMapping(value={"/user/home", "/user"})
    public String userHome(Model model) {
        return findPaginated(1, "BankAccountNumber", "asc", model);
    }

    @GetMapping("/user/home/page/{pageno}")
    public String findPaginated(@PathVariable(value = "pageno") int pageNo,
                                @RequestParam("sort_field") String sortField,
                                @RequestParam("sort_dir") String sortDir,
                                Model model) {
        User user = setActiveUserName(model, userService);
        int pageSize = ControllerConstants.PAGE_SIZE;

        Page<BankAccount> page = bankAccountService.findPaginated(user, pageNo, pageSize, sortField, sortDir);
        List<BankAccount> listBankAccounts = page.getContent();

        modelAddAttributes(pageNo, sortField, sortDir, model, page.getTotalPages(), page.getTotalElements());

        model.addAttribute("listBankAccounts", listBankAccounts);

        return "user/home";
    }

    public static User setActiveUserName(Model model, UserService userService) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        model.addAttribute("userName", user.getUserName() +
                "[" + user.getEmail() + "], " +
                user.getLastName() + " " +
                user.getFirstName() + ".");
        model.addAttribute("activeUserName", user.getUserName());
        return user;
    }

    public static Model modelAddAttributes(@PathVariable("pageno") int pageNo,
                                   @RequestParam("sort_field") String sortField,
                                   @RequestParam("sort_dir") String sortDir,
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

}
