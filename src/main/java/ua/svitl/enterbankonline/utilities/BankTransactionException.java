package ua.svitl.enterbankonline.utilities;

public class BankTransactionException extends RuntimeException {
    private String message;
    public BankTransactionException(String message) {
        super(message);
        this.message = message;
    }
}
