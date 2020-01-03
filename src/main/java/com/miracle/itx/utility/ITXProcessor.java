package com.miracle.itx.utility;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.component.log.LogComponent;
import org.apache.log4j.Logger;

public class ITXProcessor implements Processor {
	private static Logger logger = Logger.getLogger(LogComponent.class);
	@Override
	public void process(Exchange exchange) throws Exception {
		logger.debug("Invoking ITXProcessor");
		Message msg = exchange.getIn();
		System.out.println("Received message : " + msg.getBody());
		logger.debug("ITXProcessor : Received message : " + msg.getBody());
		String response = ITXInvoker.transform(msg.getBody().toString(),(String) msg.getHeader("mapName"));
		msg.setBody(response);		
		System.out.println("Transformed message : " + msg.getBody());
		logger.debug("ITXProcessor : Transformed message : " + msg.getBody());
	}
}
