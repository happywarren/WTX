/* Copyright (C) 2013 Interactive Brokers LLC. All rights reserved.  This code is subject to the terms
 * and conditions of the IB API Non-Commercial License or the IB API Commercial License, as applicable. */

package com.lt.tms.library.ib.contracts;

import com.lt.tms.library.ib.client.Contract;
import com.lt.tms.library.ib.client.Types;

public class StkContract extends Contract {
    public StkContract(String symbol) {
        symbol(symbol);
        secType(Types.SecType.STK.name());
        exchange("SMART");
        currency("USD");
    }

    public StkContract(String symbol,String exchange,String currency) {
        symbol(symbol);
        secType(Types.SecType.STK.name());
        exchange(exchange);
        currency(currency);
    }
}
