package com.sahib.uberApp.UberApplication.repositories;

import com.sahib.uberApp.UberApplication.entities.Driver;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {



    // ST_Distance(point1, point2) -> cal. distance
    // ST_DWithin(point1, 10000) // -> cal. within 10 km area


    @Query(value = "select d.* , ST_Distance(d.current_location ,  :pickUpLocation) AS distance " +
            "FROM driver d " +
            "WHERE d.available = true AND ST_DWithin(d.current_location , :pickUpLocation , 1000) " +
            "ORDER BY distance " +
            "LIMIT 5 ", nativeQuery = true)
    List<Driver> findTopNearestDriver(Point pickUpLocation);

    @Query(value = "SELECT d.* " +
            "FROM driver d " +
            "WHERE d.available = true AND ST_DWithin(d.current_location , :pickUpLocation , 1500) " +
            "ORDER BY d.average_rating DESC " +
            "LIMIT 5 " , nativeQuery = true)
    List<Driver> findTopRatedDrivers(Point pickUpLocation);

    boolean existsByUsersId(Long userId);

//    @Modifying
//    @Query("""
//            UPDATE driver r
//            set r.averageRating = (r.averageRating * r.totalRating + :rating) / (r.totalRating + 1),
//            r.totalRating = r.totalRating + 1
//            where r.users.id = :userId
//            """)
//    void updateRating(Long userId, Double rating);

    @Modifying
    @Transactional
    @Query("""
    UPDATE Driver r
    SET r.averageRating = (r.averageRating * r.totalRating + :rating) / (r.totalRating + 1),
    r.totalRating = r.totalRating + 1
    WHERE r.users.id = :userId
    """)
    void updateRating(Long userId, Double rating);

}
