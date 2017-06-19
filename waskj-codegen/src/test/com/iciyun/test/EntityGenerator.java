package com.waskj.test;

import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.JDBCMetaDataConfiguration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2x.POJOExporter;
import org.junit.Test;

import java.io.File;
import java.util.Properties;

/**
 * Created by poet on 2016/10/27.
 */
public class EntityGenerator {

    @Test
    public void exportPojo(){
        Configuration cfg = new Configuration()
                .setProperty("hibernate.connection.username","root")
                .setProperty("hibernate.connection.password","root")
                .setProperty("hibernate.connection.url","jdbc:mysql://192.168.1.70:3333/ams-v1?useSSL=false&useUnicode=true&characterEncoding=utf8")
                .setProperty("hibernate.connection.driver_class","com.mysql.jdbc.Driver")
                .setProperty("hibernate.dialect","org.hibernate.dialect.MySQLDialect");

//        Properties properties = new Properties();
//        properties.put("connection.username","root");
//        properties.put("connection.password","root");
//        properties.put("connection.url","jdbc:mysql://192.168.1.70:3333/ams-v1?useUnicode=true&characterEncoding=utf8");
//        properties.put("connection.driver_class","com.mysql.jdbc.Driver");
//        properties.put("dialect","org.hibernate.dialect.MySQLDialect");


//        StandardServiceRegistryBuilder serviceRegistryBuilder = new StandardServiceRegistryBuilder();
//        serviceRegistryBuilder.applySettings(properties);
//        ServiceRegistry serviceRegistry = serviceRegistryBuilder.build();

        POJOExporter exporter = new POJOExporter(cfg,new File("D://generate"));
        exporter.getProperties().setProperty("ejb3", ""+false);
        exporter.getProperties().setProperty("jdk5", ""+false);
        exporter.start();
    }

}
