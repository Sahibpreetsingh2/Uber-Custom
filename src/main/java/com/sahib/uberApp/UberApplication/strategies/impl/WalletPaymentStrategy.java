package com.sahib.uberApp.UberApplication.strategies.impl;

import com.sahib.uberApp.UberApplication.entities.Driver;
import com.sahib.uberApp.UberApplication.entities.Payment;
import com.sahib.uberApp.UberApplication.entities.Rider;
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
public class WalletPaymentStrategy implements PaymentStrategy  {

    private final WalletService walletService;

    // Ride : 100
    //Rider = 200
    //Driver  = 300
    // platecommission = 0.3
    // deductfromRider = 100
    // addAmounToDriver = 70
    // PC = 30

    @Override
    @Transactional
    public void getPaymentProcess(Payment payment) {

        Double rideAmount  = payment.getAmount();

        // Driver Part :
        Driver driver = payment.getRide().getDriver();
        Wallet driverWallet = walletService.findByUsers(driver.getUsers().getId());

        double addAmountTODriverWallet = rideAmount * (1 - PaymentStrategy.PLATEFORM_COMMISSIONION);  // 70

        walletService.addMoneyToWallet(driver.getUsers().getId() , addAmountTODriverWallet ,TransacationType.CREDIT , TransacationMethod.RIDE , payment.getRide() , null);

        // Rider Part :

        Rider rider = payment.getRide().getRider();
        walletService.deductMoneyFromWallet(rider.getUsers().getId() , rideAmount , TransacationType.DEBIT , TransacationMethod.RIDE , payment.getRide() , null);

        payment.setDriverAmount(addAmountTODriverWallet);
        payment.setPlatformCommissionAmt(rideAmount - addAmountTODriverWallet);

    }
}
