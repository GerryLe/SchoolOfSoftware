package com.rosense.basic.model;

import java.util.Date;

import net.sf.json.JSONObject;

/**
 * 分页数据模型
 * 用于接收客户端的参数
 *
 */
public class PageHelper {
	private String sessionId;//回话
	private String id; //唯一标示
	private String ids; //ID集合（删除）
	private int offset; // 当前页
	private int limit; // 每页显示记录数
	private String sort; // 排序字段名
	private String order; // 按什么排序(asc,desc)
	private Date created; //创建日期
	private String formatDate;//格式化时间
	private int rowNum;//行记录
	private long total;//总记录
	private String userId;//用户ID
	private String userName;//用户名称
	private String filter;//获取过滤器
	private JSONObject jsonObject;
	private String search;//搜索内容
	private String select;//搜索元素
	
	
	public String getSelect() {
		return select;
	}

	public void setSelect(String select) {
		this.select = select;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public String getFilter() {
		if (filter != null && jsonObject == null) {
			jsonObject = JSONObject.fromObject(filter);
		}
		try {
			if (jsonObject != null)
				for (Object key : jsonObject.keySet()) {
					Object value = jsonObject.get(key);
					try {
						org.apache.commons.beanutils.BeanUtils.setProperty(this, key.toString(), value);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
		} catch (Exception e) {
		}
		return filter;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public long getTotal() {
		return total;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	public int getRowNum() {
		return rowNum;
	}

	public void setFormatDate(String formatDate) {
		this.formatDate = formatDate;
	}

	public String getFormatDate() {
		return formatDate;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		if (offset < 0)
			SystemContext.setPage(1);
		else
			SystemContext.setPage(Math.round(offset / limit) + 1);
		this.offset = offset;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		if (limit < 0)
			SystemContext.setRows(15);
		else
			SystemContext.setRows(limit);
		this.limit = limit;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

}
