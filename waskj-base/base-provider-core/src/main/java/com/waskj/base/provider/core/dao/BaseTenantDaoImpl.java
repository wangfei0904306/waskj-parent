package com.waskj.base.provider.core.dao;

import com.waskj.base.api.core.SecurityTenantUtil;
import com.waskj.base.core.util.QueryFilter;
import com.waskj.base.domain.core.BaseEntity;
import com.waskj.base.domain.core.ObTenantEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
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
public abstract class BaseTenantDaoImpl<T extends BaseEntity, PK extends Serializable> extends BaseDaoImpl<T,PK> {

	private static final Logger log = LoggerFactory.getLogger(BaseTenantDaoImpl.class);

	private static final String idType = "AUTO";

	private static final String batchSize = "5000";

	/**
	 * 构造时，告诉baseDao要查询表的类型，并且初始化查询和统计语句
	 */
	@SuppressWarnings("unchecked")
	public BaseTenantDaoImpl() {
		super();
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
		hql += " WHERE 1=1 and t.tenantEntity = " + SecurityTenantUtil.getTenantId() +" ";
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
		if(t instanceof ObTenantEntity){
			if(((ObTenantEntity)t).getTenantEntity()==null){
				((ObTenantEntity)t).setTenantEntity(SecurityTenantUtil.getTenantInfo());
			}
		}
		return super.save(t);
	}

}
