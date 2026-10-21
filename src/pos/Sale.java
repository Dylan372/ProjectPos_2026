package pos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A sale transaction.
 * GRASP patterns applied:
 * - Information Expert: Sale knows its line items, so it knows its total and change.
 * - Creator: Sale contains its SalesLineItems, so it creates them.
 */
public class Sale {

    private final List<SalesLineItem> lineItems = new ArrayList<>();
    private final LocalDateTime dateTime = LocalDateTime.now();
    private Payment payment;
    private boolean isComplete = false;

    /** Creator pattern: Sale aggregates its line items, so it creates them. */
    public void makeLineItem(ProductSpecification spec, int quantity) {
        if (isComplete) {
            throw new IllegalStateException("Sale is already complete.");
        }
        lineItems.add(new SalesLineItem(spec, quantity));
    }

    /** Information Expert: Sale has all the information to compute its own total. */
    public double getTotal() {
        double total = 0;
        for (SalesLineItem sli : lineItems) {
            total += sli.getSubtotal();
        }
        return total;
    }

    public void makePayment(double cashTendered) {
        if (cashTendered < getTotal()) {
            throw new IllegalArgumentException(
                    String.format("Insufficient payment: %.2f < %.2f", cashTendered, getTotal()));
        }
        this.payment = new Payment(cashTendered);
        this.isComplete = true;
    }

    public double getChange() {
        if (payment == null) {
            return 0;
        }
        return payment.getAmount() - getTotal();
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public List<SalesLineItem> getLineItems() {
        return Collections.unmodifiableList(lineItems);
    }
}
