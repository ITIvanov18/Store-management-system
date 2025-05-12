package org.nbu.service;

import org.nbu.data.CashRegister;
import org.nbu.repository.CashRegisterRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CashRegisterService {

    private final CashRegisterRepo cashRegisterRepo;

    public CashRegisterService(CashRegisterRepo cashRegisterRepo) {
        this.cashRegisterRepo = cashRegisterRepo;
    }

    public void save(CashRegister cashRegister) {
        cashRegisterRepo.save(cashRegister);
    }

    public List<CashRegister> findAll() {
        return cashRegisterRepo.findAll();
    }

    public CashRegister findById(int id) {
        return cashRegisterRepo.findById(id).orElse(null);
    }

    public CashRegister findByCashierId(int cashierId) {
        for (CashRegister register : cashRegisterRepo.findAll()) {
            if (register.getCashier() != null && register.getCashier().getId() == cashierId) {
                return register;
            }
        }
        return null;
    }

    public List<CashRegister> findByStoreId(int storeId) {
        return cashRegisterRepo.findAll().stream()
                .filter(r -> r.getStore() != null && r.getStore().getId() == storeId)
                .toList();
    }

    public void removeCashier(int registerId) {
        CashRegister register = findById(registerId);
        if (register != null) {
            register.setCashier(null);
            cashRegisterRepo.save(register);
        }
    }
}
