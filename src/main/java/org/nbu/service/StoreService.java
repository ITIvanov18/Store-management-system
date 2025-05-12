package org.nbu.service;

import jakarta.persistence.EntityNotFoundException;
import org.nbu.data.Store;
import org.nbu.repository.StoreRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreService {

    private final StoreRepo storeRepository;

    public StoreService(StoreRepo storeRepository) {
        this.storeRepository = storeRepository;
    }

    public void save(Store store) {
        storeRepository.save(store);
    }

    public List<Store> findAll() {
        return storeRepository.findAll();
    }

    public Store findById(int storeId) {
        return storeRepository.findById(storeId).orElseThrow(() -> new EntityNotFoundException("Магазинът не е намерен"));
    }
}
