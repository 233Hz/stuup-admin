package com.poho.common.generator;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Mybatis Plus 代码生成
 */
public class MySqlGenerator {

    public static void main(String[] args) {
        // 项目路径
        String PROJECT_DIR = System.getProperty("user.dir") + "\\stuup_common";
        String CONTROLLER_DIR = System.getProperty("user.dir") + "\\stuup_api\\src\\main\\java\\com\\poho\\stuup\\api\\controller";
        // 数据库设置
        String URL = "jdbc:mysql://192.168.0.100:3306/stuup_db?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai&autoReconnect=true&failOverReadOnly=false";
        String USERNAME = "root";
        String PASSWORD = "admin";
        // 路径配置
        Map<OutputFile, String> pathInfoMap = new HashMap<>();
        pathInfoMap.put(OutputFile.xml, PROJECT_DIR + "\\src\\main\\resources\\mapper");
        pathInfoMap.put(OutputFile.controller, CONTROLLER_DIR);
        FastAutoGenerator.create(URL, USERNAME, PASSWORD)
                // 全局配置
                .globalConfig(builder -> {
                    builder.author("BUNGA") // 设置作者
//                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .disableOpenDir()   // 不打开目录
                            .dateType(DateType.ONLY_DATE)
                            .commentDate("yyyy-MM-dd")
                            .outputDir(PROJECT_DIR + "\\src\\main\\java"); // 指定输出目录
                })
                // 包配置
                .packageConfig(builder -> {
                    builder.parent("com.poho") // 设置父包名
                            .moduleName("stuup") // 设置父包模块名
                            .entity("model")
                            .service("service")
                            .serviceImpl("service.impl")
                            .controller("controller")
                            .mapper("dao")
                            .xml("mapper")
                            .pathInfo(pathInfoMap); // 设置mapperXml生成路径
                })
                // 策略配置
                .strategyConfig(builder -> {
                    builder.addInclude(scanner("表名").split(",")) // 设置需要生成的表名
                            .addTablePrefix("t_") // 设置过滤表前缀
                            .controllerBuilder()
                            .enableRestStyle()
                            .entityBuilder()
                            .idType(IdType.AUTO)
                            .enableLombok()
                            .serviceBuilder()
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImpl")
                            .mapperBuilder()
                            .enableMapperAnnotation()
                            .enableBaseResultMap();
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }

    /**
     * 读取控制台内容
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入" + tip + "（多个表用 , 分割）：");
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotBlank(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }
}
