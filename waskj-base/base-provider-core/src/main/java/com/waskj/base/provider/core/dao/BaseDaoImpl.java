package com.waskj.base.provider.core.dao;

import com.waskj.base.core.util.QueryFilter;
import com.waskj.base.domain.core.AuditableEntity;
import com.waskj.base.domain.core.BaseEntity;
import com.waskj.base.provider.core.util.SqlUtil;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * 基础数据库操作
 *
 * http://git.oschina.net/sphsyv/sypro
 *
 * @author 孙宇
 *
 * @param <T>
 *            操作对象
 * @param <PK>
 *            主键
 */
public abstract class BaseDaoImpl<T extends BaseEntity, PK extends Serializable> implements BaseDao<T, PK> {

	private static final Logger log = LoggerFactory.getLogger(BaseDaoImpl.class);

	private static final String idType = "AUTO";

	private static final String batchSize = "5000";

	/**
	 * 注入hibernate sessionFactory
	 */
	@Resource(name = "sessionFactory")
	private SessionFactory hiSessionFactory;

	private final Class<T> entityClass;// 泛型类的class
	// private final String tableName;// 用于sql的表名

	private final String hql;// 查询语句
	private final String countHql;// 统计语句
	public static final String JOINFETCH = " LEFT JOIN FETCH ";// 预先抓取关键字
	public static final String JOIN = " LEFT JOIN ";// 级联查询关键字
	public static final String RIGHTJOINFETCH = " RIGHT JOIN FETCH ";
	public static final String RIGHTJOIN = " RIGHT JOIN ";

	public String getCountHql() {
		return countHql;
	}

	public String getHql() {
		return hql;
	}

	/**
	 * 构造时，告诉baseDao要查询表的类型，并且初始化查询和统计语句
	 */
	@SuppressWarnings("unchecked")
	public BaseDaoImpl() {
		this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
		this.hql = " SELECT DISTINCT t FROM " + this.entityClass.getSimpleName() + " t ";
		this.countHql = " SELECT COUNT(DISTINCT t) FROM " + this.entityClass.getSimpleName() + " t ";
		// Table t =
		// this.entityClass.getAnnotation(javax.persistence.Table.class);
		// this.tableName = (String) Mirror.me(t).getValue(t, "name");
	}

	/**
	 * 由于hibernate4必须开启session，所以通过这个方法获得当前线程的session
	 *
	 * @return
	 */
	public Session getCurrentSession() {
		return hiSessionFactory.getCurrentSession();
	}

