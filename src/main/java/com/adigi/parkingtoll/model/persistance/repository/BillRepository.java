package com.adigi.parkingtoll.model.persistance.repository;

import com.adigi.parkingtoll.model.persistance.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill, Long> {
}
