package ua.svitl.enterbankonline.controller;

import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ua.svitl.enterbankonline.model.BankAccount;
import ua.svitl.enterbankonline.model.CreditCard;
import ua.svitl.enterbankonline.model.Person;
import ua.svitl.enterbankonline.model.User;
import ua.svitl.enterbankonline.service.BankAccountService;
import ua.svitl.enterbankonline.service.PersonService;
import ua.svitl.enterbankonline.service.UserService;

import java.util.List;
import java.util.Map;

@Controller
public class AccountManagementController {
    private final UserService userService;
    private final PersonService personService;
    private final BankAccountService bankAccountService;

    public AccountManagementController(UserService userService,
                                       PersonService personService,
                                       BankAccountService bankAccountService) {
        this.userService = userService;
        this.personService = personService;
        this.bankAccountService = bankAccountService;
    }

    @GetMapping(value={"/user/home", "/user"})
    public String userHome(Model model) {
        return findPaginated(1, "BankAccountNumber", "asc", model);
    }

    @GetMapping("/user/home/page/{pageno}")
    public String findPaginated(@PathVariable(value = "pageno") int pageNo,
                                @RequestParam("sortfield") String sortField,
                                @RequestParam("sortdir") String sortDir,
                                Model model) {
        int pageSize = ControllerConstants.PAGE_SIZE;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        Person person = personService.getPersonByPersonUsersContains(user);
        model.addAttribute("person", person);
        model.addAttribute("activeUserName", person.getLastName() + ' ' + person.getFirstName() + '!');

        Page<BankAccount> page = bankAccountService.findPaginated(person, pageNo, pageSize, sortField, sortDir);
        List<BankAccount> listBankAccounts = page.getContent();

        LoginController.modelAddAttributes(pageNo, sortField, sortDir, model, page.getTotalPages(), page.getTotalElements());

        model.addAttribute("listBankAccounts", listBankAccounts);
        model.addAttribute("userLastName", person.getLastName());
        model.addAttribute("userFirstName", person.getFirstName());

        Map<Integer, List<CreditCard>> linkedCards = bankAccountService.getCardsForEachBankAccount(listBankAccounts);
        model.addAttribute("mapAccountIdCards", linkedCards);

        return "user/home";
    }

    // TODO for USER:
    // TODO Платежі user/payments.html thymeleaf
    // TODO Controller for payments
    // TODO user/home додати Картки до кожного рахунку в thymeleaf
    // TODO Deactivate BankAccount /user/home/deactivateAccount/{id}
    // TODO Send Request for activating Account once more
    // TODO make payment

    // TODO for ADMIN:
    // TODO BankAccount Management for specific user
    // TODO admin/user_accounts page /admin/home/show_user_accounts/{id}(id=${userdata.userId})
    // TODO block/unblock accounts /admin/user_accounts/deactivateAccount/{id}

    // TODO Generate Pdf Report on Payment

}