	/**
	 * 更改hql和添加参数
	 *
	 * 调用方法后，hql的条件会变成 and t.xxx like :t_xxx这种形式，并且添加了params
	 *
	 * 如果有预先抓取参数，则hql还会加上例如 join fetch t.company as company类似的语句
	 *
	 * 如果有关联参数，hql还会加上例如join t.company as company类似语句
	 *
	 * @param filter
	 *            过滤器
	 * @param params
	 *            参数
	 * @param hql
	 *            查询语句
	 * @return
	 */
	public String changeHqlAndParams(QueryFilter filter, Map<String, Object> params, String hql) {
		if (filter.getJoinFetch() != null && filter.getJoinFetch().length > 0) {// 如果有预先抓取需求，这拼装hql
			for (String joinFetch : filter.getJoinFetch()) {
				if (joinFetch.indexOf("#") > -1) {// 如果有井号，说明有自定义别名
					hql += " " + JOINFETCH + " " + joinFetch.substring(0, joinFetch.indexOf("#")) + " AS "
							+ joinFetch.substring(joinFetch.indexOf("#") + 1) + " ";
				} else {
					hql += " " + JOINFETCH + " " + joinFetch + " AS " + joinFetch.substring(joinFetch.indexOf(".") + 1)
							+ " ";// left join fetch t.company as company
				}
			}
		}
		if (filter.getRightJoinFetch() != null && filter.getRightJoinFetch().length > 0) {
			for (String rightJoinFetch : filter.getRightJoinFetch()) {
				if (rightJoinFetch.indexOf("#") > -1) {// 如果有井号，说明有自定义别名
					hql += " " + RIGHTJOINFETCH + " " + rightJoinFetch.substring(0, rightJoinFetch.indexOf("#"))
							+ " AS " + rightJoinFetch.substring(rightJoinFetch.indexOf("#") + 1) + " ";
				} else {
					hql += " " + RIGHTJOINFETCH + " " + rightJoinFetch + " AS "
							+ rightJoinFetch.substring(rightJoinFetch.indexOf(".") + 1) + " ";
				}
			}
		}
		if (filter.getJoin() != null && filter.getJoin().length > 0) {// 如果有关联需要，拼装hql
			for (String join : filter.getJoin()) {
				if (join.indexOf("#") > -1) {// 如果有井号，说明有自定义别名
					hql += " " + JOIN + " " + join.substring(0, join.indexOf("#")) + " AS "
							+ join.substring(join.indexOf("#") + 1) + " ";
				} else {
					hql += " " + JOIN + " " + join + " AS " + join.substring(join.indexOf(".") + 1) + " ";
				}
			}
		}
		if (filter.getRightJoin() != null && filter.getRightJoin().length > 0) {// 如果有关联需要，拼装hql
			for (String rightJoin : filter.getRightJoin()) {
				if (rightJoin.indexOf("#") > -1) {// 如果有井号，说明有自定义别名
					hql += " " + RIGHTJOIN + " " + rightJoin.substring(0, rightJoin.indexOf("#")) + " AS "
							+ rightJoin.substring(rightJoin.indexOf("#") + 1) + " ";
				} else {
					hql += " " + RIGHTJOIN + " " + rightJoin + " AS " + rightJoin.substring(rightJoin.indexOf(".") + 1)
							+ " ";// right join t.company as company
				}
			}
		}
		hql += " WHERE 1=1 ";
		if (filter.getParams().size() > 0) {
			int paramIndex = 0;// 参数占位符索引
			for (Object[] o : filter.getParams()) {
				if (o[1].toString().equalsIgnoreCase("In") || o[1].toString().equalsIgnoreCase("Not In")) {
					@SuppressWarnings("unchecked")
					List<Object> values = (ArrayList<Object>) o[2];
					String inParamString = "";
					for (int i = 0; i < values.size(); i++) {
						if (i > 0) {
							inParamString += " , ";
						}
						++paramIndex;// 增加参数占位符索引
						String paramName = "_" + paramIndex;
						inParamString += " :" + paramName + " ";
						params.put(paramName, values.get(i));
					}
					if (o[1].toString().equalsIgnoreCase("In")) {
						hql += " AND " + o[0] + " IN (" + inParamString + ") ";
					}
					if (o[1].toString().equalsIgnoreCase("Not In")) {
						hql += " AND " + o[0] + " NOT IN (" + inParamString + ") ";
					}
				} else if (o[1].toString().equalsIgnoreCase("Is Null")) {
					hql += " AND " + o[0] + " IS NULL ";
				} else if (o[1].toString().equalsIgnoreCase("Is Not Null")) {
					hql += " AND " + o[0] + " IS NOT NULL ";
				} else {
					++paramIndex;// 增加参数占位符索引
					String paramName = "_" + paramIndex;
					hql += " AND " + o[0] + " " + o[1] + " :" + paramName + " ";
					params.put(paramName, o[2]);
				}
			}
		}
		return hql;
	}

