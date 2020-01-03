package com.miracle.camel.utility;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class RouteCache {

	private static RouteCache routeCache;
	private static Map<String, File> cache = new HashMap<>();

	private RouteCache() {

	}

	public static RouteCache getInstance() {
		if (routeCache == null) {
			synchronized (RouteCache.class) {
				if (routeCache == null) {
					routeCache = new RouteCache();
				}
			}
		}
		return routeCache;
	}

	/**
	 * @return the cache
	 */
	public static Map<String, File> getCache() {
		return cache;
	}

	/**
	 * @param cache the cache to set
	 */
	public static void setCache(Map<String, File> cache) {
		RouteCache.cache = cache;
	}

}
