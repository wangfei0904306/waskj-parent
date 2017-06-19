package com.waskj.codegen.demo;

import com.waskj.codegen.CodeGeneratorConfigure;
import com.waskj.codegen.config.*;

/**
 * Created by poet on 2016/10/27.
 */
public class DemoGeneratorConfigure extends CodeGeneratorConfigure {
    @Override
    public void configJdbc(JdbcConfig jdbcConfig) {
        jdbcConfig.setDriverClassName("com.mysql.jdbc.Driver");
        jdbcConfig.setJdbcUrl("jdbc:mysql://192.168.1.70:3333/ams-v1?useUnicode=true&characterEncoding=utf8");
        jdbcConfig.setPassword("root");
        jdbcConfig.setUsername("root");
    }

    //排除的表前缀
    @Override
    public void configExclude(TableExcludeConfig excludeConfig) {
        excludeConfig.addExcludeTablePrefix("mag_");
        excludeConfig.addExcludeTablePrefix("com_");
        excludeConfig.addExcludeTablePrefix("sys_");
    }

    //表前缀跟包名的关系
    @Override
    public void configShortAlias(ShortAliasConfig aliasConfig) {
        aliasConfig.addShortAlias("ad","debtor",null);
        aliasConfig.addShortAlias("ap","asset",null);
        aliasConfig.addShortAlias("am","manage",null);
    }

    //代码输出路径
    @Override
    public void configOutput(FileOutputConfig outputConfig) {
        outputConfig.setOutputDir("D://generate");
    }

    //配置基础包路径
    @Override
    public void configBasePackage(PackageConfig config) {
        config.setDaoBasePackage("com.waskj.ams.provider.${alias}.dao");
        config.setDomainBasePackage("com.waskj.ams.domain.${alias}");
        config.setServiceBasePackage("com.waskj.ams.api.${alias}");
        config.setServiceImplBasePackage("com.waskj.ams.provider.${alias}");
        config.setControllerBasePackage("com.waskj.ams.consumer.${alias}");
    }

    //配置实体名字
    @Override
    public void configEntityName(BeanNameConfig config) {
        config.setDomainSuffix("JpaEntity");
        config.setDaoSuffix("Dao");
        config.setServiceSuffix("Service");
        config.setServiceImplSuffix("ServiceImpl");
        config.setControllerSuffix("JpaController");
    }
}
