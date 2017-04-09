package com.rosense.basic.util.reflect;

/**
 * 
 * @author Can-Dao
 * 	
 * 2015年8月31日 下午1:05:01
 *
 */
abstract class PrivateStrings {
	/**
	 * @param array
	 * @param valueItem
	 * @return
	 */
	public static final boolean likesIn(final String valueItem, final String[] array) {
		if (null == array || null == valueItem || "".equals(valueItem.trim()))
			return false;

		for (final String value : array) {
			if (valueItem.equals(value) || valueItem.startsWith(value) || valueItem.endsWith(value))
				return true;
		}
		return false;
	}

}
