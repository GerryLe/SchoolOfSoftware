package com.rosense.basic.util.reflect;

import java.lang.reflect.Modifier;

/**
 * 
 * @author Can-Dao
 * 	
 * 2015年8月31日 下午1:04:43
 *
 */
public class SampleSubInstanceFilter extends SampleSubTypeFilter {
	public static final ISubTypeFilter DEFAULT = new SampleSubInstanceFilter();

	public SampleSubInstanceFilter() {
	}

	
	protected boolean filterForInstance(final Class<?> clazz) {
		final int modifier = clazz.getModifiers();
		return (!Modifier.isAbstract(modifier) && !Modifier.isInterface(modifier));
	}
}
