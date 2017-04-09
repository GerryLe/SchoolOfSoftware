package com.rosense.module.cache;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rosense.basic.util.SQLUtils;
import com.rosense.basic.util.StringUtil;
import com.rosense.basic.util.dbutil.DBConnectPool;
import com.rosense.module.cache.CacheState.ECacheState;

/**
 * 用于管理系统级别的缓存,如小区信息,ADJC信息等.
 * 
 */
public final class Caches {
	final Log log;
	private static final Caches instance = new Caches();
	private final Collection<ACaches> cacheImpls = new HashSet<ACaches>(ECacheEngine.values().length);
	private final Map<String, CacheState> cacheStateMaps = new HashMap<String, CacheState>();
	/**
	 * 所有注册的缓存配置定义
	 */
	public final Map<String, CacheConfig> configs;

	/**
	 * nerver instance
	 */
	private Caches() {
		configs = new HashMap<String, CacheConfig>(8);
		log = LogFactory.getLog(Caches.class);
		//loadFromCacheXml();
	}

	public static final Caches getInstance() {
		return instance;
	}

	/**
	 * 
	 * 默认加载config/cache.xml中定义的缓存对象
	 */
	private void loadFromCacheXml() {
		final String cacheConfigFile = AppContext.getAppConfig("cache.xml");
		try {
			CachesUtil.loadCaches(this, new FileInputStream(cacheConfigFile), false);
		} catch (final FileNotFoundException e) {
			// if file not found ,ignore,just info
			//			System.err.println("load cache file " + cacheConfigFile + " failed:file not found.");
		} catch (final Exception e) {
			e.printStackTrace();// for debug
		}
	}

	private final Lock lockInitImpl = new ReentrantReadWriteLock().writeLock();

	private final ACaches getCachesImpl(final ECacheEngine cacheEngine) {
		for (final ACaches cacheImpl : cacheImpls)
			if (cacheEngine == cacheImpl.getCacheEngine())
				return cacheImpl;
		lockInitImpl.lock();
		try {
			for (final ACaches cacheImpl : cacheImpls)
				if (cacheEngine == cacheImpl.getCacheEngine())
					return cacheImpl;
			ACaches cacheImpl = null;
			switch (cacheEngine) {
			case MEMORY:
				cacheImpls.add(cacheImpl = new CachesMemory());
				break;
			case EHCACHE_DISK:
				cacheImpls.add(cacheImpl = new CachesEhcache());
				break;
			}
			if (null != cacheImpl)
				return cacheImpl;
		} finally {
			lockInitImpl.unlock();
		}
		throw new UnsupportedOperationException("Unsupported cacheEngine:" + cacheEngine);
	}

	/**
	 * 判断当前缓存集中是否存在给定名称的缓存
	 * @param cacheName 要判断是否存在的缓存名称
	 * @return 存在返回true,否则false
	 */
	public static final boolean contains(final String cacheName) {
		return Caches.instance.configs.containsKey(cacheName);
	}

	public static final void remove(final String cacheName) {
		final CacheConfig config = Caches.instance.configs.remove(cacheName);
		if (null == config)
			return;
		Caches.instance.getCachesImpl(config.cacheEngine).removeCache(cacheName);
		Caches.instance.cacheStateMaps.remove(cacheName);
	}

	public static final CacheState getCacheState(final String cacheName) {
		if (StringUtils.isBlank(cacheName)) {
			return null;
		}
		return Caches.instance.cacheStateMaps.get(cacheName);
	}

	public static void checkSafeCache(final String cacheName) {
		CacheState cacheState = Caches.getCacheState(cacheName);
		if (cacheState == null) {
			Caches.refreshCache(cacheName, DBConnectPool.getInstance().getDataSource());
			cacheState = Caches.getCacheState(cacheName);
			if (cacheState == null) {
				return;
			}
		}
		switch (cacheState.eCacheState) {
		case loading:
			try {
				Thread.sleep(5000);
				checkSafeCache(cacheName);
			} catch (final InterruptedException e) {
			}
			break;
		case loaded:
			if (cacheState.cacheCount == 0) {
				Caches.refreshCache(cacheName, DBConnectPool.getInstance().getDataSource());
			}
			break;
		}
	}

	/**
	 * 向cacheMgr里面注册缓存,此方法只实现了Map<String,String>类型行数据的存储, 给定的sql中必须有一个CACHEID的列名作为该Cache的唯一标识
	 * 
	 * @see Caches#registryMap(DataSource, String, String, String)
	 * @param dataSource
	 * @param domain
	 * @param sqlsMap
	 *            cacheName:sql
	 */
	public final void registryMap(final DataSource dataSource, final CacheConfig... configs) {
		for (final CacheConfig config : configs) {
			try {
				registryMap(dataSource, config);
			} catch (final Exception e) {
				//
			}
		}
	}

