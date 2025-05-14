package org.nbu.service;

import org.nbu.data.*;
import org.nbu.repository.ReceiptRepo;
import org.nbu.util.ReceiptFileWriter;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ReceiptService {

    private final ReceiptRepo receiptRepository;

    public ReceiptService(ReceiptRepo receiptRepository) {
        this.receiptRepository = receiptRepository;
    }

    public Receipt save(Receipt receipt) {
        return receiptRepository.save(receipt);
    }

    public List<Receipt> findAll() {
        return receiptRepository.findAll();
    }

    public long countReceipts() {
        return receiptRepository.count();
    }

    public double getTotalTurnover() {
        return receiptRepository.findAll().stream()
                .mapToDouble(Receipt::calculateTotalPrice)
                .sum();
    }

    public double getTotalTurnoverByStoreId(int storeId) {
        return receiptRepository.findAll().stream()
                .filter(r -> r.getCashier() != null &&
                        r.getCashier().getStore() != null &&
                        r.getCashier().getStore().getId() == storeId)
                .mapToDouble(Receipt::calculateTotalPrice)
                .sum();
    }

    public long countReceiptsByStoreId(int storeId) {
        return receiptRepository.findAll().stream()
                .filter(r -> r.getCashier() != null &&
                        r.getCashier().getStore() != null &&
                        r.getCashier().getStore().getId() == storeId)
                .count();
    }

    /**
     * Генерира касова бележка:
     * - Създава Receipt обект
     * - Присвоява му продадените продукти
     * - Записва в базата
     * - Създава .txt файл
     */
    public Receipt generateReceipt(Store store,
                                   Map<Product, Integer> products,
                                   Map<Integer, BigDecimal> prices,
                                   Cashier cashier) {

        Receipt receipt = new Receipt();
        receipt.setReceiptIssueDate(LocalDateTime.now());
        receipt.setCashier(cashier);

        double totalAmount = 0;
        List<SoldProduct> sold = new ArrayList<>();

        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            Product product = entry.getKey();
            int qty = entry.getValue();
            BigDecimal price = prices.get(product.getId());

            SoldProduct soldProduct = new SoldProduct();
            soldProduct.setProduct(product);
            soldProduct.setQuantity(qty);
            soldProduct.setSellingPrice(price.doubleValue());
            soldProduct.setReceipt(receipt);

            sold.add(soldProduct);
            totalAmount += price.doubleValue() * qty;
        }

        receipt.setTotalAmount(totalAmount);
        receipt.setTotalSoldProducts(sold);

        ReceiptFileWriter.writeReceiptToFile(receipt, products, prices);

        return receipt;
    }


}
