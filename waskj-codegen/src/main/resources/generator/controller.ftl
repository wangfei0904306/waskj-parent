package ${controllerPackage};

import ${entityClassFullName};
import ${serviceClassFullName};
import BaseJpaController;
import com.baomidou.mybatisplus.plugins.Page;
import JsonResult;
import PageResult;
import com.waskj.base.core.util.QueryFilter;
import com.waskj.base.core.util.QueryFilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

public class ${controllerClassName}  extends BaseJpaController<${entityClassName}, Long, ${serviceClassName}> {

    private Logger log = LoggerFactory.getLogger(${controllerClassName}.class);

    @RequestMapping("list")
    public PageResult list(HttpServletRequest request){
    Page<${entityClassName}> page = super.getPage();
        QueryFilter filter = QueryFilterBuilder.of(request).build();
        service.findPage(filter,page);
        return new PageResult(page);
    }

    @RequestMapping("del")
    public JsonResult del(Long id){
        return super.delete(QueryFilterBuilder.of().eq("t.id",id).build());
    }

    @RequestMapping("show")
    public JsonResult show(Long id){
        ${entityClassName} entity = service.getLazy(id);
        if( entity == null )
            return JsonResult.resultError("没有找到数据!");
        return JsonResult.resultSuccess("",entity);
    }

    @RequestMapping("add")
    public JsonResult add(${entityClassName} entity){
        return super.save(entity);
    }

    @RequestMapping("update")
    public JsonResult doUpdate(${entityClassName} entity){
        return super.update(entity);
    }

}