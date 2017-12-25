/* Copyright (C) 2013 Interactive Brokers LLC. All rights reserved.  This code is subject to the terms
 * and conditions of the IB API Non-Commercial License or the IB API Commercial License, as applicable. */

package com.lt.tms.library.ib.contracts;

import com.lt.tms.library.ib.client.Contract;
import com.lt.tms.library.ib.client.Types.SecType;

public class FutContract extends Contract {

    public FutContract(String symbol, String lastTradeDateOrContractMonth) {
        symbol(symbol);
        secType(SecType.FUT);
        exchange("ONE");
        currency("USD");
        lastTradeDateOrContractMonth(lastTradeDateOrContractMonth);
    }

    public FutContract(String symbol, String lastTradeDateOrContractMonth, String currency,String exchange) {
        symbol(symbol);
        secType(SecType.FUT.name());
        currency(currency);
        exchange(exchange);
        lastTradeDateOrContractMonth(lastTradeDateOrContractMonth);
    }
}
