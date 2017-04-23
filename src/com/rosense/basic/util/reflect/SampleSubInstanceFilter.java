package com.rosense.basic.util.reflect;

import java.lang.reflect.Modifier;

/**
 * 
* @author 黄家乐
 * 	
 * 2017年3月20日 
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
