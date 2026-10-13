package com.sahib.uberApp.UberApplication.entities;

import com.sahib.uberApp.UberApplication.enums.PaymentMethod;
import com.sahib.uberApp.UberApplication.enums.RideStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = {
        @Index(name = "idx_ride_riderId" , columnList = "rider_id"),
        @Index(name = "idx_ride_driverId" , columnList = "driver_id")
})
@Entity
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(columnDefinition = "geometry(Point , 4326)")
    private Point pickUpLocation;

    @Column(columnDefinition = "geometry(Point , 4326)")
    private Point dropOffLocation;

    @CreationTimestamp
    private LocalDateTime createdTime;

    @ManyToOne
    private Rider rider;

    @ManyToOne
    private Driver driver;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private RideStatus rideStatus;

    private String otp;

    LocalDateTime startedAt;
    LocalDateTime endedAt;

    private Double fare;

}
