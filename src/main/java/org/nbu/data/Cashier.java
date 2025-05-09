    package org.nbu.data;

    import jakarta.persistence.*;

    @Entity
    @Table(name = "Cashier")
    public class Cashier {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        private String cashierName;

        private double monthlySalary;

        @ManyToOne
        @JoinColumn(name = "store_id")
        private Store store;

        @OneToOne(mappedBy = "cashier")
        private CashRegister cashRegister;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCashierName() {
            return cashierName;
        }

        public void setCashierName(String cashierName) {
            this.cashierName = cashierName;
        }

        public double getMonthlySalary() {
            return monthlySalary;
        }

        public void setMonthlySalary(double monthlySalary) {
            this.monthlySalary = monthlySalary;
        }

        public Store getStore() {
            return store;
        }

        public void setStore(Store store) {
            this.store = store;
        }

        public CashRegister getCashRegister() {
            return cashRegister;
        }

        public void setCashRegister(CashRegister cashRegister) {
            this.cashRegister = cashRegister;
        }
    }
