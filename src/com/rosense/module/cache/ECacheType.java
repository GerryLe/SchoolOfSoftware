package com.rosense.module.cache;

/**
 * 缓存数据存储数据库结构类型
 * 
 * @author QianFei.Xu qianfei.xu@rosense.cn
 * @dateTime Jan 4, 2010 2:11:23 PM
 */
public enum ECacheType {
	/**
	 * 表示一行数据是一个String
	 */
	string,
	/**
	 * 表示一行数据是一个Map&lt;String,String&gt;
	 */
	map_string,
	/**
	 * 表示一行数据是一个Map&lt;String,Map&lt;String,String&gt;&gt;
	 */
	map_map_string,
}