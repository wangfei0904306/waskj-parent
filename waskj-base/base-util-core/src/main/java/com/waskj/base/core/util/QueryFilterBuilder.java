package com.waskj.base.core.util;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * QueryFilter Builder类，用于方便的使用代码构建QueryFilter
 * Created by 徐成龙 on 2016/10/11.
 */
public class QueryFilterBuilder {

    public static final int DEFAULT_PAGE_SIZE = 10;

    private final String Q_PREFIX = "Q_";
    private final String SPLIT_SYMBOL = "_";

//    private final String OPERATION_LIKE = "";

    // =============== fields
    private QueryFilter queryFilter;

    // =============== constructors
    private QueryFilterBuilder(){
        this.queryFilter = new QueryFilter();
    }

    private QueryFilterBuilder(HttpServletRequest request){
        this.queryFilter = new QueryFilter(request);
    }

    // =============== builder methods

    // --------------- sort
    /**
     * 设置排序<br/>
     * 举例：sendDate desc,orderNum desc,vin desc
     * @param sort
     * @return
     */
    public QueryFilterBuilder sort(String sort){
        this.queryFilter.setSort(sort);
        return this;
    }

    // --------------- page
    /**
     * 设置分页参数
     * @param page      第几页
     * @param pageSize  每页显示多少条
     * @return
     */
    public QueryFilterBuilder page(Integer page,Integer pageSize){
        return this.page(page).pageSize(pageSize);
    }

    /**
     * 设置分页参数
     * @param page  第几页
     * @return
     */
    public QueryFilterBuilder page(Integer page){
        this.queryFilter.setPage(page);
        return this;
    }

    /**
     * 设置分页参数
     * @param pageSize 每页显示条数，默认 10
     * @return
     */
    public QueryFilterBuilder pageSize(Integer pageSize){
        this.queryFilter.setPageSize(pageSize == null ? DEFAULT_PAGE_SIZE : pageSize);
        return this;
    }

    // ---------------  join
    /**
     * 设置关(内)联数据
     * @param join  内联语句，多个 <code>,</code> 隔开
     * @return
     */
    public QueryFilterBuilder join(String join){
        return this.join(join.replaceAll(" ", "").split(","));
    }

    /**
     * 设置关(内)联数据
     * @param join
     * @return
     */
    public QueryFilterBuilder join(String[] join){
        this.queryFilter.setJoin(join);
        return this;
    }

    /**
     * 设置关(内)联数据
     * @param join
     * @return
     */
    public QueryFilterBuilder join(Collection<String> join){
        String[] joinArr = new String[join.size()];
        return this.join(join.toArray(joinArr));
    }

    // ----------------- joinFetch
    /**
     * 设置预加载数据
     * @param joinFetch 预加载语句，多个 <code>,</code> 隔开
     * @return
     */
    public QueryFilterBuilder joinFetch(String... joinFetch){
        if( joinFetch != null && joinFetch.length != 0 )
            this.queryFilter.setJoinFetch(joinFetch);
        return this;
    }

    public QueryFilterBuilder joinFetch(Collection<String> joinFetch){
        String[] joinFetchArr = new String[joinFetch.size()];
        return this.joinFetch(joinFetch.toArray(joinFetchArr));
    }

    // -------------- rightJoin

    /**
     * 右联
     * @param rightJoin
     * @return
     */
    public QueryFilterBuilder rightJoin(String rightJoin){
        return this.rightJoin(rightJoin.replaceAll(" ", "").split(","));
    }

    public QueryFilterBuilder rightJoin(String[] rightJoin){
        this.queryFilter.setRightJoin(rightJoin);
        return this;
    }

    public QueryFilterBuilder rightJoin(Collection<String> rightJoin){
        String[] rightJoinArr = new String[rightJoin.size()];
        return this.rightJoin(rightJoin.toArray(rightJoinArr));
    }

    // --------- rightJoinFetch
    public QueryFilterBuilder rightJoinFetch(String rightJoinFetch){
        return this.rightJoinFetch(rightJoinFetch.replaceAll(" ", "").split(","));
    }

    public QueryFilterBuilder rightJoinFetch(String[] rightJoinFetch){
        this.queryFilter.setRightJoinFetch(rightJoinFetch);
        return this;
    }

    public QueryFilterBuilder rightJoinFetch(Collection<String> rightJoinFetch){
        String[] rightJoinFetchArr = new String[rightJoinFetch.size()];
        return this.rightJoinFetch(rightJoinFetch.toArray(rightJoinFetchArr));
    }

    // --------- query

    // ==================== like
    private final String LIKE = "LIKE";
    public static final String DOUBLE_QUOTE = "%%";
    private final String NOT_LIKE = "NOT LIKE";

    /**
     * like模糊匹配
     * @param name   属性名
     * @param value  值
     * @return
     */

