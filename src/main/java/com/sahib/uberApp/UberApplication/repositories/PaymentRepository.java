package com.sahib.uberApp.UberApplication.repositories;

import com.sahib.uberApp.UberApplication.entities.Payment;
import com.sahib.uberApp.UberApplication.entities.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByRide(Ride ride);

}
