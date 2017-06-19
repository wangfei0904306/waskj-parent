package com.waskj.base.provider.core;

import com.baomidou.mybatisplus.plugins.Page;
import com.waskj.base.api.core.BaseJpaService;
import com.waskj.base.core.util.QueryFilter;
import com.waskj.base.core.util.QueryFilterBuilder;
import com.waskj.base.domain.core.BaseEntity;
import com.waskj.base.provider.core.dao.BaseDao;
import com.waskj.base.provider.core.util.BeanCopyUtil;
import org.nutz.lang.Mirror;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Iterator;
import java.util.List;


/**
 * 基础service实现类
 *
 * 实现此service的类必须提供泛型dao
 *
 * http://git.oschina.net/sphsyv/sypro
 *
 * @author 孙宇
 *
 * @param <T>
 * @param <PK>
 */
@Service("baseService")
public abstract class BaseJpaServiceImpl<T extends BaseEntity, PK extends Serializable,DAO extends BaseDao<T,PK>> implements BaseJpaService<T, PK> {

    private Logger log = LoggerFactory.getLogger(BaseJpaServiceImpl.class);
    private final Class<T> entityClass = (Class)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    @Autowired
    protected DAO dao;

    /**
     * 抽象方法，需要子类提供dao
     *
     */
//    public abstract void setDao(BaseDao<T, PK> dao);

    @Override
    public Serializable save(T t) {
        return this.dao.save(t);
    }

    @Override
    public void saveOrUpdate(T t) {
        this.dao.saveOrUpdate(t);
    }

    @Override
    public void delete(T t) {
        this.dao.delete(t);
    }

    @Override
    public void update(T t) {
        this.dao.update(t);
    }

    @Override
    public void updateSelect(T t) {
        Mirror mirror = this.deleteTheEmptyObjectInTheCollection(t);
        Serializable id = (Serializable)mirror.getValue(t, "id");
        T ot = this.dao.get((PK) id);
        if(ot != null) {
            try {
                BeanCopyUtil.copyPropertiesIgnoreNull(t, ot);
                this.dao.update(ot);
            } catch (BeansException var7) {
                log.error("更新失败！");
                var7.printStackTrace();
            }
        } else {
            log.error("更新失败，找不到对象");
        }
    }

    @Override
    public void merge(T t) {
        this.dao.merge(t);
    }

    @Override
    public T get(PK pk) {
        return this.dao.get(pk);
    }

    @Override
    public T getLazy(PK pk) {
        QueryFilter filter = QueryFilterBuilder.of().eq("t.id",pk).build();
        return get(filter);
    }

    @Override
    public T load(PK pk) {
        T result = null;
        try {
            result = dao.load(pk);
        } catch (Exception e) {
           log.error("load entity error!",e);
        }
        return result;
    }

    @Override
    public T get(QueryFilter filter) {
        return this.dao.get(filter);
    }

    @Override
    public List<T> find() {
        return this.dao.find();
    }

    @Override
    public List<T> find(QueryFilter filter) {
        return this.dao.find(filter);
    }

    @Override
    public Page<T> findPage(QueryFilter filter, Page<T> page) {

        filter.setPage(page.getCurrent());
        filter.setPageSize(page.getSize());

        List<T> records = this.dao.find(filter);
        Long count = this.count(filter);

        page.setRecords(records);
        page.setTotal(count.intValue());

        return page;
    }

    @Override
    public Long count() {
        return this.dao.count();
    }

    @Override
    public Long count(QueryFilter filter) {
        return this.dao.count(filter);
    }


    public Mirror<?> deleteTheEmptyObjectInTheCollection(T t) {
        Mirror mirror = Mirror.me(this.entityClass);
        Field[] fields = mirror.getFields();
        Field[] var4 = fields;
        int var5 = fields.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            Field field = var4[var6];
            if(field.getGenericType().toString().indexOf("java.util.List") > -1) {
                List list = (List)mirror.getValue(t, field.getName());
                Iterator it = list.iterator();

                while(it.hasNext()) {
                    Object o = it.next();
                    Mirror oMirror = Mirror.me(o);
                    if(oMirror.getValue(o, "id") == null) {
                        it.remove();
                    }
                }
            }
        }

        return mirror;
    }

}