	/**
	 * 判断如果给定的缓存名称不存在时将根据给定的properties属性进行创建新缓存结构.
	 * @param cacheName 缓存名称
	 * @param props 该缓存结构的属性值
	 * @return 具体的缓存实例,此实例不是数据实例,而是管理数据的容器实例.
	 */
	public static final Object createCacheIfNonExists(final String cacheName, final Properties props) {
		final Caches caches = getInstance();
		// 获得旧的定义,如果存在,只增加SQL
		CacheConfig cacheConfig = caches.configs.get(cacheName);
		if (null == cacheConfig)// 不存在时,需要新增加
		{
			final String cacheEngine = props.getProperty("cacheEngine", ECacheEngine.EHCACHE_DISK.name());
			caches.configs.put(cacheName, cacheConfig = new CacheConfig(cacheName, ECacheEngine.valueOf(cacheEngine)));
			cacheConfig.properties = props;
		}
		final ACaches cache = Caches.instance.getCachesImpl(cacheConfig.cacheEngine);
		return cache.safeGetCache(cacheName, props);
	}

	/**
	 * 向cacheMgr里面注册缓存,此方法只实现了Map<String,String>类型行数据的存储, 给定的sql中必须有一个CACHEID的列名作为该Cache的唯一标识
	 * 
	 * @param dataSource
	 * @param cacheName
	 * @param sql
	 */
	public final void registryMap(final DataSource dataSource, final CacheConfig inConfig) {
		if (null == inConfig)
			return;
		if (inConfig.cacheList.isEmpty())
			return;

		final String cacheName = inConfig.getName();
		// 获得旧的定义,如果存在,只增加SQL
		CacheConfig cacheConfig = configs.get(cacheName);
		if (null == cacheConfig)// 不存在时,需要新增加
		{
			configs.put(cacheName, cacheConfig = new CacheConfig(cacheName, inConfig.cacheEngine));
			cacheConfig.properties = inConfig.properties;
		}
		cacheStateMaps.put(cacheName, new CacheState());

		// 根据不同的存储类型使用不同的实现
		final ACaches caches = Caches.instance.getCachesImpl(cacheConfig.cacheEngine);
		final ECacheType cacheType = ECacheType.valueOf(cacheConfig.properties.getProperty("cacheType", ECacheType.map_string.name()));
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		int cacheCount = 0;
		try {
			conn = dataSource.getConnection();
			stmt = conn.createStatement();
			for (final String sql : inConfig.cacheList) {
				if (cacheConfig.cacheList.contains(sql))// 尝试加入缓存时,已经存在了该相同SQL的定义,不能重复加入
				{
					log.warn("尝试加入缓存时,已经存在了该相同SQL的定义,不能重复加入");
					continue;
				} else
					cacheConfig.cacheList.add(sql);
				try {
					rs = stmt.executeQuery(sql);
					caches.safeGetCache(cacheName, cacheConfig.properties);
					final PrivateCacheRowMapper rowMapper = new PrivateCacheRowMapper(cacheName, caches, cacheType);
					while (rs.next()) {
						rowMapper.mapRow(rs);
						cacheCount = cacheCount + 1;
					}
				} catch (final Exception ex) {
					ex.printStackTrace();//for debug
				} finally {
					//close resultset every sql is finished.
					SQLUtils.closeAll(rs, null, null);
				}
			}

		} catch (final Exception e) {
		} finally {
			SQLUtils.closeAll(rs, stmt, conn);
			final CacheState cacheState = cacheStateMaps.get(cacheName);
			cacheState.eCacheState = ECacheState.loaded;
			cacheState.cacheCount = cacheCount;
		}
	}

	private static class PrivateCacheRowMapper {
		static final String CACHEID = "CACHEID";
		Collection<String> headColl;
		int headSize;
		final String cacheName;
		final ACaches caches;
		final ECacheType cacheType;

		public PrivateCacheRowMapper(final String cacheName, final ACaches caches, final ECacheType cacheType) {
			this.cacheName = cacheName;
			this.caches = caches;
			this.cacheType = cacheType;
		}

		private String tmpValue;
		private Map<String, String> map;
		private Map<String, Map<String, String>> mapMap;

		public Object mapRow(final ResultSet rs) throws SQLException {
			if (null == headColl) {
				final ResultSetMetaData rsmd = rs.getMetaData();
				headSize = rsmd.getColumnCount() + 1;
				headColl = new ArrayList<String>(headSize);
				String tmpCol;
				for (int i = 1; i < headSize; i++) {
					//Mysql获得字段名是小写,应用程序里用到的是大写,需要转换
					tmpCol = rsmd.getColumnLabel(i).toUpperCase();
					if (PrivateCacheRowMapper.CACHEID.equals(tmpCol))
						continue;
					headColl.add(tmpCol);
				}
			}
			final String cacheId = rs.getString(PrivateCacheRowMapper.CACHEID);
			if (StringUtil.isEmpty(cacheId))
				return null;
			switch (cacheType) {
			case string: {
				tmpValue = rs.getString("CACHEVALUE");
				if (null != tmpValue)
					caches.setCacheValue(cacheName, cacheId, tmpValue);
				break;
			}
			case map_string: {
				map = caches.getMapValue(cacheName, cacheId);
				if (null == map)
					map = new HashMap<String, String>(headSize);
				for (final String headCol : headColl) {
					tmpValue = rs.getString(headCol);
					if (null != tmpValue)
						map.put(headCol, tmpValue);
				}
				caches.setCacheValue(cacheName, cacheId, map);
				break;
			}
			case map_map_string: {
				mapMap = caches.getMapMapValue(cacheName, cacheId);
				if (null == mapMap)
					mapMap = new LinkedHashMap<String, Map<String, String>>();
				final String dataId = rs.getString("DATAID");
				map = mapMap.get(dataId);
				if (null == map)
					mapMap.put(dataId, map = new HashMap<String, String>(headSize));
				for (final String headCol : headColl) {
					tmpValue = rs.getString(headCol);
					if (null != tmpValue)
						map.put(headCol, tmpValue);
				}
				caches.setCacheValue(cacheName, cacheId, mapMap);
				break;
			}
			}
			return null;
		}
	}

