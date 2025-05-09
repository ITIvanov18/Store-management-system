package org.nbu.data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class StoreStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private double totalTurnover;
    private int issuedReceipts;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTotalTurnover() {
        return totalTurnover;
    }

    public void setTotalTurnover(double totalTurnover) {
        this.totalTurnover = totalTurnover;
    }

    public int getIssuedReceipts() {
        return issuedReceipts;
    }

    public void setIssuedReceipts(int issuedReceipts) {
        this.issuedReceipts = issuedReceipts;
    }
}
