package org.nbu.service;

import org.junit.jupiter.api.Test;
import org.nbu.data.*;
import org.nbu.repository.ReceiptRepo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class ReceiptServiceTest {

    @Test
    public void testGenerateReceipt_CreatesCorrectReceipt() {
        ReceiptRepo receiptRepo = mock(ReceiptRepo.class);
        ReceiptService receiptService = new ReceiptService(receiptRepo);
        receiptService.setSkipFileWriting(true);

        Product product1 = new Product();
        product1.setId(1);
        product1.setProductName("Milk");
        product1.setDeliveryPrice(1.00);
        product1.setCategory(ProductCategoryEnum.FOOD);
        product1.setQuantity(10);
        product1.setHasExpirationDate(true);
        product1.setExpirationDate(LocalDate.now().plusDays(3));

        Product product2 = new Product();
        product2.setId(2);
        product2.setProductName("Shampoo");
        product2.setDeliveryPrice(3.00);
        product2.setCategory(ProductCategoryEnum.NON_FOOD);
        product2.setQuantity(5);
        product2.setHasExpirationDate(false);

        Map<Product, Integer> products = new HashMap<>();
        products.put(product1, 2);
        products.put(product2, 1);

        Map<Integer, BigDecimal> prices = new HashMap<>();
        prices.put(1, new BigDecimal("2.50"));
        prices.put(2, new BigDecimal("5.00"));

        Cashier cashier = new Cashier();
        cashier.setCashierName("Ivan");

        Store store = new Store();
        store.setStoreName("Test Store");

        Receipt receipt = receiptService.generateReceipt(store, products, prices, cashier);

        assertNotNull(receipt);
        assertEquals(2, receipt.getTotalSoldProducts().size());
        assertEquals("Ivan", receipt.getCashier().getCashierName());
        assertEquals(2 * 2.50 + 1 * 5.00, receipt.getTotalAmount(), 0.001);
    }
}