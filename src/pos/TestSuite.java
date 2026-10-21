package pos;

/**
 * Simple automated test suite (plain assertions, no external libraries) —
 * inspired by the Test-Driven Development practice (book ch. 21).
 * Run: java pos.TestSuite
 */
public class TestSuite {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        testSaleTotalIsSumOfLineItemSubtotals();
        testChangeIsTenderedMinusTotal();
        testPaymentUpdatesInventory();
        testReturnRestocksInventory();
        testInsufficientStockRejected();
        testInsufficientPaymentRejected();
        testUnknownItemRejected();
        testProductCatalogLoading();

        System.out.println();
        System.out.printf("Tests passed: %d, failed: %d%n", passed, failed);
        if (failed > 0) {
            System.exit(1);
        }
    }

    private static Store newTestStore() {
        ProductCatalog catalog = new ProductCatalog();
        catalog.add(new ProductSpecification(1001, "Coca-Cola 330ml", 3.50));
        catalog.add(new ProductSpecification(1004, "Instant Noodles", 4.50));
        Inventory inventory = new Inventory();
        inventory.restock(1001, 50);
        inventory.restock(1004, 10);
        return new Store("Test Store", "Test Address", catalog, inventory);
    }

    private static void check(String name, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("  [PASS] " + name);
        } else {
            failed++;
            System.out.println("  [FAIL] " + name);
        }
    }

    private static void testSaleTotalIsSumOfLineItemSubtotals() {
        System.out.println("Sale total calculation");
        Store store = newTestStore();
        Register r = new Register(store);
        r.makeNewSale();
        r.enterItem(1001, 2);   // 2 x 3.50 = 7.00
        r.enterItem(1004, 1);   // 1 x 4.50 = 4.50
        check("total == 11.50", Math.abs(r.endSale() - 11.50) < 0.001);
    }

    private static void testChangeIsTenderedMinusTotal() {
        System.out.println("Change calculation");
        Store store = newTestStore();
        Register r = new Register(store);
        r.makeNewSale();
        r.enterItem(1001, 1);   // 3.50
        r.endSale();
        check("change == 96.50 for 100 tendered", Math.abs(r.makePayment(100) - 96.50) < 0.001);
    }

    private static void testPaymentUpdatesInventory() {
        System.out.println("Inventory updated after payment");
        Store store = newTestStore();
        Register r = new Register(store);
        r.makeNewSale();
        r.enterItem(1001, 3);
        r.endSale();
        r.makePayment(20);
        check("stock of 1001 reduced from 50 to 47", store.getStock(1001) == 47);
    }

    private static void testReturnRestocksInventory() {
        System.out.println("Return restocks inventory");
        Store store = newTestStore();
        Register r = new Register(store);
        r.makeNewReturn();
        r.enterReturnedItem(1004, 2);
        double refund = r.endReturn();
        r.receiveRefund(refund);
        check("refund total == 9.00", Math.abs(refund - 9.00) < 0.001);
        check("stock of 1004 increased from 10 to 12", store.getStock(1004) == 12);
    }

    private static void testInsufficientStockRejected() {
        System.out.println("Insufficient stock rejected");
        Store store = newTestStore();
        Register r = new Register(store);
        r.makeNewSale();
        boolean thrown = false;
        try {
            r.enterItem(1004, 99);
        } catch (IllegalStateException e) {
            thrown = true;
        }
        check("IllegalStateException thrown", thrown);
        check("stock unchanged", store.getStock(1004) == 10);
    }

    private static void testInsufficientPaymentRejected() {
        System.out.println("Insufficient payment rejected");
        Store store = newTestStore();
        Register r = new Register(store);
        r.makeNewSale();
        r.enterItem(1001, 10);   // 35.00
        r.endSale();
        boolean thrown = false;
        try {
            r.makePayment(20);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        check("IllegalArgumentException thrown", thrown);
    }

    private static void testUnknownItemRejected() {
        System.out.println("Unknown item rejected");
        Store store = newTestStore();
        Register r = new Register(store);
        r.makeNewSale();
        boolean thrown = false;
        try {
            r.enterItem(9999, 1);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        check("IllegalArgumentException thrown", thrown);
    }

    private static void testProductCatalogLoading() {
        System.out.println("Product catalog loading");
        ProductCatalog catalog = ProductCatalog.loadFromFile(PathsUtil.productsPath());
        check("catalog contains item 1001",
                catalog.getSpecification(1001) != null
                        && "Coca-Cola 330ml".equals(catalog.getSpecification(1001).getDescription()));
    }
}
