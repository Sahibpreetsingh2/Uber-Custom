package com.sahib.uberApp.UberApplication.entities;

import com.sahib.uberApp.UberApplication.enums.PaymentMethod;
import com.sahib.uberApp.UberApplication.enums.RideRequestStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = {
        @Index(name = "idx_ride_request_rider" , columnList = "rider_id")
})
public class RideRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(columnDefinition = "Geometry(Point , 4326)")
    Point pickUpLocation;

    @Column(columnDefinition = "Geometry(Point , 4326)")
    Point dropOffLocation;

    @CreationTimestamp
    private LocalDateTime requestedTime;

    @ManyToOne
    private Rider rider;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private RideRequestStatus rideRequestStatus;

    private Double fare;
}
