package ua.svitl.enterbankonline.controller;

public class ControllerConstants {
    public static final String ADMIN_REGISTRATION = "admin/registration";
    public static final String ADMIN_HOME = "admin/home";
    public static final String USER_HOME = "user/home";
    public static final String REDIRECT = "redirect:/";
    public static final int PAGE_SIZE = 5;

    public enum USER_ROLE {
        ADMIN,
        USER
    }

    private ControllerConstants() { }
}
