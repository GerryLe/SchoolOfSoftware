package com.rosense.basic.util.reflect;

import java.io.File;
import java.net.URL;

/**
 * 
 * @author Can-Dao
 * 	
 * 2015年8月31日 下午1:05:09
 *
 */
public class PathURLFilter implements IPathURLFilter {
	public static final IPathURLFilter DEFAULT = new PathURLFilter("/", ".jar");
	private String[] exts;

	public PathURLFilter(final String... exts) {
		this.exts = exts;
	}

	
	public boolean accept(final URL pathURL) {
		// for any
		if (null == exts)
			return true;
		// do filter
		return PrivateStrings.likesIn(new File(pathURL.getFile()).getName(), exts);
	}
}
