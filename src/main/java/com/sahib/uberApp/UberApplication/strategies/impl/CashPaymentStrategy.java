package com.sahib.uberApp.UberApplication.strategies.impl;

import com.sahib.uberApp.UberApplication.entities.Driver;
import com.sahib.uberApp.UberApplication.entities.Payment;
import com.sahib.uberApp.UberApplication.entities.Wallet;
import com.sahib.uberApp.UberApplication.enums.TransacationMethod;
import com.sahib.uberApp.UberApplication.enums.TransacationType;
import com.sahib.uberApp.UberApplication.services.WalletService;
import com.sahib.uberApp.UberApplication.strategies.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CashPaymentStrategy implements PaymentStrategy  {

    private final WalletService walletService;

    // Ride : 100 cash
    // Driver : 200
    // plateform commission = 0.3
    // Driver deduct : 30 ruppe

    // Driver : 200  - 30

    @Override
    @Transactional
    public void getPaymentProcess(Payment payment) {

        Double rideAmount  = payment.getAmount();

        double deductDriverWalletAmt = rideAmount * PaymentStrategy.PLATEFORM_COMMISSIONION;

        Driver driver = payment.getRide().getDriver();

        Wallet wallet = walletService.findByUsers(driver.getUsers().getId());

        walletService.deductMoneyFromWallet(driver.getUsers().getId() , deductDriverWalletAmt , TransacationType.DEBIT , TransacationMethod.RIDE , payment.getRide() , null);

        payment.setPlatformCommissionAmt(deductDriverWalletAmt); // dirty checking ....
    }
}
