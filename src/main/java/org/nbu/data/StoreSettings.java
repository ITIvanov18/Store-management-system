package org.nbu.data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class StoreSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private double foodMarkupPercentage;
    private double nonFoodMarkupPercentage;
    private int daysBeforeExpirationForDiscount;
    private double discountPercentage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getFoodMarkupPercentage() {
        return foodMarkupPercentage;
    }

    public void setFoodMarkupPercentage(double foodMarkupPercentage) {
        this.foodMarkupPercentage = foodMarkupPercentage;
    }

    public double getNonFoodMarkupPercentage() {
        return nonFoodMarkupPercentage;
    }

    public void setNonFoodMarkupPercentage(double nonFoodMarkupPercentage) {
        this.nonFoodMarkupPercentage = nonFoodMarkupPercentage;
    }

    public int getDaysBeforeExpirationForDiscount() {
        return daysBeforeExpirationForDiscount;
    }

    public void setDaysBeforeExpirationForDiscount(int daysBeforeExpirationForDiscount) {
        this.daysBeforeExpirationForDiscount = daysBeforeExpirationForDiscount;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
}
