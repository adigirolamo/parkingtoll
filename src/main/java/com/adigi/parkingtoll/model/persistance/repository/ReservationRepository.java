package com.adigi.parkingtoll.model.persistance.repository;

import com.adigi.parkingtoll.model.persistance.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
