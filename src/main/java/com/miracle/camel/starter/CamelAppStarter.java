package com.miracle.camel.starter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStream;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelContextAware;
import org.apache.camel.component.log.LogComponent;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.model.ModelHelper;
import org.apache.camel.model.RoutesDefinition;
import org.apache.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

import com.miracle.camel.utility.RouteCache;


/**
 * 
 * @author 
 *
 */
@SpringBootApplication
@ComponentScan("com.miracle.*")
@ImportResource({ "classpath:camel-context.xml" })
public class CamelAppStarter implements CamelContextAware, CommandLineRunner {
	// Declaring CamelContext Object
	private static CamelContext camelContext = null;
	// Declaring ModelCamelContext Object
	private static ModelCamelContext modelCamelContext = null;
	// Declaring Logger Object
	private static Logger logger = Logger.getLogger(LogComponent.class);
	// Declaring and initializing routeFolder path
	private static String routesFolder = "C:\\Users\\mthadi\\eclipse-workspace-01\\camel-api-v3-log\\routes\\";
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// Logging Application Starter Check
			logger.debug(CamelAppStarter.class.getName().toString()+" : starting runner class");
			// Starting Application run
			SpringApplication.run(CamelAppStarter.class, args);
		} catch (Exception exception) {
			// Logging Exception details
			logger.error(exception.getMessage().toString().trim(), exception);
		}
	}
	
	/**
	 * 
	 */
	@SuppressWarnings("static-access")
	@Override
	public void run(String... args) throws Exception {
		try {
			// Logging run method check. 
			logger.debug(" Started Loading routes");
			// Initializing routes folder object.
			File folder = new File(routesFolder);
			// Logging routes folder object check
			logger.debug("Routes Folder Object Created");
			// Listing files from the route folder.
			File[] files = folder.listFiles(new FilenameFilter() {
				// Checking for only xml files and adding the same to files array.
				@Override
				public boolean accept(File dir, String name) {
					// Returning only xml files.
					return name.endsWith(".xml");
				}
			});
			
			modelCamelContext = camelContext.adapt(ModelCamelContext.class);
			//Iterating route xml's 
			for (File file : files) {
				// Logging route file name.
				logger.debug("Loading Route : "+file.getName().toString().trim());
				// Creating route file input stream.
				InputStream is = new FileInputStream(file);
				// Loading route definition from file input stream.
				RoutesDefinition routeDefinition = ModelHelper.loadRoutesDefinition(modelCamelContext, is);
				// Logging RouteDefinition check.
				logger.debug("Creating RouteDefinition for : "+file.getName().toString().trim());
				// Adding routes from route Definition to context.
				modelCamelContext.addRouteDefinitions(routeDefinition.getRoutes());
				// Reloading Route Cache.
				RouteCache.getInstance().getCache().put(file.getName(), file);
				// Logging Route Start Check.
				logger.debug("Starting Route : "+file.getName().toString().trim());
				// Starting Context for the new route.
				modelCamelContext.start();
			}
		} catch (Exception exception) {
			// Logging Exception details
			logger.error(exception.getMessage().toString().trim(), exception);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public static ModelCamelContext getModelCamelContext() {
		// Returning ModelCamelContext Object
		return CamelAppStarter.modelCamelContext;
	}

	/**
	 * 
	 * @param modelCamelContext
	 */
	public static void setModelCamelContext(ModelCamelContext modelCamelContext) {
		// Setting ModelCamelContext Object
		CamelAppStarter.modelCamelContext = modelCamelContext;
	}
	
	/**
	 * 
	 *  @param camelContext
	 */
	@Override
	public void setCamelContext(CamelContext camelContext) {
		// Setting CamelContext Object
		CamelAppStarter.camelContext = camelContext;
	}
	
	/**
	 * 
	 */
	@Override
	public CamelContext getCamelContext() {
		// Returning CamelContext Object
		return CamelAppStarter.camelContext;
	}

}
