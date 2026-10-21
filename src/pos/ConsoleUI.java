package pos;

import java.util.Scanner;

/**
 * Presentation layer: a simple text-based user interface (Model-View
 * Separation — this class only displays data and forwards input to the
 * domain layer via the Register controller; it contains no business logic).
 */
public class ConsoleUI {

    private final Store store;
    private final Register register;
    private final Scanner in = new Scanner(System.in);

    public ConsoleUI(Store store, Register register) {
        this.store = store;
        this.register = register;
    }

    public void run() {
        System.out.println();
        System.out.println("==================================================");
        System.out.println("     Welcome to " + store.getName());
        System.out.println("     " + store.getAddress());
        System.out.println("     PoS System - Software Engineering Practice");
        System.out.println("==================================================");
        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("[1] New Sale        [2] Handle Returns");
            System.out.println("[3] Show Catalog    [4] Show Inventory");
            System.out.println("[5] Run Demo        [0] Exit");
            System.out.print("Choice: ");
            String choice = in.nextLine().trim();
            System.out.println();
            switch (choice) {
                case "1" -> processSale(false);
                case "2" -> processReturn(false);
                case "3" -> showCatalog();
                case "4" -> showInventory();
                case "5" -> runDemo();
                case "0" -> running = false;
                default -> System.out.println("Invalid choice, please try again.");
            }
        }
        saveInventory();
        System.out.println("Goodbye!");
    }

    /** Handles the Process Sale use case step by step (interactive or scripted). */
    public void processSale(boolean scripted) {
        try {
            register.makeNewSale();
            System.out.println(">> Starting a new sale...");
            while (true) {
                System.out.print("Enter item ID (0 to finish): ");
                String id = scripted ? readScripted() : in.nextLine().trim();
                if (id.equals("0")) {
                    break;
                }
                System.out.print("Enter quantity: ");
                String qty = scripted ? readScripted() : in.nextLine().trim();
                register.enterItem(Integer.parseInt(id), Integer.parseInt(qty));
                ProductSpecification spec = store.getSpecification(Integer.parseInt(id));
                System.out.printf("   Added: %s x %s = %.2f | running total = %.2f%n",
                        qty, spec.getDescription(), spec.getPrice() * Integer.parseInt(qty),
                        register.getCurrentSale().getTotal());
            }
            double total = register.endSale();
            System.out.printf(">> Sale total: %.2f%n", total);
            System.out.print("Cash tendered: ");
            String tendered = scripted ? readScripted() : in.nextLine().trim();
            double change = register.makePayment(Double.parseDouble(tendered));
            System.out.printf(">> Change due: %.2f%n", change);
            System.out.print(Receipt.forSale(store, register.getCurrentSale(),
                    Double.parseDouble(tendered)));
            System.out.println(">> Inventory updated.");
        } catch (Exception e) {
            System.out.println("!! " + e.getMessage());
            System.out.println(">> Sale cancelled, inventory unchanged.");
        }
    }

    /** Handles the Handle Returns use case step by step (interactive or scripted). */
    public void processReturn(boolean scripted) {
        try {
            register.makeNewReturn();
            System.out.println(">> Starting a new return...");
            while (true) {
                System.out.print("Enter returned item ID (0 to finish): ");
                String id = scripted ? readScripted() : in.nextLine().trim();
                if (id.equals("0")) {
                    break;
                }
                System.out.print("Enter quantity: ");
                String qty = scripted ? readScripted() : in.nextLine().trim();
                register.enterReturnedItem(Integer.parseInt(id), Integer.parseInt(qty));
                ProductSpecification spec = store.getSpecification(Integer.parseInt(id));
                System.out.printf("   Returned: %s x %s = %.2f | refund total = %.2f%n",
                        qty, spec.getDescription(), spec.getPrice() * Integer.parseInt(qty),
                        register.getCurrentReturn().getRefundTotal());
            }
            double refundTotal = register.endReturn();
            register.receiveRefund(refundTotal);
            System.out.printf(">> Refund total: %.2f%n", refundTotal);
            System.out.print(Receipt.forReturn(store, register.getCurrentReturn(), refundTotal));
            System.out.println(">> Inventory restocked.");
        } catch (Exception e) {
            System.out.println("!! " + e.getMessage());
            System.out.println(">> Return cancelled, inventory unchanged.");
        }
    }

    public void showCatalog() {
        System.out.println("----- Product Catalog -----");
        for (ProductSpecification spec : store.getCatalog().getAll()) {
            System.out.println("  " + spec);
        }
    }

    public void showInventory() {
        System.out.println("----- Inventory -----");
        for (ProductSpecification spec : store.getCatalog().getAll()) {
            System.out.printf("  %04d  %-22s stock: %d%n",
                    spec.getItemID(), spec.getDescription(),
                    store.getInventory().getStock(spec.getItemID()));
        }
    }

    /** Scripted end-to-end demonstration — ideal for the demo video. */
    public void runDemo() {
        System.out.println("===== DEMO: Process Sale =====");
        processSale(true);
        System.out.println();
        System.out.println("===== DEMO: Handle Returns =====");
        processReturn(true);
        System.out.println();
        System.out.println("===== Inventory after demo =====");
        showInventory();
    }

    // ---------- scripted input queue (used by runDemo) ----------
    private final java.util.Queue<String> script = new java.util.ArrayDeque<>();
    private boolean scriptActive = false;

    /** Feeds a sequence of answers for the scripted demo. */
    public void setScript(String... inputs) {
        script.clear();
        script.addAll(java.util.List.of(inputs));
        scriptActive = true;
    }

    private String readScripted() {
        if (!scriptActive || script.isEmpty()) {
            return "0";
        }
        String v = script.poll();
        System.out.println(v);
        return v;
    }

    private void saveInventory() {
        store.getInventory().save(PathsUtil.inventoryPath());
    }
}
