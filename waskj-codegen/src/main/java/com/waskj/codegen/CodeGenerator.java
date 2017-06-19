package com.waskj.codegen;

import com.waskj.codegen.model.RenderModel;
import com.waskj.codegen.render.ClassFileRender;
import com.waskj.codegen.util.JdbcUtil;
import com.waskj.codegen.config.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * main method to start generate code
 * Created by poet on 2016/10/27.
 */
public class CodeGenerator {

    private JdbcConfig jdbcConfig = new JdbcConfig();
    private ShortAliasConfig shortAliasConfig = new ShortAliasConfig();
    private FileOutputConfig fileOutputConfig = new FileOutputConfig();
    private TableExcludeConfig tableExcludeConfig = new TableExcludeConfig();
    private PackageConfig packageConfig = new PackageConfig();
    private BeanNameConfig beanNameConfig = new BeanNameConfig();

    public void doGenerate(CodeGeneratorConfigure configure){

        // 配置
        doConfig(configure);

        // 获取需要的table
        Collection<String> tableNames = getTablesToGenerate();

        // freemarker模型
        Collection<RenderModel> models = tablesToRenderModels(tableNames);

        // 生成文件
        doRender(models);

    }

    // do config
    private void doConfig(CodeGeneratorConfigure configure){
        configure.configExclude(tableExcludeConfig);
        configure.configJdbc(jdbcConfig);
        configure.configOutput(fileOutputConfig);
        configure.configShortAlias(shortAliasConfig);
        configure.configBasePackage(packageConfig);
        configure.configEntityName(beanNameConfig);
    }

    private Collection<RenderModel> tablesToRenderModels(Collection<String> tableNames){
        List<RenderModel> models = new ArrayList<>();
        // table name to render model
        for (String tableName : tableNames) {
            RenderModel renderModel = tableNameToRenderModel(tableName);
            models.add(renderModel);
        }
        return models;
    }

    private Collection<String> getTablesToGenerate(){
        // 所有数据表
        Collection<String> allTables = JdbcUtil.getTables(this.jdbcConfig);

        final Set<String> excludeTable = this.tableExcludeConfig.getExcludeTables();
        final Set<String> excludePrefixes = this.tableExcludeConfig.getExcludePrefixes();

        Set<String> ret = allTables.stream().filter(s -> !excludeTable.contains(s))
                .filter(s -> !excludePrefixes.stream().anyMatch(p -> s.startsWith(p)))
                .collect(Collectors.toSet());
        return ret;
    }

    //把表转成模型, 主要工作
    private RenderModel tableNameToRenderModel(String tableName){
        RenderModel ret = new RenderModel();

        String entityName = tableToEntityName(tableName);

        String packageAlias = getPackageByTableName(tableName);

        String entityClassName = entityName.concat(beanNameConfig.getDomainSuffix());
        String entityPackage = packageConfig.getDomainBasePackage().replace("${alias}",packageAlias);
        String entityClassFullName = entityPackage.concat(".").concat(entityClassName);

        String daoClassName = entityName.concat(beanNameConfig.getDaoSuffix());
        String daoPackage = packageConfig.getDaoBasePackage().replace("${alias}",packageAlias);
        String daoClassFullName = daoPackage.concat(".").concat(daoClassName);

        String serviceClassName = entityName.concat(beanNameConfig.getServiceSuffix());
        String servicePackage = packageConfig.getServiceBasePackage().replace("${alias}",packageAlias);
        String serviceClassFullName = servicePackage.concat(".").concat(serviceClassName);


        String serviceImplClassName = entityName.concat(beanNameConfig.getServiceImplSuffix());
        String serviceImplPackage = packageConfig.getServiceImplBasePackage().replace("${alias}",packageAlias);
        String serviceImplClassFullName = serviceImplPackage.concat(".").concat(serviceImplClassName);


        String controllerClassName = entityName.concat(beanNameConfig.getControllerSuffix());
        String controllerPackage = packageConfig.getControllerBasePackage().replace("${alias}",packageAlias);
        String controllerClassFullName = controllerPackage.concat(".").concat(controllerClassName);


        ret.setEntityClassFullName(entityClassFullName);
        ret.setEntityClassName(entityClassName);
        ret.setEntityPackage(entityPackage);

        ret.setDaoClassFullName(daoClassFullName);
        ret.setDaoClassName(daoClassName);
        ret.setDaoPackage(daoPackage);

        ret.setServiceClassFullName(serviceClassFullName);
        ret.setServiceClassName(serviceClassName);
        ret.setServicePackage(servicePackage);

        ret.setServiceImplClassFullName(serviceImplClassFullName);
        ret.setServiceImplClassName(serviceImplClassName);
        ret.setServiceImplPackage(serviceImplPackage);

        ret.setControllerClassFullName(controllerClassFullName);
        ret.setControllerClassName(controllerClassName);
        ret.setControllerPackage(controllerPackage);

        return ret;
    }

    private void doRender(Collection<RenderModel> models){
        ClassFileRender render = new ClassFileRender(fileOutputConfig.getOutputRootDir());
        for (RenderModel model : models) {
            render.doRender(model);
        }
    }

    // --------------------
    // 根据 表名 获取实体名称 不包含 后缀
    private String tableToEntityName(String tableName){
        String[] names = tableName.split("_");

//        String shortAlias = names[0]; // 别名位

        ShortAlias alias = getShortAliasByTableName(tableName);

        if( alias != null )
            names[0] = alias.getEntityAlias();

        return concatString(firstUpper(names));
    }

    // 获取包别名
    private String getPackageByTableName(String tableName){
        String pack = null;
        String[] names = tableName.split("_");
        ShortAlias alias = getShortAliasByTableName(tableName);

        return alias == null ? names[0] : alias.getPackageAlias();
    }

    private ShortAlias getShortAliasByTableName(String tableName){
        return this.shortAliasConfig.getShortAliases().get(tableName.split("_")[0]);
    }

    private String concatString(String... str){
        StringBuilder sb = new StringBuilder();
        for (String s : str) {
            sb.append(s);
        }
        return sb.toString();
    }

    private String[] firstUpper(String[] names){
        String[] ret = new String[names.length];
        int i = 0;
        for (String name : names) {
            ret[i] = firstUpper(name);
            i++;
        }
        return ret;
    }

    private String firstUpper(String s){
        StringBuilder sb = new StringBuilder(s);
        sb.setCharAt(0,Character.toUpperCase(s.charAt(0)));
        return sb.toString();
    }


}
