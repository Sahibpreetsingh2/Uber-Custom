package com.sahib.uberApp.UberApplication.repositories;

import com.sahib.uberApp.UberApplication.entities.Driver;
import com.sahib.uberApp.UberApplication.entities.Ride;
import com.sahib.uberApp.UberApplication.entities.Rider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RideRepository extends JpaRepository<Ride , Long> {

//    Page<Ride> getAllRidesOfDriver(Long driverId, PageRequest pageRequest);

    @Query("SELECT r from Ride r WHERE r.driver.id = :driverId")
    List<Ride> findAllRidesOfDriver(Long driverId);

    Page<Ride> findByDriver(Driver driver, Pageable pageable);

    Page<Ride> findByRider(Rider rider, Pageable pageable);

}
