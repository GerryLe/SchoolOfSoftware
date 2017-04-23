package com.rosense.basic.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
* @author 黄家乐
 * 	
 * 2017年3月20日 
 *
 */
public class SQLUtils {

	/**
	 * 
	 * @param jdbcResultSet
	 * @param statement
	 * @param connection @ deprecated 本方法只适合关闭普通的连接，考虑到系统中将所有由Spring创建的数据库连接监制到，
	 *            使用 {@link #closeAllForPool(ResultSet, Statement, Connection)}
	 */
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

}
