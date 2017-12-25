package com.lt.tms.library.ib.client;

import java.io.IOException;

public interface ETransport {
	void send(EMessage msg) throws IOException;
}
