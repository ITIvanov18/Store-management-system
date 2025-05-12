package org.nbu.service;

import org.nbu.data.Cashier;
import org.nbu.repository.CashierRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CashierService {

    private final CashierRepo cashierRepository;

    public CashierService(CashierRepo cashierRepository) {
        this.cashierRepository = cashierRepository;
    }

    public Cashier save(Cashier cashier) {
        return cashierRepository.save(cashier);
    }

    public List<Cashier> findByStoreIdWhenCashRegisterIsNull(int storeId) {
        return cashierRepository.findByStoreIdWhenCashRegisterIsNull(storeId);
    }

    public Cashier findById(int id) {
        return cashierRepository.findById(id).orElse(null);
    }
}