	@Override
	public Serializable save(T t) {
		if (StringUtils.equalsIgnoreCase("uuid", idType)) {
			Method method = ReflectionUtils.findMethod(t.getClass(), "setId", String.class);
			try {
				method.invoke(t, UUID.randomUUID().toString());
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if( isAuditable(t) ){
			AuditableEntity ae = (AuditableEntity)t;
            Date date = new Date();
			ae.setCreatedDate(date);
            ae.setLastModifiedDate(date);
		}

		return this.getCurrentSession().save(t);
	}

	@Override
	public void saveAll(List<T> l) {
		if (l != null && l.size() > 0) {
			for (T t:l) {
				this.save(t);
			}
		}
	}

	@Override
	public void saveOrUpdate(T t) {
		this.getCurrentSession().saveOrUpdate(t);
	}

	@Override
	public void merge(T t) {
		this.getCurrentSession().merge(t);
	}

	@Override
	public void delete(T t) {
		// Mirror<?> mirror = Mirror.me(t);
		// try {
		// if (mirror.getField("id") != null && mirror.getValue(t, "id") !=
		// null) {
		// this.executeHql("DELETE " + this.entityClass.getSimpleName() + " t
		// WHERE t.id = " + mirror.getValue(t, "id"));
		// // this.executeSql("DELETE FROM " + this.tableName + " WHERE ID = " +
		// mirror.getValue(t, "id"));
		// } else {
		// this.getCurrentSession().delete(t);
		// }
		// } catch (NoSuchFieldException e) {
		// } catch (FailToGetValueException e) {
		// }
		this.getCurrentSession().delete(t);
	}

	@Override
	public void update(T t) {
		if( isAuditable(t) ){
			((AuditableEntity)t).setLastModifiedDate(new Date());
		}
		this.getCurrentSession().update(t);
	}

	@Override
	public T get(PK pk) {
		return (T) this.getCurrentSession().get(this.entityClass, pk);
	}

	@Override
	public T load(PK pk) {
		return (T) this.getCurrentSession().load(this.entityClass, pk);
	}

	@Override
	public T get(QueryFilter filter) {
		return this.get(filter, this.getHql());
	}

	@Override
	public T get(QueryFilter filter, String newHql) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = newHql;
		hql = changeHqlAndParams(filter, params, hql);
		return this.getByHql(hql, params);
	}

	@Override
	public T getByHql(String hql) {
		return this.getByHql(hql, null);
	}

	@Override
	public T getByHql(String hql, Map<String, Object> params) {
		Query q = this.getCurrentSession().createQuery(hql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				q.setParameter(key, params.get(key));
			}
		}
		List<T> l = q.setFirstResult((1 - 1) * 1).setMaxResults(1).list();
		if (l != null && l.size() > 0) {
			return l.get(0);
		}
		return null;
	}

	@Override
	public List<T> find(String hql) {
		return this.find(hql, null, null, null);
	}

	@Override
	public List<T> find() {
		return this.find(this.getHql(), null, null, null);
	}

	@Override
	public List<T> find(String hql, Map<String, Object> params) {
		return this.find(hql, params, null, null);
	}

	@Override
	public List<T> find(Map<String, Object> params) {
		return this.find(this.getHql(), params, null, null);
	}

	@Override
	public List<T> find(QueryFilter filter) {
		return this.find(filter, this.getHql());
	}

