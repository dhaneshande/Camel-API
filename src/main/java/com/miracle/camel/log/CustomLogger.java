package com.miracle.camel.log;

import org.apache.camel.Exchange;
import org.apache.camel.component.log.LogComponent;
import org.apache.camel.spi.ExchangeFormatter;
import org.apache.log4j.Logger;

/**
 * 
 * @author
 *
 */
public class CustomLogger implements ExchangeFormatter {
	// Declaring Logger Object and initializing the same with camel LogComponent
	// class
	private static Logger logger = Logger.getLogger(LogComponent.class);
	
	
	/**
	 * 
	 */
	@Override
	public String format(Exchange exchange) {
		try {
			// Logging Custom logs
			logger.debug("From Route : "+exchange.getFromRouteId().toString() + " : " + exchange.getFromEndpoint().toString());
		} catch (Exception e) {
			logger.error(e.getMessage().toString().trim());
			System.out.println("test out");
		}
		// Returning customized Log Message Which will be logged on to console.
		return exchange.getFromRouteId().toString() + " : " + exchange.getFromEndpoint().toString();

	}

}
