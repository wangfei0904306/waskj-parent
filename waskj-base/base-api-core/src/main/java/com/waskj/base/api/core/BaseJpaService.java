package com.waskj.base.api.core;

import com.baomidou.mybatisplus.plugins.Page;
import com.waskj.base.core.util.QueryFilter;

import java.io.Serializable;
import java.util.List;



/**
 * 基础Service
 *
 * http://git.oschina.net/sphsyv/sypro
 *
 * @author 孙宇
 *
 * @param <T>
 * @param <PK>
 */
public interface BaseJpaService<T extends Serializable, PK extends Serializable> {
    /**
     * 保存一个对象
     *
     * @param t
     * @return
     */
    Serializable save(T t);

    /**
     * 保存或更新一个对象
     *
     * @param t
     */
    void saveOrUpdate(T t);

    /**
     * 删除一个对象
     *
     * @param t
     */
    void delete(T t);

    /**
     * 修改一个对象
     *
     * @param t
     */
    void update(T t);

    /**
     * 修改一个对象(只修改选择部分）
     *
     * @param t
     */
    void updateSelect(T t);

    void merge(T t);

    /**
     * 获得一个对象
     *
     * @param pk
     *            主键
     * @return
     */
    T get(PK pk);

    T getLazy(PK pk);

    /**
     * 获得一个对象
     * @param pk
     * @return
     */
    T load(PK pk);

    /**
     * 获得第一个对象
     *
     * @param filter
     * @return
     */
    T get(QueryFilter filter);

    /**
     * 查找所有
     *
     * @return
     */
    List<T> find();

    /**
     * 查找所有符合条件的
     *
     * @param filter
     * @return
     */
    List<T> find(QueryFilter filter);

    /**
     * 查询列表
     *
     * @param filter
     * @param page
     * @return
     */
    Page<T> findPage(QueryFilter filter,Page<T> page);

    /**
     * 统计所有
     *
     * @return
     */
    Long count();

    /**
     * 统计符合条件的
     *
     * @param filter
     * @return
     */
    Long count(QueryFilter filter);
}
