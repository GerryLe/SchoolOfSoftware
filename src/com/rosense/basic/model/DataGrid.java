package com.rosense.basic.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author 黄家乐
 *
 */
public class DataGrid {

	/** 总记录数 */
	private Long total;

	/** 每行记录 */
	private List<?> rows;

	private List<?> footer;

	public List<?> data;

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public List<?> getRows() {
		if (rows == null) {
			rows = new ArrayList();
		}
		return rows;
	}

	public void setRows(List<?> rows) {
		this.rows = rows;
	}

	public List<?> getFooter() {
		if (footer == null) {
			footer = new ArrayList();
		}
		return footer;
	}

	public void setFooter(List<?> footer) {
		this.footer = footer;
	}

	public List<?> getData() {
		if (data == null) {
			data = new ArrayList();
		}
		return data;
	}

	public void setData(List<?> data) {
		this.data = data;
	}

}
