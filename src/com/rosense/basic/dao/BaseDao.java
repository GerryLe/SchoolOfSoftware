/**
 * 
 */
package com.rosense.basic.dao;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.rosense.basic.model.Pager;
import com.rosense.basic.model.SystemContext;
import com.rosense.basic.util.dbutil.IDBUtilsHelper;

/**
 * @author 黄家乐
 *
 */
@SuppressWarnings("unchecked")
@Repository
public class BaseDao<T> implements IBaseDao<T> {
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private IDBUtilsHelper dbHelper;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public IDBUtilsHelper getDbHelper() {
		return dbHelper;
	}

	public void setDbHelper(IDBUtilsHelper dbHelper) {
		this.dbHelper = dbHelper;
	}

	
	public Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	
	public T add(T entity) {
		this.getCurrentSession().save(entity);
		return entity;
	}

	
	public void update(T entity) {
		this.getCurrentSession().update(entity);
	}

	
	public void delete(Class<T> clazz, Serializable id) {
		this.getCurrentSession().delete(this.load(clazz, id));
	}

	
	public void delete(T entity) {
		this.getCurrentSession().delete(entity);
	}

	
	public T load(Class<T> clazz, Serializable id) {
		T t = null;
		try {
			t = (T) this.getCurrentSession().load(clazz, id);
			t.toString();
		} catch (Exception e) {
			return null;
		}
		return t;
	}

	
	public List<T> list(String hql, Object[] args) {
		return this.list(hql, args, null);
	}

	
	public List<T> list(String hql, Object arg) {
		return this.list(hql, new Object[] { arg });
	}

	
	public List<T> list(String hql) {
		return this.list(hql, null, null);
	}

	private String initSort(String hql) {
		String sort = SystemContext.getSort();
		String order = SystemContext.getOrder();
		if (sort != null && !"".equals(sort.trim())) {
			hql += " order by " + sort;
			if (!"desc".equalsIgnoreCase(order))
				hql += " asc";
			else
				hql += " desc";
		}
		SystemContext.removeSort();
		SystemContext.removeOrder();
		return hql;
	}

	@SuppressWarnings("rawtypes")
	private void setAliasParams(Query query, Map<String, Object> alias) {
		if (alias != null) {
			Set<String> keys = alias.keySet();
			for (String key : keys) {
				Object val = alias.get(key);

				if (val instanceof Integer[]) {
					//查询条件是列表
					query.setParameterList(key, (Integer[]) val);
				} else if (val instanceof String[]) {
					//查询条件是列表
					query.setParameterList(key, (String[]) val);
				} else if (val instanceof List) {
					//查询条件是列表
					query.setParameterList(key, (List<Object>) val);
				} else if (val instanceof Collection) {
					//查询条件是列表
					query.setParameterList(key, (Collection) val);
				} else {
					query.setParameter(key, val);
				}
			}
		}
	}

	private void setArgsParams(Query query, Object[] args) {
		if (args != null && args.length > 0) {
			int index = 0;
			for (Object arg : args) {
				query.setParameter(index++, arg);
			}
		}
	}

	
	public List<T> list(String hql, Object[] args, Map<String, Object> alias) {
		hql = initSort(hql);

		Query query = this.getCurrentSession().createQuery(hql);
		setAliasParams(query, alias);
		setArgsParams(query, args);

		return query.list();
	}

	
	public List<T> list(String hql, Map<String, Object> alias) {
		return this.list(hql, null, alias);
	}

	
	public Pager<T> find(String hql, Object[] args) {
		return this.find(hql, args, null);
	}

	
	public Pager<T> find(String hql, Object arg) {
		return this.find(hql, new Object[] { arg });
	}

	
	public Pager<T> find(String hql) {
		return this.find(hql, null, null);
	}

