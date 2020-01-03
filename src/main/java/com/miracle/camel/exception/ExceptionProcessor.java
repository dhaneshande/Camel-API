package com.miracle.camel.exception;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.log.LogComponent;
import org.apache.log4j.Logger;

import com.miracle.camel.log.CustomLogger;

/**
 * Exception Processor is called whenever an exception needs to be handled
 * through out the application or routes.
 * 
 * 
 * Methods :
 * 
 * public void process(Exchange exchange) throws Exception
 * 
 * @author
 * 
 * 
 */
public class ExceptionProcessor implements Processor {
	// Declaring Logger Object and initializing the same with camel LogComponent
	// class
	private static Logger logger = Logger.getLogger(LogComponent.class);

	/**
	 *
	 */
	@Override
	public void process(Exchange exchange) throws Exception {
		String exception = exchange.getException().getMessage().toString().trim();
		logger.error("Exception : " + exception);
		System.out.println("Test Exception");
		logger.debug("Exception : " + exception);
		CustomLogger cl = new CustomLogger();
		cl.format(exchange);
	}

}