    public QueryFilterBuilder like(String name,String value){
       return this.addFilter(name,LIKE,DOUBLE_QUOTE+value+DOUBLE_QUOTE);
    }

    /**
     * like模糊匹配，匹配开头
     * @param name   属性名
     * @param value  值
     * @return
     */
    public QueryFilterBuilder leftLike(String name,String value){
        return this.addFilter(name,LIKE,DOUBLE_QUOTE + value);
    }

    /**
     * like模糊匹配，匹配结尾
     * @param name   属性名
     * @param value  值
     * @return
     */
    public QueryFilterBuilder rightLike(String name,String value){
        return this.addFilter(name,LIKE,value+DOUBLE_QUOTE);
    }

    /**
     * like模糊匹配，匹配相反(not)
     * @param name   属性名
     * @param value  值
     * @return
     */
    public QueryFilterBuilder notLike(String name,String value){
        return this.addFilter(name,NOT_LIKE,DOUBLE_QUOTE+value+DOUBLE_QUOTE);
    }

    public QueryFilterBuilder notLeftLike(String name,String value){
        return this.addFilter(name,NOT_LIKE,DOUBLE_QUOTE+value);
    }

    public QueryFilterBuilder notRightLike(String name,String value){
        return this.addFilter(name,NOT_LIKE,value+DOUBLE_QUOTE);
    }

    // --------------  eq
    public static final String EQ = "=";
    public static final String NE = "!=";
    public static final String GT = ">";
    public static final String GE = ">=";
    public static final String LT = "<";
    public static final String LE = "<=";
    public static final String IN = "IN";
    public static final String NOT_IN = "NOT IN";
    public static final String IS_NULL = "IS NULL";
    public static final String IS_NOT_NULL = "IS NOT NULL";

    /**
     * 相等条件
     * @param name
     * @param value
     * @return
     */
    public QueryFilterBuilder eq(String name,Object value){
        return this.addFilter(name,EQ,value);
    }

    /**
     * 不等条件
     * @param name
     * @param value
     * @return
     */
    public QueryFilterBuilder ne(String name,Object value){
        return this.addFilter(name,NE,value);
    }

    /**
     * 大于
     * @param name
     * @param value
     * @return
     */
    public QueryFilterBuilder gt(String name,Object value){
        return this.addFilter(name,GT,value);
    }

    /**
     * 大于等于
     * @param name
     * @param value
     * @return
     */
    public QueryFilterBuilder ge(String name,Object value){
        return this.addFilter(name,GE,value);
    }

    /**
     * 小于
     * @param name
     * @param value
     * @return
     */
    public QueryFilterBuilder lt(String name,Object value){
        return this.addFilter(name,LT,value);
    }

    /**
     * 小于等于
     * @param name
     * @param value
     * @return
     */
    public QueryFilterBuilder le(String name,Object value){
        return this.addFilter(name,LE,value);
    }

    /**
     * 包含
     * @param name
     * @param collection
     * @return
     */
    public QueryFilterBuilder in(String name,Collection collection){
        return this.addFilter(name,IN,collection);
    }

    /**
     * 不包含
     * @param name
     * @param collection
     * @return
     */
    public QueryFilterBuilder notIn(String name,Collection collection){
        return this.addFilter(name,NOT_IN,collection);
    }

    /**
     * 为null
     * @param name
     * @return
     */
    public QueryFilterBuilder isNull(String name){
        return this.addFilter(name,IS_NULL,null);
    }

    /**
     * 不为null
     * @param name
     * @return
     */
    public QueryFilterBuilder isNotNull(String name){
        return this.addFilter(name,IS_NOT_NULL,null);
    }


    // ========== build method
    public QueryFilter build(){
        return this.queryFilter;
    }

    // *************** private methods
    private QueryFilterBuilder addFilter(String name,String operation,Object value){
        Object[] params = {name,operation,value};
        this.queryFilter.getParams().add(params);
        return this;
    }

//    private QueryFilterBuilder addFilter(String name,String operation,String value){
//        String qname = this.getQueryName(name,operation);
//        this.queryFilter.addFilter(qname,value);
//        return this;
//    }

    private QueryFilterBuilder addFilter(String name,String operation,String value,Class type){
        String qname = this.getQueryName(name,operation,type);
        this.queryFilter.addFilter(qname,value);
        return this;
    }

    private String getQueryName(String name,String operation){
        return Q_PREFIX.concat(name).concat(SPLIT_SYMBOL).concat(operation);
    }

    private String getQueryName(String name,String operation,Class type){
        return this.getQueryName(name,operation).concat(SPLIT_SYMBOL).concat(type.getSimpleName());
    }

    // =============== factory methods
    public static QueryFilterBuilder of(){
        return new QueryFilterBuilder();
    }

    public static  QueryFilterBuilder of(HttpServletRequest request){
        return new QueryFilterBuilder(request);
    }

}
