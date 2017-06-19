package com.waskj.test;

import com.waskj.codegen.CodeGenerator;
import org.junit.Test;

/**
 * Created by poet on 2016/10/27.
 */
public class GeneratorTest {

    //执行生成代码文件
    @Test
    public void doTest(){
        //配置文件
        TestGeneratorConfigure configure = new TestGeneratorConfigure();
        //生成
        new CodeGenerator().doGenerate(configure);
    }

}
