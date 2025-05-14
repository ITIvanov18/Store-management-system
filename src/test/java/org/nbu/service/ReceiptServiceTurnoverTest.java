package org.nbu.service;

import org.junit.jupiter.api.Test;
import org.nbu.data.*;
import org.nbu.repository.ReceiptRepo;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ReceiptServiceTurnoverTest {

    @Test
    public void testGetTotalTurnoverByStoreId() {
        // Mock dependencies
        ReceiptRepo receiptRepo = mock(ReceiptRepo.class);
        ReceiptService receiptService = new ReceiptService(receiptRepo);

        // Създаване на магазини 1 и 2
        Store store1 = new Store();
        store1.setId(1);
        Store store2 = new Store();
        store2.setId(2);

        // Създаване на касиер за магазин 1
        Cashier cashier1 = new Cashier();
        cashier1.setStore(store1);

        // касиер за магазин 2
        Cashier cashier2 = new Cashier();
        cashier2.setStore(store2);

        // Касова бележка за магазин 1 с продадени продукти
        Receipt receipt1 = new Receipt();
        receipt1.setCashier(cashier1);
        SoldProduct sold1 = new SoldProduct();
        sold1.setSellingPrice(50.0);
        sold1.setQuantity(2);
        sold1.setReceipt(receipt1);
        sold1.setProduct(new Product());
        receipt1.setTotalSoldProducts(List.of(sold1));

        // Касова бележка за магазин 2 с продадени продукти
        Receipt receipt2 = new Receipt();
        receipt2.setCashier(cashier2);
        SoldProduct sold2 = new SoldProduct();
        sold2.setSellingPrice(100.0);
        sold2.setQuantity(2);
        sold2.setReceipt(receipt2);
        sold2.setProduct(new Product());
        receipt2.setTotalSoldProducts(List.of(sold2));

        // Касова бележка без касиер (ще бъде игнорирана)
        Receipt receipt3 = new Receipt();
        receipt3.setCashier(null);
        receipt3.setTotalSoldProducts(List.of());

        when(receiptRepo.findAll()).thenReturn(List.of(receipt1, receipt2, receipt3));

        // Проверка на оборота на магазин 1 (50 * 2 = 100)
        double turnover = receiptService.getTotalTurnoverByStoreId(1);

        assertEquals(100.0, turnover, 0.001);
    }
}