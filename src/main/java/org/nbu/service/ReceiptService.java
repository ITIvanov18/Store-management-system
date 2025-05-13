package org.nbu.service;

import org.nbu.data.Receipt;
import org.nbu.repository.ReceiptRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReceiptService {
    private final ReceiptRepo receiptRepository;

    public ReceiptService(ReceiptRepo receiptRepository) {
        this.receiptRepository = receiptRepository;
    }

    public Receipt save(Receipt receipt) {
        return receiptRepository.save(receipt);
    }

    public List<Receipt> findAll() {
        return receiptRepository.findAll();
    }

    public long countReceipts() {
        return receiptRepository.count();
    }

    public double getTotalTurnover() {
        return receiptRepository.findAll().stream()
                .mapToDouble(Receipt::getTotalPrice)
                .sum();
    }

    public double getTotalTurnoverByStoreId(int storeId) {
        return receiptRepository.findAll().stream()
                .filter(r -> r.getCashier() != null &&
                        r.getCashier().getStore() != null &&
                        r.getCashier().getStore().getId() == storeId)
                .mapToDouble(Receipt::getTotalPrice)
                .sum();
    }

    public long countReceiptsByStoreId(int storeId) {
        return receiptRepository.findAll().stream()
                .filter(r -> r.getCashier() != null &&
                        r.getCashier().getStore() != null &&
                        r.getCashier().getStore().getId() == storeId)
                .count();
    }
}
