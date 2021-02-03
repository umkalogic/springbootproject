package ua.svitl.enterbankonline.model;

public class EntityConstants {
    public static final int BANK_MFO = 320_130;
    public static final long DEFAULT_BANK_ACCOUNT = 26_000_000_000_000L + EntityConstants.BANK_MFO * 1_000_000L;
    public static final String DEFAULT_CREDIT_CARD_NAME = "MasterCard";
    public static final String DEFAULT_BANK_ACCOUNT_TYPE = "Regular";
    public static final String DEFAULT_CURRENCY = "грн.";

    private EntityConstants() { }
}
