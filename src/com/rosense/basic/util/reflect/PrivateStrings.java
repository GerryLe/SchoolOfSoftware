package com.rosense.basic.util.reflect;

/**
 * 
* @author 黄家乐
 * 	
 * 2017年3月20日 
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
