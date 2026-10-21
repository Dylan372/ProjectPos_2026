package pos;

/**
 * Describes a product type (itemID, description, price) independent of the
 * physical items in stock — a "Description Class" (Applying UML and Patterns, ch. 9).
 */
public class ProductSpecification {

    private final int itemID;
    private final String description;
    private final double price;

    public ProductSpecification(int itemID, String description, double price) {
        this.itemID = itemID;
        this.description = description;
        this.price = price;
    }

    public int getItemID() {
        return itemID;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return String.format("%04d  %-22s %8.2f", itemID, description, price);
    }
}
