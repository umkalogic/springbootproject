package ua.svitl.enterbankonline.controller;

public class ControllerConstants {
    // views
    public static final String ADMIN_REGISTRATION = "admin/registration";
    public static final String ADMIN_HOME = "admin/home";
    public static final String USER_HOME = "user/home";
    public static final String REDIRECT = "redirect:/";
    public static final String VIEW_USER_MAKE_PAYMENT = "user/make_payment";
    public static final String VIEW_USER_SEND_PAYMENT = "user/send_payment";


    public static final String MODEL_ATTR_BANK_ACCOUNT_NUMBER = "BankAccountNumber";
    public static final String MODEL_ATTR_STRING_INFO = "stringInfo";

    // pagination
    public static final int PAGE_SIZE = 5;

    public static final String BANK_ACCOUNT_NUMBER = "bankAccountFromNumber";

    public enum USER_ROLE {
        ADMIN,
        USER
    }

    private ControllerConstants() { }
}
