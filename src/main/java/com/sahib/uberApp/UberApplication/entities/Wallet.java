package com.sahib.uberApp.UberApplication.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @OneToOne(optional = false)
    private Users users;

    private Double balance;

    @OneToMany(mappedBy = "wallet" , cascade = CascadeType.ALL)
    @ToString.Exclude
    List<WalletTransaction> walletTransactions;

}
