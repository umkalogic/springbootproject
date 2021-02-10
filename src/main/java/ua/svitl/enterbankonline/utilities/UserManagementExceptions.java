package ua.svitl.enterbankonline.utilities;

public class UserManagementExceptions extends RuntimeException {
    private String message;
    public UserManagementExceptions(String message) {
        super(message);
        this.message = message;
    }

}
