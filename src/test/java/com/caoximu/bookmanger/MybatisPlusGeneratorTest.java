package com.caoximu.bookmanger;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.sql.Types;
import java.util.Collections;

public class MybatisPlusGeneratorTest {

    public static void main(String[] args) {
        String[] tableNames = {"users"};
        String url = "jdbc:mysql://192.168.1.177:7567/book_manger?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=Asia/Shanghai&rewriteBatchedStatements=true";
        FastAutoGenerator.create(url , "root", "mysql1.177")
                .globalConfig(builder -> {
                    builder.author("caoximu") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .outputDir("D:\\code\\java\\book-manger\\src\\test\\java\\generatedCode"); // 指定输出目录
                })
                .dataSourceConfig(builder -> builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                    int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                    if (typeCode == Types.SMALLINT) {
                        // 自定义类型转换
                        return DbColumnType.INTEGER;
                    }
                    return typeRegistry.getColumnType(metaInfo);

                }))
                .packageConfig(builder -> {
                    builder.parent("com.caoximu.bookmanger") // 设置父包名
                            .controller("controller")
                            .entity("entity") // 设置实体类包名
                           .service("service") // 设置service包名
                           .serviceImpl("service.impl") // 设置service实现类包名
                           .mapper("mapper") // 设置mapper包名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "D:\\code\\java\\book-manger\\src\\test\\java\\generatedCode\\mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude(tableNames) // 设置需要生成的表名
                            .addTablePrefix("t_", "c_") // 设置过滤表前缀
                            .controllerBuilder().enableRestStyle() // 开启restful风格控制器
                            .enableFileOverride() // 覆盖已生成文件
                            .entityBuilder().enableLombok(); // 开启lombok模型，默认是false
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}