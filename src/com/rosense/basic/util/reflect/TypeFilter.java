package com.rosense.basic.util.reflect;

/**
 * 
 * @author Can-Dao
 * 	
 * 2015年8月31日 下午1:04:56
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
