package pos;

import java.time.format.DateTimeFormatter;

/**
 * Builds the text of the printed receipts for sales and returns
 * (part of the Infrastructure / presentation concern; kept as plain text).
 */
public final class Receipt {

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String LINE =
            "------------------------------------------------";

    private Receipt() {
    }

    public static String forSale(Store store, Sale sale, double tendered) {
        StringBuilder sb = new StringBuilder();
        sb.append('\n').append(LINE).append('\n');
        sb.append(center(store.getName())).append('\n');
        sb.append(center(store.getAddress())).append('\n');
        sb.append(LINE).append('\n');
        sb.append("SALE RECEIPT\n");
        sb.append("Date: ").append(sale.getDateTime().format(FMT)).append('\n');
        sb.append(LINE).append('\n');
        sb.append(String.format("%-6s %-22s %4s %8s", "ID", "Item", "Qty", "Subtotal")).append('\n');
        for (SalesLineItem sli : sale.getLineItems()) {
            sb.append(String.format("%-6d %-22s %4d %8.2f",
                    sli.getSpecification().getItemID(),
                    sli.getSpecification().getDescription(),
                    sli.getQuantity(),
                    sli.getSubtotal())).append('\n');
        }
        sb.append(LINE).append('\n');
        sb.append(String.format("TOTAL: %34.2f", sale.getTotal())).append('\n');
        sb.append(String.format("CASH TENDERED: %27.2f", tendered)).append('\n');
        sb.append(String.format("CHANGE: %33.2f", sale.getChange())).append('\n');
        sb.append(LINE).append('\n');
        sb.append(center("Thank you for shopping with us!")).append('\n');
        sb.append(LINE).append('\n');
        return sb.toString();
    }

    public static String forReturn(Store store, Return ret, double refunded) {
        StringBuilder sb = new StringBuilder();
        sb.append('\n').append(LINE).append('\n');
        sb.append(center(store.getName())).append('\n');
        sb.append(center(store.getAddress())).append('\n');
        sb.append(LINE).append('\n');
        sb.append("RETURN RECEIPT\n");
        sb.append("Date: ").append(ret.getDateTime().format(FMT)).append('\n');
        sb.append(LINE).append('\n');
        sb.append(String.format("%-6s %-22s %4s %8s", "ID", "Item", "Qty", "Refund")).append('\n');
        for (ReturnLineItem rli : ret.getLineItems()) {
            sb.append(String.format("%-6d %-22s %4d %8.2f",
                    rli.getSpecification().getItemID(),
                    rli.getSpecification().getDescription(),
                    rli.getQuantity(),
                    rli.getSubtotal())).append('\n');
        }
        sb.append(LINE).append('\n');
        sb.append(String.format("TOTAL REFUND: %28.2f", ret.getRefundTotal())).append('\n');
        sb.append(LINE).append('\n');
        sb.append(center("Items have been restocked.")).append('\n');
        sb.append(LINE).append('\n');
        return sb.toString();
    }

    private static String center(String text) {
        int width = LINE.length();
        if (text.length() >= width) {
            return text;
        }
        int pad = (width - text.length()) / 2;
        return " ".repeat(pad) + text;
    }
}
