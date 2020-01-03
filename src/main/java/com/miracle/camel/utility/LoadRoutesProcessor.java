package com.miracle.camel.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.file.watch.FileWatchComponent;
import org.apache.camel.component.log.LogComponent;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.model.ModelHelper;
import org.apache.camel.model.RoutesDefinition;
import org.apache.log4j.Logger;

import com.miracle.camel.starter.CamelAppStarter;


/**
 * Load Routes Processor is called only when new routes are added to the routes
 * folder.
 * 
 * This processor reloads the new routes from the new route XML file into
 * ModelCamelContext and start that particular route.
 * 
 * While loading new routes and starting them this class doesn't effect any
 * existing routes that were loaded to context previously.
 * 
 * Methods :
 * 
 * public void process(Exchange exchange) throws Exception
 * 
 * public void loadRoutes(String fileName) throws Exception
 * 
 * public void deleteRoutes(String fileName) throws Exception
 * 
 * @author
 * 
 * 
 */
public class LoadRoutesProcessor implements Processor {

	// Declaring ModelCamelContext Object
	private static ModelCamelContext modelCamelContext = null;
	// Declaring Logger Object
	private static Logger logger = Logger.getLogger(LogComponent.class);
	// Declaring and initializing routeFolder path
	private static String routesFolder = "//routes//";
	
	/**
	 * This method loads new routes to the Application camel Context and starts that
	 * particular route without effecting other routes that are already running.
	 * 
	 * @param fileName
	 * @throws Exception
	 */
	
	@SuppressWarnings("static-access")
	public void loadRoutes(String fileName) throws Exception {
		// Creating application object.
		CamelAppStarter application = new CamelAppStarter();
		// Logging Route Definition file name.
		logger.debug("Loading RouteDefinition :" + fileName);
		// Getting ModelCamelContext from the application.
		modelCamelContext = application.getModelCamelContext();
		// Creating file object for the newly added route file.
		File file = new File(routesFolder + fileName);
		// Creating InputStream object for the new route file added.
		InputStream is = new FileInputStream(file);
		// Logging RouteDefnition Check for the new route file.
		logger.debug("Creating RouteDefinition for :" + file.getName().toString().trim());
		// Creating and Initializing route Definition from the route xml using
		// modelCamelContext and the file inputStream.
		RoutesDefinition routeDefinition = ModelHelper.loadRoutesDefinition(modelCamelContext, is);
		// Adding the new routes from the route Definition into ModelCamelContext.
		modelCamelContext.addRouteDefinitions(routeDefinition.getRoutes());
		// Logging route definition check after adding routes.
		logger.debug("Route Definition added:" + routeDefinition.getRoutes().toString());
		// Reloading routeCache after loading new route.
		RouteCache.getInstance().getCache().put(file.getName(), file);
		// Starting the modelCamelContext for the new route added.
		modelCamelContext.start();
	}

	/**
	 * This method Deletes removed routes from the Application camel Context and
	 * shutdowns that particular route without effecting other routes that are
	 * already running.
	 * 
	 * @param fileName
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	public void deleteRoutes(String fileName) throws Exception {
		// Creating application object.
		CamelAppStarter application = new CamelAppStarter();
		// Logging Route Definition file name.
		logger.debug("Loading RouteDefinition :" + fileName);
		// Getting ModelCamelContext from the application.
		modelCamelContext = application.getModelCamelContext();
		// Logging routeId to be removed
		logger.debug(fileName.substring(0, fileName.lastIndexOf(".")));
		// Loading and removing the routeDefinition from CamelContext of the route
		// removed.
		modelCamelContext.removeRouteDefinition(
				modelCamelContext.getRouteDefinition(fileName.substring(0, fileName.lastIndexOf("."))));
		// Logging route definition check after removing route.
		logger.debug("Route Definition removed:" + fileName.substring(0, fileName.lastIndexOf(".")));
		// Removing file details from routeCache after removing route definition for
		// deleted route.
		RouteCache.getInstance().getCache()
				.remove(routesFolder + fileName);
		// Logging route Cache update
		logger.debug("Route Definition for routeId " + fileName.substring(0, fileName.lastIndexOf("."))
				+ " has been removed from RouteCache");
	}

	/**
	 * This method is called from the "loadroutes.xml" file once any new route XML
	 * is added to route folder.
	 */
	@Override
	public void process(Exchange exchange) throws Exception {
		// Declaring and Initializing filename object for the new route file added to
		// the routes directory.
		String fileName = exchange.getIn().getHeader("CamelFileName").toString().trim();
		// Declaring and initializing fileEvent Object from file watch component.
		String fileEvent = exchange.getIn().getHeader(FileWatchComponent.EVENT_TYPE_HEADER).toString().trim();
		// Logging new filename added to routes folder
		
		try {
			if (fileEvent.equalsIgnoreCase("CREATE") || fileEvent.equalsIgnoreCase("MODIFY")) {
				logger.info(fileName + " :file added to routes");
				// Validating for XML type file.
				if (fileName.endsWith(".xml")) {
					// Logging check before calling load routes method
					logger.info("Calling Load Routes to load :" + fileName);
					// Calling load routes method to load the new route from the added file and to
					// reload the context with the same.
					loadRoutes(fileName);
				} else {
					// Logging info if the file is not and XML type.
					logger.info("Omitting " + fileName + " as it is not in xml format");
				}
			}
			if (fileEvent.equalsIgnoreCase("DELETE")) {
				logger.info(fileName + " :file deleted from routes");
				// Logging check before calling delete routes method
				logger.info("Calling Delete Routes as :" + fileName + " is deleted");
				// Calling Delete routes method to remove the route definition of the removed
				// route from Camel context.
				deleteRoutes(fileName);
				// Logging check after calling delete routes method
				logger.info("Deleted route config for :" + fileName + " from context");
			}

		} catch (Exception exception) {
			// Logging exception details.
			logger.error("RouteLoader :" + fileName + " : " + exception.getMessage().toString().trim());
		}
	}
}
