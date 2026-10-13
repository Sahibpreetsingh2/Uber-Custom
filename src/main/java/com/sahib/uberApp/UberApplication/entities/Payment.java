package com.sahib.uberApp.UberApplication.entities;

import com.sahib.uberApp.UberApplication.enums.PaymentMethod;
import com.sahib.uberApp.UberApplication.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @OneToOne
    private Ride ride;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private Double amount;

    private LocalDateTime localDateTime;


    private String transactionId;

    private Double driverAmount;
    private Double platformCommissionAmt;

}
