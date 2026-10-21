package pos;

/**
 * One line of a sale: a product specification and the quantity sold.
 * Information Expert: knows its own subtotal.
 */
public class SalesLineItem {

    private final ProductSpecification spec;
    private final int quantity;

    public SalesLineItem(ProductSpecification spec, int quantity) {
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
