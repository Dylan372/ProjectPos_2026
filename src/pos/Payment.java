package pos;

/**
 * A cash payment received for a sale. Information Expert: knows its amount.
 */
public class Payment {

    private final double amount;

    public Payment(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }
}
