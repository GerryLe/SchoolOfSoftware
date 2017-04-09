package com.rosense.basic.util.reflect;

import java.net.URL;

/**
 * 
 * @author Can-Dao
 * 	
 * 2015年8月31日 下午1:05:20
 *
 */
public interface ISubTypeFilter {
	public boolean accept(Class<?> baseType, URL pathUrl, String typePath);
}