	@Override
	public List<T> find(QueryFilter filter, String newHql) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = newHql;
		hql = changeHqlAndParams(filter, params, hql);
		if (StringUtils.isNotBlank(filter.getSort())) {// 如果有排序需求，这拼装排序条件
			hql += " ORDER BY " + filter.getSort();
		}
		return this.find(hql, params, filter.getPage(), filter.getPageSize());
	}

	@Override
	public List<T> find(String hql, Integer page, Integer pageSize) {
		return this.find(hql, null, page, pageSize);
	}

	@Override
	public List<T> find(String hql, Map<String, Object> params, Integer page, Integer pageSize) {
		Query q = getCurrentSession().createQuery(hql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				q.setParameter(key, params.get(key));
			}
		}
		if (page == null || pageSize == null) {
			return q.list();
		}
		return q.setFirstResult((page - 1) * pageSize).setMaxResults(pageSize).list();
	}

	@Override
	public Long count(String hql) {
		return this.count(hql, null);
	}

	@Override
	public Long count() {
		return this.count(this.getCountHql());
	}

	@Override
	public Long count(String hql, Map<String, Object> params) {
		Query q = this.getCurrentSession().createQuery(hql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				q.setParameter(key, params.get(key));
			}
		}
		List<Long> list = q.list();
		return list.size() > 0 ? list.get(0) : 0L;
	}

	@Override
	public Long count(QueryFilter filter) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = this.getCountHql();
		hql = changeHqlAndParams(filter, params, hql);
		return this.count(hql.replaceAll(RIGHTJOINFETCH, RIGHTJOIN).replaceAll(JOINFETCH, JOIN), params);// 统计是不可以预先抓取的，所以要更换hql
	}

	@Override
	public Long count(QueryFilter filter, String newHql) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = newHql;
		hql = changeHqlAndParams(filter, params, hql);
		return this.count(hql.replaceAll(RIGHTJOINFETCH, RIGHTJOIN).replaceAll(JOINFETCH, JOIN), params);// 统计是不可以预先抓取的，所以要更换hql
	}

	@Override
	public int executeHql(String hql) {
		return this.executeHql(hql, null);
	}

	@Override
	public int executeHql(String hql, Map<String, Object> params) {
		Query q = getCurrentSession().createQuery(hql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				q.setParameter(key, params.get(key));
			}
		}
		return q.executeUpdate();
	}

	@Override
	public Map<String, Object> getBySql(String sql) {
		return this.getBySql(sql, null);
	}

	@Override
	public Map<String, Object> getBySql(String sql, Map<String, Object> params) {
		List<Map<String, Object>> l = this.findBySql(sql, params, 1, 1);
		if (l != null && l.size() > 0) {
			return l.get(0);
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> findBySql(String sql) {
		return this.findBySql(sql, null, null, null);
	}

	@Override
	public List<Map<String, Object>> findBySql(String sql, Map<String, Object> params) {
		return this.findBySql(sql, params, null, null);
	}

	@Override
	public List<Map<String, Object>> findBySql(String sql, Integer page, Integer pageSize) {
		return this.findBySql(sql, null, page, pageSize);
	}

	/**
	 * 如果sql语句中，列有别名，那么返回的map的key与别名相同，否则返回Map的Key都是大写的
	 */
	@SuppressWarnings("deprecation")
	@Override
	public List<Map<String, Object>> findBySql(String sql, Map<String, Object> params, Integer page, Integer pageSize) {
		@SuppressWarnings("unchecked")
		SQLQuery q = getCurrentSession().createSQLQuery(sql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				q.setParameter(key, params.get(key));
			}
		}
		List<String> aliasList = SqlUtil.getAliasList(sql);
		if (aliasList != null && aliasList.size() > 0) {
			for (String alias : aliasList) {
				q.addScalar(alias);
			}
		}
		if (page == null || pageSize == null) {
			return q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		}
		return q.setFirstResult((page - 1) * pageSize).setMaxResults(pageSize)
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}

	@Override
	public int executeSql(String sql) {
		return this.executeSql(sql, null);
	}

	@Override
	public int executeSql(String sql, Map<String, Object> params) {
		SQLQuery q = getCurrentSession().createSQLQuery(sql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				q.setParameter(key, params.get(key));
			}
		}
		return q.executeUpdate();
	}

	@Override
	public Long countBySql(String sql) {
		return this.countBySql(sql, null);
	}

	@Override
	public Long countBySql(String sql, Map<String, Object> params) {
		SQLQuery q = getCurrentSession().createSQLQuery(sql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				q.setParameter(key, params.get(key));
			}
		}
		List list = q.list();
		return list.size() > 0 ? Long.valueOf(String.valueOf(list.get(0))) : 0L;
	}

	// =============== private method
	protected boolean isAuditable(T t){
		return t instanceof AuditableEntity;
	}

}
