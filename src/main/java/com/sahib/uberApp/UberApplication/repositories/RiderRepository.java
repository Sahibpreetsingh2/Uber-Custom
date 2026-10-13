package com.sahib.uberApp.UberApplication.repositories;

import com.sahib.uberApp.UberApplication.entities.Rider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RiderRepository extends JpaRepository<Rider, Long> {

    boolean existsByUsersId(Long userId);

//    newAvg = (oldAvg * oldCount + newRating) / (oldCount + 1)

//    @Modifying
//    @Query("""
//            UPDATE rider r
//            set r.averageRating = (r.averageRating * r.totalRating + :rating) / (r.totalRating + 1),
//            r.totalRating = r.totalRating + 1
//            where r.users.id = :userId
//            """)
//    void updateRating(Long userId, Double rating);


    @Modifying
    @Transactional
    @Query("""
    UPDATE Rider r
    SET r.averageRating = (r.averageRating * r.totalRating + :rating) / (r.totalRating + 1),
        r.totalRating = r.totalRating + 1
    WHERE r.users.id = :userId
""")
    void updateRating(Long userId, Double rating);


}
