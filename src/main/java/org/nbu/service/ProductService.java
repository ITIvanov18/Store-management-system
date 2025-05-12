package org.nbu.service;

import org.nbu.data.Product;
import org.nbu.data.ProductCategoryEnum;
import org.nbu.data.StoreSettings;
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
        productRepository.save(product);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<Product> findByStoreId(int storeId) {
        return productRepository.findByStoreId(storeId);
    }

    public BigDecimal calculateSellingPrice(Product product, StoreSettings settings) {
        BigDecimal markup;

        if (product.getCategory() == ProductCategoryEnum.FOOD) {
            markup = BigDecimal.valueOf(settings.getFoodMarkupPercentage()).divide(BigDecimal.valueOf(100));
        } else {
            markup = BigDecimal.valueOf(settings.getNonFoodMarkupPercentage()).divide(BigDecimal.valueOf(100));
        }

        BigDecimal deliveryPrice = BigDecimal.valueOf(product.getDeliveryPrice());
        BigDecimal priceWithMarkup = deliveryPrice.add(deliveryPrice.multiply(markup));

        if (product.isHasExpirationDate() && product.getExpirationDate() != null) {
            long daysToExpire = ChronoUnit.DAYS.between(LocalDate.now(), product.getExpirationDate());
            if (daysToExpire >= 0 && daysToExpire <= settings.getDaysBeforeExpirationForDiscount()) {
                BigDecimal discount = BigDecimal.valueOf(settings.getDiscountPercentage()).divide(BigDecimal.valueOf(100));
                return priceWithMarkup.subtract(priceWithMarkup.multiply(discount)).setScale(2, RoundingMode.HALF_UP);
            }
        }

        return priceWithMarkup.setScale(2, RoundingMode.HALF_UP);
    }
}
