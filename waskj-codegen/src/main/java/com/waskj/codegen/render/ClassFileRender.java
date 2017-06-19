package com.waskj.codegen.render;

import com.waskj.codegen.model.RenderModel;
import com.waskj.codegen.util.ExceptionUtil;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;

/**
 * Created by poet on 2016/10/27.
 */
public class ClassFileRender {

    private String outputRootDir;

    private File outRoot = null;

    private Configuration configuration;

    private Template daoTemplate;
    private Template serviceTemplate;
    private Template serviceImplTemplate;
    private Template controllerTemplate;

    public ClassFileRender(String outputRootDir) {
        // 默认拼接 src/main/java
        this.outputRootDir = outputRootDir.concat("/src/main/java");
        this.outRoot = new File(this.outputRootDir);

        this.initFreemarker();
    }

    /**
     * 执行模板渲染
     * @param renderModel
     */
    public void doRender(RenderModel renderModel){
        this.renderDao(renderModel);
        this.renderService(renderModel);
        this.renderServiceImpl(renderModel);
        this.renderController(renderModel);
    }


    private void renderDao(RenderModel model){
        ensureDirExist(model.getDaoPackage());
        File daoFile = new File(outRoot,getJavaFile(model.getDaoClassFullName()));
        renderFile(daoFile,model,this.daoTemplate);
    }

    private void renderService(RenderModel model){
        ensureDirExist(model.getServicePackage());
        File serviceFile = new File(outRoot,getJavaFile(model.getServiceClassFullName()));
        renderFile(serviceFile,model,this.serviceTemplate);
    }

    private void renderServiceImpl(RenderModel model){
        ensureDirExist(model.getServiceImplPackage());
        File serviceImplFile = new File(outRoot,getJavaFile(model.getServiceImplClassFullName()));
        renderFile(serviceImplFile,model,this.serviceImplTemplate);
    }

    private void renderController(RenderModel model){
        ensureDirExist(model.getControllerPackage());
        File controllerFile = new File(outRoot,getJavaFile(model.getControllerClassFullName()));
        renderFile(controllerFile,model,this.controllerTemplate);
    }

    private void renderFile(File file,RenderModel model,Template template){
        try {
            Writer writer = new FileWriter(file);
            template.process(model,writer);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            ExceptionUtil.wrapException(e);
        }
    }

    private void ensureDirExist(String packagePath){
        String path = packagePath.replaceAll("\\.","/");
        File outputDir = new File(outRoot,path);
        if(!outputDir.exists())
            outputDir.mkdirs();
    }

    private String getJavaFile(String fullClassName){
        return fullClassName.replaceAll("\\.","/").concat(".java");
    }


    private void initFreemarker() {
        configuration = new Configuration(Configuration.VERSION_2_3_23);
        configuration.setTemplateLoader(new ClassTemplateLoader());
        try {
            daoTemplate = configuration.getTemplate("generator/dao.ftl");
            serviceTemplate = configuration.getTemplate("generator/service.ftl");
            serviceImplTemplate = configuration.getTemplate("generator/serviceimpl.ftl");
            controllerTemplate = configuration.getTemplate("generator/controller.ftl");
        } catch (IOException e) {
            ExceptionUtil.wrapException(e);
        }
    }

}
