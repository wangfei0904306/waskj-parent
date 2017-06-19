package com.waskj.base.consumer.core;

import com.baomidou.framework.controller.SuperController;
import com.waskj.base.api.core.BaseJpaService;
import com.waskj.base.consumer.core.format.MultiDateFormat;
import com.waskj.base.consumer.core.result.JsonResult;
import com.waskj.base.consumer.core.result.PageResult;
import com.waskj.base.core.util.QueryFilter;
import com.waskj.base.domain.core.BaseEntity;
import com.waskj.base.provider.core.util.BeanCopyUtil;
import org.nutz.lang.Mirror;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.util.JavaScriptUtils;

import java.beans.PropertyEditorSupport;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 *
 */
public abstract class BaseJpaController<T extends BaseEntity, PK extends Serializable,S extends BaseJpaService<T,PK>> extends SuperController {

    private static final Logger log = LoggerFactory.getLogger(BaseController.class);

    private final Class<T> entityClass;

    @Autowired
    protected S service;

//    public abstract void setService(BaseJpaService<T, PK> service);

//    protected abstract BaseJpaService<T,PK> getService();

    @SuppressWarnings("unchecked")
    public BaseJpaController() {
        this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @InitBinder
    public void initBinder(ServletRequestDataBinder binder) {
        /**
         * 自动转换日期类型的字段格式
         */
//		binder.registerCustomEditor(Date.class,
//				new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));
        binder.registerCustomEditor(Date.class,
                new CustomDateEditor(new MultiDateFormat(), true));

        /**
         * 防止XSS攻击
         */
        binder.registerCustomEditor(String.class, new BaseJpaController.StringEscapeEditor(true, false));

    }

    @Override
    protected String getPageIndexParamName() {
        return "_index";
    }

    @Override
    protected String getPageSizeParamName() {
        return "_size";
    }

    public JsonResult save(T t) {
        JsonResult j = new JsonResult();
        try {
            Mirror<?> mirror = deleteTheEmptyObjectInTheCollection(t);// 如果不是级联添加，那么这句话不写也行，子方法重写save的时候，视情况添加这句话吧
            try {
                if (mirror.getField("created") != null) {
                    mirror.setValue(t, "created", new Date());
                }
            } catch (Exception e) {
            }
            try {
                if (mirror.getField("modified") != null) {
                    mirror.setValue(t, "modified", new Date());
                }
            } catch (Exception e) {
            }
            this.service.save(t);
            j.setMsg("保存成功");
            j.setSuccess(true);
        } catch (Exception e) {
            j.setMsg("保存失败！");
            e.printStackTrace();
        }
        return j;
    }

    public JsonResult delete(QueryFilter filter) {
        JsonResult j = new JsonResult();
        try {
            this.service.delete(this.service.get(filter));
            j.setMsg("删除成功");
            j.setSuccess(true);
        } catch (Exception e) {
            j.setMsg("删除失败");
            e.printStackTrace();
        }
        return j;
    }

    @SuppressWarnings("unchecked")
    public JsonResult update(T t) {
        Mirror<?> mirror = deleteTheEmptyObjectInTheCollection(t);// 如果不是级联更新，那么这句话不写也行，子方法重写update的时候，视情况添加这句话吧
        JsonResult j = new JsonResult();
        PK id = (PK) mirror.getValue(t, "id");// 如果你的主键不叫id，那么只能在之类里面重写update方法了
        T ot = this.service.get(id);
        if (ot != null) {
            try {
                BeanCopyUtil.copyPropertiesIgnoreNull(t,ot);
//                BeanUtils.copyProperties(t, ot, new String[] { "created" });// 一句话将新对象信息覆盖到原数据中
//                try {
//                    if (mirror.getField("modified") != null) {
//                        mirror.setValue(ot, "modified", new Date());
//                    }
//                } catch (NoSuchFieldException e) {
//                }
                this.service.update(ot);
                j.setMsg("更新成功");
                j.setSuccess(true);
            } catch (BeansException e) {
                j.setMsg("更新失败！");
                e.printStackTrace();
            }
        } else {
            j.setMsg("更新失败，找不到对象");
        }
        return j;
    }

    public JsonResult merge(T t) {
        Mirror<?> mirror = deleteTheEmptyObjectInTheCollection(t);// 如果不是级联更新，那么这句话不写也行，子方法重写update的时候，视情况添加这句话吧
        JsonResult j = new JsonResult();
        PK id = (PK) mirror.getValue(t, "id");// 如果你的主键不叫id，那么只能在之类里面重写update方法了
        T ot = this.service.get(id);
        if (ot != null) {
            try {
                BeanCopyUtil.copyPropertiesIgnoreNull(t,ot);
//                BeanUtils.copyProperties(t, ot, new String[] { "created" });// 一句话将新对象信息覆盖到原数据中
//                try {
//                    if (mirror.getField("modified") != null) {
//                        mirror.setValue(ot, "modified", new Date());
//                    }
//                } catch (NoSuchFieldException e) {
//                }
                this.service.merge(ot);
                j.setMsg("更新成功");
                j.setSuccess(true);
            } catch (BeansException e) {
                j.setMsg("更新失败！");
                e.printStackTrace();
            }
        } else {
            j.setMsg("更新失败，找不到对象");
        }
        return j;
    }

    public JsonResult get(QueryFilter filter) {
        JsonResult j = new JsonResult();
        j.setSuccess(true);
        j.setObj(this.service.get(filter));
        return j;
    }

    public PageResult<T> find(QueryFilter filter) {
        PageResult<T> p = new PageResult<T>();
        p.setRows(this.service.find(filter));
        p.setTotalRows(this.service.count(filter));
        p.setPage(filter.getPage());
        p.setPageSize(filter.getPageSize());
        return p;
    }

    /**
     * 在级联保存或者级联更新的时候，过滤id为空的关联对象
     *
     * 避免出现下面错误
     *
     * Caused by: org.hibernate.TransientObjectException: object references an unsaved transient instance - save the transient instance before flushing
     *
     * @param t
     * @return
     */
    @SuppressWarnings("rawtypes")
    public Mirror<?> deleteTheEmptyObjectInTheCollection(T t) {
        Mirror<?> mirror = Mirror.me(this.entityClass);
        Field[] fields = mirror.getFields();// 获得这个类的所有属性
        for (Field field : fields) {
            if (field.getGenericType().toString().indexOf("java.util.List") > -1) {
                List list = (List) mirror.getValue(t, field.getName());
                for (Iterator it = list.iterator(); it.hasNext();) {
                    Object o = it.next();
                    Mirror<?> oMirror = Mirror.me(o);
                    if(oMirror.isOf(BaseEntity.class)){
                        if (oMirror.getValue(o, "id") == null) {// 我默认你的主键叫ID，如果不叫这个，那你只能重写方法了
                            it.remove();
                        }
                    }
                }
            }
        }
        return mirror;
    }


    class StringEscapeEditor extends PropertyEditorSupport {

        private boolean escapeHTML;// 编码HTML
        private boolean escapeJavaScript;// 编码javascript

        public StringEscapeEditor() {
        }

        public StringEscapeEditor(boolean escapeHTML, boolean escapeJavaScript) {
            this.escapeHTML = escapeHTML;
            this.escapeJavaScript = escapeJavaScript;
        }

        @Override
        public String getAsText() {
            Object value = getValue();
            return value != null ? value.toString() : "";
        }

        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            if (text == null) {
                setValue(null);
            } else {
                String value = text;
                if (escapeHTML) {
                    value = HtmlUtils.htmlEscape(value);
                }
                if (escapeJavaScript) {
                    value = JavaScriptUtils.javaScriptEscape(value);
                }
                value = value.replaceAll("\t","    ");
                setValue(value);
            }
        }

    }

    private QueryFilter getQueryFilter(){
        return new QueryFilter(request);
    }

    // 获取page方法

}
