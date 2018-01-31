/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.bean;

import com.otod.test.QuoteClient;


/**
 *
 * @author Administrator
 */
public class ServerContext {

    private static QuoteClient quoteClient = null;

    public static QuoteClient getQuoteClient() {
        return quoteClient;
    }

    public static void setQuoteClient(QuoteClient quoteClient) {
        ServerContext.quoteClient = quoteClient;
    }
}
