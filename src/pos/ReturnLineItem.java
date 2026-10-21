package pos;

/**
 * One line of a return: a product specification and the quantity returned.
 * Information Expert: knows its own refund subtotal.
 */
public class ReturnLineItem {

    private final ProductSpecification spec;
    private final int quantity;

    public ReturnLineItem(ProductSpecification spec, int quantity) {
        this.spec = spec;
        this.quantity = quantity;
    }

    public double getSubtotal() {
        return spec.getPrice() * quantity;
    }

    public ProductSpecification getSpecification() {
        return spec;
    }

    public int getQuantity() {
        return quantity;
    }
}