	private static final Collection<String> EMP_KEYS = new ArrayList<String>(0);

	/**
	 * 获得某一种类型的Cache的Key列表
	 * 
	 * @param domain
	 * @param cacheName
	 * @return
	 */
	public static final Collection<String> keys(final String cacheName) {
		final Caches caches = Caches.instance;
		final CacheConfig config = caches.configs.get(cacheName);
		if (null == config)
			return Caches.EMP_KEYS;
		return Caches.instance.getCachesImpl(config.cacheEngine).keysCache(cacheName);
	}

	/**
	 * 获得给定缓存类型和实际对象Key对应的缓存数据
	 * 
	 * @see #getString(String, String, String)
	 * @see #getMap(String, String, String)
	 * @see #getMapMap(String, String, String)
	 * @param cacheName
	 * @param cacheKey
	 * @return
	 */
	public static final Object get(final String cacheName, final String cacheKey) {
		final Caches caches = Caches.instance;
		final CacheConfig config = caches.configs.get(cacheName);
		if (null == config)
			return null;
		return Caches.instance.getCachesImpl(config.cacheEngine).getCacheValue(cacheName, cacheKey);
	}

	/**
	 * 允许手动添加一个缓存数据对象
	 * 
	 * @param cacheName
	 * @param cacheKey
	 * @param cacheObject
	 */
	public static final void set(final String cacheName, final String cacheKey, final Object cacheObject) {
		final Caches caches = Caches.instance;
		final CacheConfig config = caches.configs.get(cacheName);
		if (null == config)
			return;
		Caches.instance.getCachesImpl(config.cacheEngine).setCacheValue(cacheName, cacheKey, cacheObject);
	}

	/**
	 * 获得给定缓存类型和实际对象Key对应的缓存数据,只是一个String对象
	 * 
	 * @param cacheName
	 * @param cacheKey
	 * @return
	 */
	public static final String getString(final String cacheName, final String cacheKey) {
		//cacheKey为null时会有异常抛出
		if (cacheKey == null) {
			return cacheKey;
		}
		final Caches caches = Caches.instance;
		final CacheConfig config = caches.configs.get(cacheName);
		if (null == config)
			return null;
		return Caches.instance.getCachesImpl(config.cacheEngine).getStringValue(cacheName, cacheKey);
	}

	/**
	 * 获得给定缓存类型和实际对象Key对应的缓存数据,只是一个Map&lt;String,String&gt;对象
	 * 
	 * @param cacheName
	 * @param cacheKey
	 * @return
	 */
	public static final Map<String, String> getMap(final String cacheName, final String cacheKey) {
		//cacheKey为null时会有异常抛出
		if (cacheKey == null) {
			return null;
		}
		final Caches caches = Caches.instance;
		final CacheConfig config = caches.configs.get(cacheName);
		if (null == config)
			return null;
		return Caches.instance.getCachesImpl(config.cacheEngine).getMapValue(cacheName, cacheKey);
	}

	/**
	 * 获得给定缓存类型和实际对象Key对应的缓存数据,只是一个Map&lt;String,Map&lt;String,String&gt;&gt;对象
	 * 
	 * @param cacheName
	 * @param cacheKey
	 * @return
	 */
	public static final Map<String, Map<String, String>> getMapMap(final String cacheName, final String cacheKey) {
		//cacheKey为null时会有异常抛出
		if (cacheKey == null) {
			return null;
		}
		final Caches caches = Caches.instance;
		final CacheConfig config = caches.configs.get(cacheName);
		if (null == config)
			return null;
		return Caches.instance.getCachesImpl(config.cacheEngine).getMapMapValue(cacheName, cacheKey);
	}

	/**
	 * 允许应用重新加载一个缓存.如果指定的缓存名称不存在,将不做操作.
	 * @param cacheName
	 * @param dataSource
	 * @return
	 */
	public static final boolean refreshCache(final String cacheName, final DataSource dataSource) {
		final Caches caches = Caches.instance;
		final CacheConfig config = caches.configs.get(cacheName);
		if (null == config)
			return false;
		remove(cacheName);
		caches.registryMap(dataSource, config);
		return true;
	}
}