	@SuppressWarnings("rawtypes")
	private void setPagers(Query query, Pager pager) {
		Integer page = SystemContext.getPage();
		Integer rows = SystemContext.getRows();
		if (page == null || page < 0)
			page = 1;
		if (rows == null || rows < 0)
			rows = 30;
		pager.setPage(page);
		pager.setRows(rows);
		query.setFirstResult((page - 1) * rows).setMaxResults(rows);
	}

	
	public Pager<T> find(String hql, Object[] args, Map<String, Object> alias) {
		Pager<T> pager = new Pager<T>();

		hql = initSort(hql);

		Query query = this.getCurrentSession().createQuery(hql);
		setPagers(query, pager);
		setAliasParams(query, alias);
		setArgsParams(query, args);

		pager.setDataRows(query.list());
		pager.setTotal(this.count(hql, args, alias, true));

		return pager;
	}

	
	public Pager<T> find(String hql, Map<String, Object> alias) {
		return this.find(hql, null, alias);
	}

	
	public Object queryObject(String hql, Object[] args) {
		return this.queryObject(hql, args, null);
	}

	
	public Object queryObject(String hql, Object[] args, Map<String, Object> alias) {
		Query query = this.getCurrentSession().createQuery(hql);
		setAliasParams(query, alias);
		setArgsParams(query, args);
		return query.uniqueResult();
	}

	
	public Object queryObject(String hql, Map<String, Object> alias) {
		return this.queryObject(hql, null, alias);
	}

	
	public Object queryObject(String hql, Object arg) {
		return this.queryObject(hql, new Object[] { arg }, null);
	}

	
	public Object queryObject(String hql) {
		return this.queryObject(hql, null, null);
	}

	
	public void updateByHql(String hql, Object[] args, Map<String, Object> alias) {
		Query query = this.getCurrentSession().createQuery(hql);
		setAliasParams(query, alias);
		setArgsParams(query, args);
		query.executeUpdate();
	}

	
	public void updateByHql(String hql, Map<String, Object> alias) {
		this.updateByHql(hql, null, alias);
	}

	
	public void updateByHql(String hql, Object[] args) {
		this.updateByHql(hql, args, null);
	}

	
	public void updateByHql(String hql, Object arg) {
		this.updateByHql(hql, new Object[] { arg }, null);
	}

	
	public void updateByHql(String hql) {
		this.updateByHql(hql, null, null);
	}

	
	public Object queryObjectSQL(String sql, Object[] args, Class<?> clz, boolean hasEntity) {
		return this.queryObjectSQL(sql, args, null, clz, hasEntity);
	}

	
	public Object queryObjectSQL(String sql, Object arg, Class<?> clz, boolean hasEntity) {
		return this.queryObjectSQL(sql, new Object[] { arg }, null, clz, hasEntity);
	}

	
	public Object queryObjectSQL(String sql, Class<?> clz, boolean hasEntity) {
		return this.queryObjectSQL(sql, null, null, clz, hasEntity);
	}

	
	public Object queryObjectSQL(String sql, Object[] args, Map<String, Object> alias, Class<?> clz, boolean hasEntity) {
		SQLQuery sqlQuery = this.getCurrentSession().createSQLQuery(sql);
		setAliasParams(sqlQuery, alias);
		setArgsParams(sqlQuery, args);
		if (hasEntity) {
			sqlQuery.addEntity(clz);
		} else {
			//			sqlQuery.setResultTransformer(Transformers.aliasToBean(clz));
			sqlQuery.setResultTransformer(new BeanTransformerAdapter(clz));
		}
		return sqlQuery.uniqueResult();
	}

	
	public Object queryObjectSQL(String sql, Map<String, Object> alias, Class<?> clz, boolean hasEntity) {
		return this.queryObjectSQL(sql, null, alias, clz, hasEntity);
	}

	
	public Object[] queryObjectSQL(String sql, Object[] args) {
		return this.queryObjectSQL(sql, args, null);
	}

	
	public Object[] queryObjectSQL(String sql, Object arg) {
		return this.queryObjectSQL(sql, new Object[] { arg }, null);
	}

	
	public Object[] queryObjectSQL(String sql) {
		return this.queryObjectSQL(sql, null, null);
	}

	
	public Object[] queryObjectSQL(String sql, Object[] args, Map<String, Object> alias) {
		SQLQuery sqlQuery = this.getCurrentSession().createSQLQuery(sql);
		setAliasParams(sqlQuery, alias);
		setArgsParams(sqlQuery, args);
		return (Object[]) sqlQuery.uniqueResult();
	}

	
	public Object[] queryObjectSQL(String sql, Map<String, Object> alias) {
		return this.queryObjectSQL(sql, null, alias);
	}

	
	public <N extends Object> List<N> listSQL(String sql, Object[] args, Class<?> clz, boolean hasEntity) {
		return this.listSQL(sql, args, null, clz, hasEntity);
	}

	
	public <N extends Object> List<N> listSQL(String sql, Object arg, Class<?> clz, boolean hasEntity) {
		return this.listSQL(sql, new Object[] { arg }, null, clz, hasEntity);
	}

	
	public <N extends Object> List<N> listSQL(String sql, Class<?> clz, boolean hasEntity) {
		return this.listSQL(sql, null, null, clz, hasEntity);
	}

	
	public <N extends Object> List<N> listSQL(String sql, Object[] args, Map<String, Object> alias, Class<?> clz, boolean hasEntity) {
		sql = initSort(sql);
		SQLQuery sqlQuery = this.getCurrentSession().createSQLQuery(sql);
		setAliasParams(sqlQuery, alias);
		setArgsParams(sqlQuery, args);
		if (hasEntity) {
			sqlQuery.addEntity(clz);
		} else {
			sqlQuery.setResultTransformer(new BeanTransformerAdapter(clz));
		}
		return sqlQuery.list();
	}

	
	public <N extends Object> List<N> listSQL(String sql, Map<String, Object> alias, Class<?> clz, boolean hasEntity) {
		return this.listSQL(sql, null, alias, clz, hasEntity);
	}

	
	public List<Object[]> listSQL(String sql, Object[] args, Map<String, Object> alias) {
		SQLQuery sqlQuery = this.getCurrentSession().createSQLQuery(sql);
		setAliasParams(sqlQuery, alias);
		setArgsParams(sqlQuery, args);
		return sqlQuery.list();
	}

	
	public List<Object[]> listSQL(String sql, Map<String, Object> alias) {
		return this.listSQL(sql, null, alias);
	}

	
	public List<Object[]> listSQL(String sql, Object[] args) {
		return this.listSQL(sql, args, null);
	}

	
	public List<Object[]> listSQL(String sql, Object arg) {
		return this.listSQL(sql, new Object[] { arg });
	}

	
	public <N extends Object> Pager<N> findSQL(String sql, Object[] args, Class<?> clz, boolean hasEntity) {
		return this.findSQL(sql, args, null, clz, hasEntity);
	}

	
	public <N extends Object> Pager<N> findSQL(String sql, Object arg, Class<?> clz, boolean hasEntity) {
		return this.findSQL(sql, new Object[] { arg }, null, clz, hasEntity);
	}

	
	public <N extends Object> Pager<N> findSQL(String sql, Class<?> clz, boolean hasEntity) {
		return this.findSQL(sql, null, null, clz, hasEntity);
	}

	
	public <N extends Object> Pager<N> findSQL(String sql, Object[] args, Map<String, Object> alias, Class<?> clz, boolean hasEntity) {
		Pager<N> pager = new Pager<N>();

		sql = initSort(sql);
		SQLQuery sqlQuery = this.getCurrentSession().createSQLQuery(sql);
		setAliasParams(sqlQuery, alias);
		setArgsParams(sqlQuery, args);
		setPagers(sqlQuery, pager);
		if (hasEntity) {
			sqlQuery.addEntity(clz);
		} else {
			sqlQuery.setResultTransformer(new BeanTransformerAdapter(clz));
		}
		pager.setDataRows(sqlQuery.list());
		pager.setTotal(this.countSQL(sql, args, alias, false).longValue());

		return pager;
	}

	
	public <N extends Object> Pager<N> findSQL(String sql, Map<String, Object> alias, Class<?> clz, boolean hasEntity) {
		return this.findSQL(sql, null, alias, clz, hasEntity);
	}

	
	public int executeSQL(String sql, Object[] args) {
		return this.executeSQL(sql, args, null);
	}

	
	public int executeSQL(String sql, Object arg) {
		return this.executeSQL(sql, new Object[] { arg }, null);
	}

	
	public int executeSQL(String sql) {
		return this.executeSQL(sql, null, null);
	}

	
	public int executeSQL(String sql, Object[] args, Map<String, Object> alias) {
		try {
			SQLQuery sqlQuery = this.getCurrentSession().createSQLQuery(sql);
			setAliasParams(sqlQuery, alias);
			setArgsParams(sqlQuery, args);
			return sqlQuery.executeUpdate();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return 0;
	}

	
	public int executeSQL(String sql, Map<String, Object> alias) {
		return this.executeSQL(sql, null, alias);
	}

	private String getCountHql(String hql, boolean isHQL) {
		String e = hql.substring(hql.lastIndexOf("from"));
		String c = "select count(*) " + e;
		if (isHQL)
			c.replaceAll("fetch", "");
		return c;
	}

	
	public Long count(String hql, Object[] args, Map<String, Object> alias, boolean isHQL) {
		hql = getCountHql(hql, isHQL);
		Query query = this.getCurrentSession().createQuery(hql);
		setAliasParams(query, alias);
		setArgsParams(query, args);
		return (Long) query.uniqueResult();
	}

	
	public Long count(String hql, Object[] args, boolean isHQL) {
		return this.count(hql, args, null, isHQL);
	}

	
	public Long count(String hql, Map<String, Object> alias, boolean isHQL) {
		return this.count(hql, null, alias, isHQL);
	}

	
	public Long count(String hql, boolean isHQL) {
		return this.count(hql, null, null, isHQL);
	}

	private String getCountSql(String hql, boolean isHQL) {
		String e = hql.substring(hql.indexOf("from"));
		String c = "select count(*) " + e;
		if (isHQL)
			c.replaceAll("fetch", "");
		return c;
	}

	
	public BigInteger countSQL(String sql, Object[] args, Map<String, Object> alias, boolean isHQL) {
		sql = getCountSql(sql, isHQL);
		SQLQuery sqlQuery = this.getCurrentSession().createSQLQuery(sql);
		setAliasParams(sqlQuery, alias);
		setArgsParams(sqlQuery, args);
		return (BigInteger) sqlQuery.uniqueResult();
	}

	
	public BigInteger countSQL(String sql, Object[] args, boolean isHQL) {
		return this.countSQL(sql, args, null, isHQL);
	}

	
	public BigInteger countSQL(String sql, Map<String, Object> alias, boolean isHQL) {
		return this.countSQL(sql, null, alias, isHQL);
	}

	
	public BigInteger countSQL(String sql, boolean isHQL) {
		return this.countSQL(sql, null, null, isHQL);
	}

}
