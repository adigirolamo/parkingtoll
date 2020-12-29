package com.adigi.parkingtoll.model.persistence.repository;

import com.adigi.parkingtoll.model.persistence.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
