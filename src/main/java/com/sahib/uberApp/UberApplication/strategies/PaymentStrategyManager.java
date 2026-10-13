package com.sahib.uberApp.UberApplication.strategies;

import com.sahib.uberApp.UberApplication.enums.PaymentMethod;
import com.sahib.uberApp.UberApplication.strategies.impl.CashPaymentStrategy;
import com.sahib.uberApp.UberApplication.strategies.impl.WalletPaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentStrategyManager {

    private final CashPaymentStrategy cashPaymentStrategy;
    private final WalletPaymentStrategy walletPaymentStrategy;

    public PaymentStrategy getPaymentStrategy(PaymentMethod paymentMethod){

        return switch (paymentMethod){
            case WALLET -> walletPaymentStrategy;
            case COD -> cashPaymentStrategy;
        };


    }

}
