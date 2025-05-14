package org.nbu.service;

import org.junit.jupiter.api.Test;
import org.nbu.data.Product;
import org.nbu.data.ProductCategoryEnum;
import org.nbu.data.StoreSettings;
import org.nbu.exceptions.InvalidProductCategoryException;
import org.nbu.repository.ProductRepo;

import java.time.LocalDate;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Test
    public void testCalculateSellingPrice_WithDiscount() {
        ProductRepo productRepo = mock(ProductRepo.class);
        ProductService productService = new ProductService(productRepo);

        Product product = new Product();
        product.setDeliveryPrice(10.00);
        product.setCategory(ProductCategoryEnum.FOOD);
        product.setHasExpirationDate(true);
        product.setExpirationDate(LocalDate.now().plusDays(3));

        StoreSettings settings = new StoreSettings();
        settings.setFoodMarkupPercentage(20);
        settings.setNonFoodMarkupPercentage(10);
        settings.setDaysBeforeExpirationForDiscount(5);
        settings.setDiscountPercentage(30);

        BigDecimal finalPrice = productService.calculateSellingPrice(product, settings);

        assertEquals(new BigDecimal("10.08"), finalPrice);
    }

    @Test
    public void testSave_ThrowsExceptionForNonFoodWithExpirationDate() {
        ProductRepo productRepo = mock(ProductRepo.class);
        ProductService productService = new ProductService(productRepo);

        Product invalidProduct = new Product();
        invalidProduct.setCategory(ProductCategoryEnum.NON_FOOD);
        invalidProduct.setHasExpirationDate(true);

        assertThrows(InvalidProductCategoryException.class, () -> {
            productService.save(invalidProduct);
        });
    }
}