/* Copyright (C) 2013 Interactive Brokers LLC. All rights reserved.  This code is subject to the terms
 * and conditions of the IB API Non-Commercial License or the IB API Commercial License, as applicable. */

package com.lt.tms.library.ib.contracts;

import com.lt.tms.library.ib.client.Contract;
import com.lt.tms.library.ib.client.Types;

public class ComboContract extends Contract {
    public ComboContract(String symbol) {
        this(symbol, "USD", "SMART");
    }

    public ComboContract(String symbol, String currency) {
        this(symbol, currency, "SMART");
    }

    public ComboContract(String symbol, String currency, String exchange) {
        symbol(symbol);
        secType(Types.SecType.BAG.name());
        currency(currency);
        exchange(exchange);
    }
}
