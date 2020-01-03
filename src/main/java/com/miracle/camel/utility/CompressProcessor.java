package com.miracle.camel.utility;

import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

/**
 * 
 * @author 
 *
 */
public class CompressProcessor implements Processor {
	
	/**
	 * 
	 */
	@Override
	public void process(Exchange exchange) throws Exception {
		// Declaring and Initializing Message object from exchange.
		Message msg = exchange.getIn();
		//
		msg.setBody("Audit Message");

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
		gzipOutputStream.write(msg.getBody().toString().getBytes());
		gzipOutputStream.close();
		byteArrayOutputStream.close();

	}
}