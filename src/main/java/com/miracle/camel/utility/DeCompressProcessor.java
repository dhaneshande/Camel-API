package com.miracle.camel.utility;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

public class DeCompressProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		Message msg = exchange.getIn();

		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(msg.getBody().toString().getBytes());
		GZIPInputStream gzipOutputStream = new GZIPInputStream(byteArrayInputStream);
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gzipOutputStream, "UTF-8"));
		StringBuilder decompressedMessage = new StringBuilder();
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			decompressedMessage.append(line);
		}
		bufferedReader.close();
		gzipOutputStream.close();
		
		msg.setBody(decompressedMessage.toString());
	}
}