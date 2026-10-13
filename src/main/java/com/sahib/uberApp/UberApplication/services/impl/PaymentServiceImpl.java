package com.sahib.uberApp.UberApplication.services.impl;

import com.sahib.uberApp.UberApplication.entities.Payment;
import com.sahib.uberApp.UberApplication.entities.Ride;
import com.sahib.uberApp.UberApplication.enums.PaymentStatus;
import com.sahib.uberApp.UberApplication.exception.ResourceNotFound;
import com.sahib.uberApp.UberApplication.repositories.PaymentRepository;
import com.sahib.uberApp.UberApplication.services.PaymentService;
import com.sahib.uberApp.UberApplication.strategies.PaymentStrategyManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentStrategyManager paymentStrategyManager;

    @Override
    public void processPayment(Ride ride , Payment payment ) {
         paymentStrategyManager.getPaymentStrategy(ride.getPaymentMethod()).getPaymentProcess(payment);
    }

    @Override
    public Payment createNewPayment(Ride updateRide) {

        Payment payment = Payment.builder()
                .paymentMethod(updateRide.getPaymentMethod())
                .paymentStatus(PaymentStatus.PENDING)
                .amount(updateRide.getFare())
                .ride(updateRide)
                .build();

        return paymentRepository.save(payment);
    }

    @Override
    public Payment findByRide(Ride ride) {
        return paymentRepository.findByRide(ride).orElseThrow(()->
                new ResourceNotFound("Payment not found with ride id : "+ride.getId()));
    }

    @Override
    @Transactional
    public void updatePaymentStatus(Payment payment, PaymentStatus paymentStatus) {
        payment.setPaymentStatus(paymentStatus); // dirty checking
    }
}
