package com.waskj.codegen;

import com.waskj.codegen.config.*;

/**
 * Created by poet on 2016/10/27.
 */
public abstract class CodeGeneratorConfigure {

    public abstract void configJdbc(JdbcConfig config);

    public abstract void configExclude(TableExcludeConfig config);

    public abstract void configShortAlias(ShortAliasConfig config);

    public abstract void configOutput(FileOutputConfig config);

    public abstract void configBasePackage(PackageConfig config);

    public abstract void configEntityName(BeanNameConfig config);

}
