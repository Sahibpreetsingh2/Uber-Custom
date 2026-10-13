package com.sahib.uberApp.UberApplication.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RideRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne
    private Users rater; // who gives rating

    @ManyToOne
    private Users rated;  // who receive rating

    private Double rating;

    private String feedback;

    @ManyToOne
    private Ride ride;

    @CreationTimestamp
    private LocalDateTime createdAt;



}
