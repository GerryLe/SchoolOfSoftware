package com.rosense.basic.util.dbutil;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.springframework.stereotype.Component;

/**
 * 
 * @author Can-Dao
 * 	
 * 2015年8月18日 下午8:16:01
 *
 */
@Component("dbutil")
public class DBUtilsHelper implements IDBUtilsHelper {
	private DataSource ds = null;
	private QueryRunner qr = null;

	public DBUtilsHelper() {
		try {
			this.ds = DBConnectPool.getInstance().getDataSource();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (this.ds != null) {
			this.qr = new QueryRunner(this.ds);
		}
	}

	public DBUtilsHelper(DataSource ds) {
		this.ds = ds;
		this.qr = new QueryRunner(this.ds);
	}

	public String getDataBase() {
		String database = null;
		try {
			Connection conn = this.ds.getConnection();
			database = conn.getCatalog();
			conn.close();
			return database;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public QueryRunner getQr() {
		return this.qr;
	}
}
