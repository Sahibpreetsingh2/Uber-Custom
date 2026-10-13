package com.sahib.uberApp.UberApplication.repositories;

import com.sahib.uberApp.UberApplication.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet  , Long> {

//    @Query("Select r from Wallet r where r.users.id = :usersId")
    Optional<Wallet> findByUsers_Id(Long usersId);
}
