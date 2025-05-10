package org.nbu.data;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Receipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDateTime receiptIssueDate;

    @ManyToOne
    private Cashier cashier;

    @OneToMany(mappedBy = "receipt", cascade = CascadeType.ALL)
    private List<SoldProduct> totalSoldProducts = new ArrayList<>();

    private double totalPrice;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getReceiptIssueDate() {
        return receiptIssueDate;
    }

    public void setReceiptIssueDate(LocalDateTime receiptIssueDate) {
        this.receiptIssueDate = receiptIssueDate;
    }

    public List<SoldProduct> getTotalSoldProducts() {
        return totalSoldProducts;
    }

    public void setTotalSoldProducts(List<SoldProduct> totalSoldProducts) {
        this.totalSoldProducts = totalSoldProducts;
    }

    public Cashier getCashier() {
        return cashier;
    }

    public void setCashier(Cashier cashier) {
        this.cashier = cashier;
    }

    public double getTotalPrice() {
        return totalSoldProducts.stream()
                .mapToDouble(p -> p.getSellingPrice() * p.getQuantity())
                .sum();
    }
}
