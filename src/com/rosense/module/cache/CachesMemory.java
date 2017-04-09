package com.rosense.module.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 基于内存Map实现的缓存管理器
 * 
 */
public final class CachesMemory extends ACaches {
	private static final CachesMemory instance = new CachesMemory();

	public static final CachesMemory instance() {
		return CachesMemory.instance;
	}

	public CachesMemory() {
	}

	
	public ECacheEngine getCacheEngine() {
		return ECacheEngine.MEMORY;
	}

	private final Map<String, Map<String, Object>> SWAP = new HashMap<String, Map<String, Object>>(8);

	
	public Object getCacheValue(final String cacheName, final String cacheKey) {
		return safeGetCache(cacheName, null).get(cacheKey);
	}

	
	public Collection<String> keysCache(final String cacheName) {
		return safeGetCache(cacheName, null).keySet();
	}

	
	public void removeCache(final String cacheName) {
		SWAP.remove(cacheName);
	}

	private final Lock lockGet = new ReentrantReadWriteLock().writeLock();

	
	public Map<String, Object> safeGetCache(final String cacheName, final Properties props) {
		Map<String, Object> object = SWAP.get(cacheName);
		if (null != object)
			return object;
		lockGet.lock();
		try {
			// check again
			object = SWAP.get(cacheName);
			if (null != object)
				return object;
			object = new HashMap<String, Object>(16);
			SWAP.put(cacheName, object);
		} finally {
			lockGet.unlock();
		}
		return object;
	}

	//used for lock operation when set cache value
	private final Lock lockSet = new ReentrantReadWriteLock().writeLock();

	
	public void setCacheValue(final String cacheName, final String cacheKey, final Object cacheObject) {
		try {
			lockSet.lock();
			safeGetCache(cacheName, null).put(cacheKey, cacheObject);
		} finally {
			lockSet.unlock();
		}
	}
}

