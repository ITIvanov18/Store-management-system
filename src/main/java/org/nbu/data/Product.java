package org.nbu.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;


@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String productName;

    private double deliveryPrice;

    private int quantity;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ProductCategoryEnum category;

    @Column(nullable = false)
    private boolean hasExpirationDate;

    private LocalDate expirationDate;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(double deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ProductCategoryEnum getCategory() {
        return category;
    }

    public void setCategory(ProductCategoryEnum category) {
        this.category = category;
    }

    public boolean isHasExpirationDate() {
        return hasExpirationDate;
    }

    public void setHasExpirationDate(boolean hasExpirationDate) {
        this.hasExpirationDate = hasExpirationDate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }
}
