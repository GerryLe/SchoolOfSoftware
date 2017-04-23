package com.rosense.basic.spring;

/**
 * 
 * @author 黄家乐
 * 	
 *
 */
public abstract class DataSourceContextHolder {
	public final static String DEFAULT_DATABASE = "druid_dataSource";
	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

	public static void setCustomerType(String customerType) {
		contextHolder.set(customerType);
	}

	public static synchronized String getCustomerType() {
		String db = contextHolder.get();
		clearCustomerType();
		if (db == null) {
			return DEFAULT_DATABASE;
		}
		return db;
	}

	public static void clearCustomerType() {
		contextHolder.remove();
	}
}