package com.rosense.module.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

/**
 * 一个缓存配置定义
 * 
 */
public class CacheConfig {

	/**
	 * 属性定义
	 */
	public Properties properties;

	/**
	 * 缓存集合,比如定义的多个SQL等
	 */
	public final Collection<String> cacheList = new ArrayList<String>(2);
	public final ECacheEngine cacheEngine;
	public String name;

	/**
	 * @param name
	 */
	public CacheConfig(final String name, final ECacheEngine cacheEngine) {
		this.name = name;
		this.cacheEngine = cacheEngine;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public final boolean isShared() {
		if (null == properties)
			return false;
		return !"false".equals(properties.getProperty("shared", "true"));
	}
}
