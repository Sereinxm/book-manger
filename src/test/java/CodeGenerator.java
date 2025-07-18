import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

public class CodeGenerator {

    public static void main(String[] args) {
        String[] tableNames = {"book"};
        String packageName = "com.caoximu.bookmanger";
        String projectPath = System.getProperty("user.dir");
        String outputDir = projectPath + "/src/test/java";
        FastAutoGenerator.create("jdbc:mysql://192.168.1.177:7567/book_manger", "root", "mysql1.177")
                .globalConfig(builder -> {
                    builder.author("caoximu")// 作者
                            .outputDir(outputDir);
                })
                .packageConfig(builder ->
                        builder.parent(packageName) // 设置父包名
//                                .moduleName(moduleName) // 设置父包模块名
                                .pathInfo(Collections.singletonMap(OutputFile.xml, outputDir)) // 设置mapperXml生成路径
                                .entity("entity")
                                .mapper("mapper")
                                .service("service")
                                .serviceImpl("service.impl")
                                .xml("mappers")
                )
                .strategyConfig(builder ->
                        builder.addInclude(tableNames) // 设置需要生成的表名
                                .addTablePrefix("t_", "c_") // 设置过滤表前缀
                                .addFieldPrefix("f_") // 设置过滤字段前缀
                                .entityBuilder()
                                .naming(NamingStrategy.underline_to_camel)
                                .idType(IdType.ASSIGN_ID)
                                .logicDeleteColumnName("F_DEL_FLAG")
                                .logicDeletePropertyName("delFlag")
                                .enableLombok()
                )
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
