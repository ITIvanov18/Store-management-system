package org.nbu.service;

import org.nbu.data.Product;
import org.nbu.data.ProductCategoryEnum;
import org.nbu.data.StoreSettings;
import org.nbu.exceptions.InvalidProductCategoryException;
import org.nbu.repository.ProductRepo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepo productRepository;

    public ProductService(ProductRepo productRepository) {
        this.productRepository = productRepository;
    }

    public void save(Product product) {
        if (product.getCategory() == ProductCategoryEnum.NON_FOOD && product.isHasExpirationDate()) {
            throw new InvalidProductCategoryException();
        }

        productRepository.save(product);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(int productId) {
        return productRepository.findById(productId).orElse(null);
    }

    public List<Product> findByStoreId(int storeId) {
        return productRepository.findAll().stream()
                .filter(p -> p.getStore() != null && p.getStore().getId() == storeId)
                .toList();
    }


    public BigDecimal calculateSellingPrice(Product product, StoreSettings settings) {
        // 1. Доставна цена
        BigDecimal deliveryPrice = BigDecimal.valueOf(product.getDeliveryPrice());

        // 2. Надценка: 20% от доставната цена
        BigDecimal markup = deliveryPrice.multiply(BigDecimal.valueOf(0.20));

        // 3. Цена с надценка
        BigDecimal priceWithMarkup = deliveryPrice.add(markup);

        // 4. ДДС: 20% от цена с надценка
        BigDecimal vat = priceWithMarkup.multiply(BigDecimal.valueOf(0.20));

        // 5. Крайна цена с ДДС
        BigDecimal finalPrice = priceWithMarkup.add(vat);

        // 6. Проверка за отстъпка (само за хранителни продукти с изтичащ срок)
        if (product.getCategory() == ProductCategoryEnum.FOOD
                && product.isHasExpirationDate()
                && product.getExpirationDate() != null) {

            long daysToExpire = ChronoUnit.DAYS.between(LocalDate.now(), product.getExpirationDate());

            if (daysToExpire >= 0 && daysToExpire <= 5) {
                // 30% отстъпка върху крайната цена, ако daysToExpire<=5
                BigDecimal discount = finalPrice.multiply(BigDecimal.valueOf(0.30));
                finalPrice = finalPrice.subtract(discount);
            }
        }

        return finalPrice.setScale(2, RoundingMode.HALF_UP);
    }
}
