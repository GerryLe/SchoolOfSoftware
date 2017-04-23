package com.rosense.basic.util.reflect;

import java.io.File;
import java.net.URL;

/**
 * 
* @author 黄家乐
 * 	
 * 2017年3月20日 
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
