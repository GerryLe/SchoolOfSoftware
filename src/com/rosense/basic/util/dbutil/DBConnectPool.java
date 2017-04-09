package com.rosense.basic.util.dbutil;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;

/**
 * 数据源
 * @author Can-Dao
 * 	
 * 2015年8月18日 下午8:15:55
 *
 */
public class DBConnectPool {

	private static DBConnectPool databasePool = null;
	private static DataSource dds = null;
	static {
		try {
			Properties properties = loadPropertyFile("db.properties");
			dds = BasicDataSourceFactory.createDataSource(properties);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private DBConnectPool() {
	}

	public static synchronized DBConnectPool getInstance() {
		if (null == databasePool) {
			databasePool = new DBConnectPool();
		}
		return databasePool;
	}

	public DataSource getDataSource() {
		return dds;
	}

	public Connection getConnection() throws SQLException {
		return dds.getConnection();
	}

	public static void closeAll(final ResultSet rs, Statement st, final Connection conn) {
		try {
			if (rs != null)
				rs.close();
		} catch (final SQLException e) {
		}
		try {
			if (st != null)
				st.close();
		} catch (final SQLException e) {
			st = null;
		}
		try {
			if (conn != null)
				conn.close();
		} catch (final SQLException e) {
		}
	}

	public static Properties loadPropertyFile(String fullFile) {
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			InputStream resourceAsStream = loader.getResourceAsStream("config/" + fullFile);
			Properties p = System.getProperties();
			p.load(resourceAsStream);
			return p;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}