package org.nbu.util;

import org.nbu.data.Cashier;
import org.nbu.data.Product;
import org.nbu.data.Receipt;
import org.nbu.data.Store;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class ReceiptFileWriter {

    public static void writeReceiptToFile(Receipt receipt,
                                          Map<Product, Integer> purchased,
                                          Map<Integer, BigDecimal> prices) {

        String fileName = "receipt-" + receipt.getId() + ".txt";

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("КАСОВА БЕЛЕЖКА №" + receipt.getId() + "\n");
            writer.write("Дата: " + receipt.getReceiptIssueDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n");

            Cashier cashier = receipt.getCashier();
            Store store = cashier.getStore();
            writer.write("Магазин: " + store.getStoreName() + "\n");
            writer.write("Касиер: " + cashier.getCashierName() + "\n\n");

            writer.write(String.format("%-20s %-10s %-10s %-10s\n", "Продукт", "Цена", "Бр.", "Общо"));
            writer.write("--------------------------------------------------\n");

            BigDecimal total = BigDecimal.ZERO;
            for (Map.Entry<Product, Integer> entry : purchased.entrySet()) {
                Product product = entry.getKey();
                int qty = entry.getValue();
                BigDecimal price = prices.get(product.getId());
                if (price == null) {
                    throw new IllegalArgumentException("Не е намерена цена за продукт: " + product.getProductName());
                }
                BigDecimal sum = price.multiply(BigDecimal.valueOf(qty));
                total = total.add(sum);

                writer.write(String.format("%-20s %-10.2f %-10d %-10.2f\n",
                        product.getProductName(), price, qty, sum));
            }

            writer.write("--------------------------------------------------\n");
            writer.write("ОБЩА СУМА: " + total.setScale(2) + " лв.\n");

        } catch (IOException e) {
            throw new RuntimeException("Грешка при запис на касовата бележка в .txt файл", e);
        }
    }
}
