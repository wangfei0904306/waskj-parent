package com.waskj.test;

import com.waskj.codegen.CodeGeneratorConfigure;

/**
 * Created by poet on 2016/10/27.
 */
public class TestGeneratorConfigure extends CodeGeneratorConfigure {
    @Override
    public void configJdbc(JdbcConfig jdbcConfig) {
        jdbcConfig.setDriverClassName("com.mysql.jdbc.Driver");
        jdbcConfig.setJdbcUrl("jdbc:mysql://192.168.1.70:3333/ams-v1?useUnicode=true&characterEncoding=utf8");
        jdbcConfig.setPassword("root");
        jdbcConfig.setUsername("root");
    }

    @Override
    public void configExclude(TableExcludeConfig excludeConfig) {
        excludeConfig.addExcludeTablePrefix("mag_");
        excludeConfig.addExcludeTablePrefix("com_");
        excludeConfig.addExcludeTablePrefix("sys_");
    }

    @Override
    public void configShortAlias(ShortAliasConfig aliasConfig) {
        aliasConfig.addShortAlias("ad","debtor",null);
        aliasConfig.addShortAlias("ap","asset",null);
        aliasConfig.addShortAlias("am","manage",null);
    }

    @Override
    public void configOutput(FileOutputConfig outputConfig) {
        outputConfig.setOutputDir("D://generate");
    }

    @Override
    public void configBasePackage(PackageConfig config) {
        config.setDaoBasePackage("com.waskj.ams.provider.${alias}.dao");
        config.setDomainBasePackage("com.waskj.ams.domain.${alias}");
        config.setServiceBasePackage("com.waskj.ams.api.${alias}");
        config.setServiceImplBasePackage("com.waskj.ams.provider.${alias}");
        config.setControllerBasePackage("com.waskj.ams.consumer.${alias}");
    }

    @Override
    public void configEntityName(BeanNameConfig config) {
        config.setDomainSuffix("JpaEntity");
        config.setDaoSuffix("Dao");
        config.setServiceSuffix("Service");
        config.setServiceImplSuffix("ServiceImpl");
        config.setControllerSuffix("JpaController");
    }
}
