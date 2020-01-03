package com.miracle.camel.utility;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

public class TransProcessor implements Processor {
	/**
	 * 
	 */
	@Override
	public void process(Exchange exchange) throws Exception {
		//
		Message msg = exchange.getIn();
		System.out.println(msg.getBody().toString().trim());
	}
}