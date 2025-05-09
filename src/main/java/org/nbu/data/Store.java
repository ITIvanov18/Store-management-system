package org.nbu.data;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String storeName;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "store_id")
    private List<CashRegister> cashRegisters = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "store_id")
    private List<Product> products = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "store_id")
    private List<Cashier> cashiers = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "storeSettings_id")
    private StoreSettings storeSettings;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "storeStatistics_id")
    private StoreStatistics storeStatistics;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public List<CashRegister> getCashRegisters() {
        return cashRegisters;
    }

    public void setCashRegisters(List<CashRegister> cashRegisters) {
        this.cashRegisters = cashRegisters;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Cashier> getCashiers() {
        return cashiers;
    }

    public void setCashiers(List<Cashier> cashiers) {
        this.cashiers = cashiers;
    }

    public StoreSettings getStoreSettings() {
        return storeSettings;
    }

    public void setStoreSettings(StoreSettings storeSettings) {
        this.storeSettings = storeSettings;
    }

    public StoreStatistics getStoreStatistics() {
        return storeStatistics;
    }

    public void setStoreStatistics(StoreStatistics storeStatistics) {
        this.storeStatistics = storeStatistics;
    }
}
