package com.sahib.uberApp.UberApplication.services;

import com.sahib.uberApp.UberApplication.entities.Payment;
import com.sahib.uberApp.UberApplication.entities.Ride;
import com.sahib.uberApp.UberApplication.enums.PaymentStatus;

public interface PaymentService {

    void processPayment(Ride ride , Payment payment);

    Payment createNewPayment(Ride updateRide);

    Payment findByRide(Ride ride);

    void updatePaymentStatus(Payment payment, PaymentStatus paymentStatus);
}
