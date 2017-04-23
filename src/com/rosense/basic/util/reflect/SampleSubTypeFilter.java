package com.rosense.basic.util.reflect;

import java.net.URL;

/**
 * 
 * @author 黄家乐
 * 	
 * 2017年3月20日 
 *
 */
public class SampleSubTypeFilter implements ISubTypeFilter {
	public static final ISubTypeFilter DEFAULT = new SampleSubTypeFilter();

	public SampleSubTypeFilter() {
	}

	
	public boolean accept(final Class<?> baseType, final URL pathUrl, final String typePath) {
		String typeDef = typePath.replace('/', '.');
		typeDef = typeDef.substring(0, typeDef.lastIndexOf('.'));
		if (typeDef.equals(baseType.getName()))
			return false;
		Class<?> clazz;
		try {
			clazz = Class.forName(typeDef, false, Thread.currentThread().getContextClassLoader());
		} catch (final Throwable e) {
			return false;
		}
		if (!filterForInstance(clazz))
			return false;

		if (baseType.isAssignableFrom(clazz))
			return true;
		return false;
	}

	/**
	 * @param clazz
	 * @return
	 */
	protected boolean filterForInstance(final Class<?> clazz) {
		return true;
	}
}
