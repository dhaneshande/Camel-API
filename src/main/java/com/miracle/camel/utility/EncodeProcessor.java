package com.miracle.camel.utility;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.commons.codec.binary.Base64;

public class EncodeProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		Message msg = exchange.getIn();
		msg.setBody(Base64.encodeBase64(msg.getBody().toString().getBytes()));

	}
}