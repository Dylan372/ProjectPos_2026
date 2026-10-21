package pos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A return transaction (Handle Returns use case).
 * - Information Expert: knows its line items, so it knows the refund total.
 * - Creator: aggregates its ReturnLineItems, so it creates them.
 */
public class Return {

    private final List<ReturnLineItem> lineItems = new ArrayList<>();
    private final LocalDateTime dateTime = LocalDateTime.now();
    private boolean isComplete = false;

    public void addReturnLineItem(ProductSpecification spec, int quantity) {
        if (isComplete) {
            throw new IllegalStateException("Return is already complete.");
        }
        lineItems.add(new ReturnLineItem(spec, quantity));
    }

    public double getRefundTotal() {
        double total = 0;
        for (ReturnLineItem rli : lineItems) {
            total += rli.getSubtotal();
        }
        return total;
    }

    public void complete() {
        this.isComplete = true;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public List<ReturnLineItem> getLineItems() {
        return Collections.unmodifiableList(lineItems);
    }
}
