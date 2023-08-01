package com.xiaolin.mpms.utils;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.xiaolin.mpms.entity.common.BaseEntry;

import java.util.Collections;
import java.util.List;

public class MPFastAutoGenerator {
    static void generator(String url, String username, String password, String outputDir, String parent, String moduleName, List<String> includes, List<String> prefix) {
        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    builder.author("xiaolin") // 设置作者
                            .enableSwagger()
                            .outputDir(outputDir); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent(parent) // 设置父包名
                            .moduleName(moduleName) // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, outputDir)); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> builder.addInclude(includes) // 设置需要生成的表名
                        .addTablePrefix(prefix) // 设置过滤表前缀
                        .entityBuilder()
                        .enableLombok()
                        .superClass(BaseEntry.class)
                        .logicDeleteColumnName("is_deleted")
                        .versionColumnName("version"))
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }


}
