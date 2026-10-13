package com.sahib.uberApp.UberApplication.strategies;

import com.sahib.uberApp.UberApplication.entities.Payment;

public interface PaymentStrategy {

    double PLATEFORM_COMMISSIONION = 0.3;
    void getPaymentProcess(Payment payment);

}
