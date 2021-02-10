package ua.svitl.enterbankonline.controller;

import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.svitl.enterbankonline.model.*;
import ua.svitl.enterbankonline.model.dto.ExistingPaymentDto;
import ua.svitl.enterbankonline.model.dto.PaymentDto;
import ua.svitl.enterbankonline.service.BankAccountService;
import ua.svitl.enterbankonline.service.PersonService;
import ua.svitl.enterbankonline.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Bank Account Management Controller.
 */
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

    /**
     * User homepage.
     * @param model Model
     * @return user/home.html
     */
    @GetMapping(value={"/user/home", "/user"})
    public String userHome(Model model) {
        return findPaginated(1, ControllerConstants.MODEL_ATTR_BANK_ACCOUNT_NUMBER, "asc", model);
    }

    @GetMapping(value={"/user/home/page/{pageno}", "/user/home/{pageno}"})
    public String findPaginated(@PathVariable(value = "pageno") int pageNo,
                                @RequestParam("sortfield") String sortField,
                                @RequestParam("sortdir") String sortDir,
                                Model model) {
        int pageSize = ControllerConstants.PAGE_SIZE;

        Person person = setActiveUserName(model);

        makePaginated(pageNo, sortField, sortDir, model, pageSize, person);

        return "user/home";
    }

    private void makePaginated(@PathVariable("pageno") int pageNo,
                               @RequestParam("sortfield") String sortField,
                               @RequestParam("sortdir") String sortDir,
                               Model model,
                               int pageSize,
                               Person person) {
        Page<BankAccount> page = bankAccountService.findPaginated(person, pageNo, pageSize, sortField, sortDir);
        List<BankAccount> listBankAccounts = page.getContent();

        LoginController.modelAddAttributes(pageNo, sortField, sortDir, model, page.getTotalPages(), page.getTotalElements());

        Map<BankAccount, List<CreditCard>> linkedAccountsCards = bankAccountService.getCardsForEachBankAccount(listBankAccounts);
        model.addAttribute("mapAccountCards", linkedAccountsCards);
    }

    /**
     * User payments.
     * @param model Model
     * @return user/payments.html
     */
    @GetMapping(value="/user/payments")
    public String userPayments(Model model) {
        return findPaginatedPayments(1, "PaymentId", "asc", model);
    }

    @GetMapping("/user/payments/page/{pageno}")
    public String findPaginatedPayments(@PathVariable(value = "pageno") int pageNo,
                                        @RequestParam("sortfield") String sortField,
                                        @RequestParam("sortdir") String sortDir,
                                        Model model) {

        Person person = setActiveUserName(model);

        int pageSize = ControllerConstants.PAGE_SIZE;
        Page<Payment> page = bankAccountService.findPaginatedPayments(person, pageNo, pageSize, sortField, sortDir);
        List<Payment> listPayments = page.getContent();

        LoginController.modelAddAttributes(pageNo, sortField, sortDir, model, page.getTotalPages(), page.getTotalElements());

        model.addAttribute("listPayments", listPayments);

        return "user/payments";
    }

    /**
     * User form for new payment.
     * @param accountId bank account id
     * @param model Model
     * @return user/make_payment.html
     */
    @GetMapping(value={"/user/home/show_form_for_payment/{id}", "/user/make_payment/{id}"})
    public String showFormForPayment(@PathVariable(value = "id") int accountId, Model model) {
        setActiveUserName(model);
        model.addAttribute("accountId", accountId);
        model.addAttribute(ControllerConstants.BANK_ACCOUNT_NUMBER, bankAccountService.getBankAccountNumberById(accountId));
        Payment payment = new Payment();
        model.addAttribute("payment", payment);
        return ControllerConstants.VIEW_USER_MAKE_PAYMENT;
    }

    @PostMapping(value="/user/make_payment/{id}")
    public String makePayment(@PathVariable(value = "id") int accountId,
                              @ModelAttribute("payment") @Valid PaymentDto paymentDto,
                              BindingResult bindingResult,
                              Model model) {
        model.addAttribute("accountId", accountId);
        model.addAttribute("bankAccountFromNumber", bankAccountService.getBankAccountNumberById(accountId));
        setActiveUserName(model);
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "{error.message}");
            return "user/make_payment";
        }
        Payment payment = bankAccountService.savePreparedPayment(paymentDto, accountId);
        String toBankAccount = paymentDto.getToBankAccount();
        String getToBankAccount = payment.getToBankAccount();
        if (Objects.equals(toBankAccount, getToBankAccount)) {
            return confirmPayment(payment.getPaymentId(), model);
        } else {
            model.addAttribute(ControllerConstants.MODEL_ATTR_STRING_INFO, "{stringinfo.something.went.wrong}");
            return "user/make_payment";
        }

    }

    /**
     * User send / confirm payment
     * @param id payment id
     * @param model Model
     * @return user/send_payment.html
     */
    @GetMapping(value={"/user/payments/confirm_payment/{id}", "/user/send_payment/{id}"})
    public String confirmPayment(@PathVariable(value = "id") int id,
                                 Model model) {
        setActiveUserName(model);
        Payment payment = bankAccountService.getPaymentById(id);
        if (id != payment.getPaymentId()) {
            model.addAttribute("stringInfo", "{stringinfo.something went wrong}");
            return ControllerConstants.VIEW_USER_SEND_PAYMENT;
        }
        model.addAttribute("payment", payment);
        return "user/send_payment";
    }

    @PostMapping(value="/user/send_payment/save")
    public String sendPreparedPayment(@ModelAttribute("payment") @Valid ExistingPaymentDto paymentDto,
                                       BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            setActiveUserName(model);
            model.addAttribute("errorMessage", "{error.message}");
            return "user/send_payment";
        }
        model.addAttribute("successMessage", "{success.message}");
        Payment thePayment = bankAccountService.getPaymentByExistingPayment(paymentDto);
        model.addAttribute("bankAccountFromNumber", bankAccountService.getBankAccountNumberById(thePayment.getBankAccount().getBankAccountId()));
        return "redirect:/user/payments";
    }

    @GetMapping(value="/user/home/enable_account_request/{id}")
    public String enableAccount(@PathVariable(value = "id") int id){
        bankAccountService.updateAccountActive(id, true);
        return "redirect:/user/home";
    }

    @GetMapping(value="/user/home/disable_account/{id}")
    public String disableAccount(@PathVariable(value = "id") int id){
        bankAccountService.updateAccountActive(id, false);

        return "redirect:/user/home";
    }

    @GetMapping(value={"/admin/home/show_user_accounts/{id}", "/admin/user_accounts/{id}"})
    public String showUserAccounts(@PathVariable(value = "id") int id, Model model) {
        model.addAttribute("uid", id);
        return findPaginatedUserAccounts(id, 1, ControllerConstants.MODEL_ATTR_BANK_ACCOUNT_NUMBER, "asc", model);
    }

    @GetMapping(value="/admin/user_accounts/{id}/page/{pageno}")
    public String findPaginatedUserAccounts(@PathVariable(value = "id") int id,
                                @PathVariable(value = "pageno") int pageNo,
                                @RequestParam("sortfield") String sortField,
                                @RequestParam("sortdir") String sortDir,
                                Model model) {
        int pageSize = ControllerConstants.PAGE_SIZE;

        User user = userService.getUserByUserId(id);
        if (id == user.getUserId()) {
            Person person = personService.getPersonByUser(user);

            model.addAttribute("userName", person.getLastName() + ' ' + person.getFirstName());
            model.addAttribute("uid", id);

            makePaginated(pageNo, sortField, sortDir, model, pageSize, person);

            return "admin/user_accounts";
        } else {
            model.addAttribute("stringInfo", "No such user: wrong id");
            return "error";
        }

    }


    @GetMapping(value="/admin/user_accounts/{u}/enable_account/{id}")
    public String enableBankAccount(@PathVariable(value = "u") int uid, @PathVariable("id") int id, Model model) {
        String resultInfo = bankAccountService.updateAccount(id, true);
        model.addAttribute("uid", uid);
        model.addAttribute("resultInfo", resultInfo);
        return findPaginatedUserAccounts(uid, 1, ControllerConstants.MODEL_ATTR_BANK_ACCOUNT_NUMBER, "asc", model);
    }


    @GetMapping(value="/admin/user_accounts/{u}/disable_account/{id}")
    public String disableBankAccount(@PathVariable(value = "u") int uid, @PathVariable("id") int id, Model model) {
        String resultInfo = bankAccountService.updateAccount(id, false);
        model.addAttribute("uid", uid);
        model.addAttribute("resultInfo", resultInfo);
        return findPaginatedUserAccounts(uid, 1, ControllerConstants.MODEL_ATTR_BANK_ACCOUNT_NUMBER, "asc", model);
    }

    public Person setActiveUserName(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByName(auth.getName());
        Person person = personService.getPersonByUser(user);
        model.addAttribute("activeUserName", person.getLastName() + ' ' + person.getFirstName());
        return person;
    }

    @GetMapping(value={"/user/payments/delete_payment/{id}", "/user/send_payment/delete_payment{id}"})
    public String deletePayment(@PathVariable(value = "id") int paymentId) {
        bankAccountService.deletePaymentById(paymentId);
        return "redirect:/user/payments";
    }
}
