package com.rosense.basic.util.reflect;

/**
 * 
* @author 黄家乐
 * 	
 * 2017年3月20日 
 *
 */
public class TypeFilter implements ITypeFilter {
	public static final ITypeFilter DEFAULT = new TypeFilter(".class");
	private String[] exts;

	public TypeFilter(final String... exts) {
		this.exts = exts;
	}

	
	public boolean accept(final String typeName) {
		if (null == exts)
			return true;
		return PrivateStrings.likesIn(typeName, exts);
	}
}
