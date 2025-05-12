package org.nbu.repository;

import org.nbu.data.Cashier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CashierRepo extends JpaRepository<Cashier, Integer> {
    @Query("SELECT c FROM Cashier c WHERE c.store.id = :storeId AND c NOT IN (SELECT cr.cashier FROM CashRegister cr WHERE cr.cashier IS NOT NULL)")
    List<Cashier> findByStoreIdWhenCashRegisterIsNull (@Param("storeId") int storeId);
}
