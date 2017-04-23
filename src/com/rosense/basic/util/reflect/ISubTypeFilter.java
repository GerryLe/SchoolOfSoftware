package com.rosense.basic.util.reflect;

import java.net.URL;

/**
 * 
 * @author 黄家乐
 * 	
 * 2017年3月20日 
 *
 */
public interface ISubTypeFilter {
	public boolean accept(Class<?> baseType, URL pathUrl, String typePath);
}
