package org.nbu.repository;

import org.nbu.data.CashRegister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CashRegisterRepo extends JpaRepository<CashRegister, Integer> {
}
