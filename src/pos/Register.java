package pos;

/**
 * GRASP Controller (facade controller): the first domain object beyond the UI
 * layer that receives and coordinates the system operations of the two use
 * cases (Process Sale, Handle Returns). It does little work itself — it
 * delegates to Sale, Return and Store.
 *
 * System operations mirror the SSD system events:
 *   makeNewSale(), enterItem(itemID, qty), endSale(), makePayment(amount)
 *   makeNewReturn(), enterReturnedItem(itemID, qty), endReturn(), receiveRefund(amount)
 */
public class Register {

    private final Store store;
    private Sale currentSale;
    private Return currentReturn;

    public Register(Store store) {
        this.store = store;
    }

    // ---------- Process Sale ----------

    public void makeNewSale() {
        currentSale = new Sale();
    }

    public void enterItem(int itemID, int quantity) {
        requireSale();
        ProductSpecification spec = store.getSpecification(itemID);
        if (spec == null) {
            throw new IllegalArgumentException("Unknown item ID: " + itemID);
        }
        if (store.getStock(itemID) < quantity) {
            throw new IllegalStateException("Insufficient stock for item " + itemID);
        }
        currentSale.makeLineItem(spec, quantity);
    }

    public double endSale() {
        requireSale();
        return currentSale.getTotal();
    }

    public double makePayment(double cashTendered) {
        requireSale();
        currentSale.makePayment(cashTendered);
        // inventory is updated only when the sale is paid
        for (SalesLineItem sli : currentSale.getLineItems()) {
            store.reduceStock(sli.getSpecification().getItemID(), sli.getQuantity());
        }
        return currentSale.getChange();
    }

    public Sale getCurrentSale() {
        return currentSale;
    }

    // ---------- Handle Returns ----------

    public void makeNewReturn() {
        currentReturn = new Return();
    }

    public void enterReturnedItem(int itemID, int quantity) {
        requireReturn();
        ProductSpecification spec = store.getSpecification(itemID);
        if (spec == null) {
            throw new IllegalArgumentException("Unknown item ID: " + itemID);
        }
        currentReturn.addReturnLineItem(spec, quantity);
    }

    public double endReturn() {
        requireReturn();
        return currentReturn.getRefundTotal();
    }

    /** Refund is paid to the customer; returned items are restocked. */
    public void receiveRefund(double amount) {
        requireReturn();
        if (amount < currentReturn.getRefundTotal()) {
            throw new IllegalArgumentException("Refund amount too small.");
        }
        for (ReturnLineItem rli : currentReturn.getLineItems()) {
            store.restock(rli.getSpecification().getItemID(), rli.getQuantity());
        }
        currentReturn.complete();
    }

    public Return getCurrentReturn() {
        return currentReturn;
    }

    private void requireSale() {
        if (currentSale == null) {
            throw new IllegalStateException("No sale in progress. Call makeNewSale() first.");
        }
    }

    private void requireReturn() {
        if (currentReturn == null) {
            throw new IllegalStateException("No return in progress. Call makeNewReturn() first.");
        }
    }
}
