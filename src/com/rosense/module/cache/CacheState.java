package com.rosense.module.cache;

/**
 * @Title:标识缓存的状态,未加载,正在加载,已经加载
 * @Copyright: Copyright (c) 2010
 */
public class CacheState {
	public int cacheCount;
	public ECacheState eCacheState;

	public CacheState(int cacheCount, ECacheState eCacheState) {
		this.eCacheState = eCacheState;
		this.cacheCount = cacheCount;
	}

	public CacheState() {
		this(0, ECacheState.loading);
	}

	public enum ECacheState {
		loading, loaded
	}
}
