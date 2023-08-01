/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.io.File;
import java.io.IOException;

/**
 * <p>
 * 配置静态资源映射
 * </p>
 *
 * @author xiaolin
 * @since 2023-02-28
 */
@Configuration
public class MvcConfig extends WebMvcConfigurationSupport {

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/resources/");
        registry.addResourceHandler("/**").addResourceLocations("classpath:/webapp/public/");
        // 配置 knife4j 文档资源的访问路径
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");


        File directory = new File("");
        //    @Value("${files.upload.path}")
        String uploadPath;
        try {
            uploadPath = directory.getCanonicalPath() + File.separator + "upload" + File.separator;
        } catch (IOException e) {
            uploadPath = directory.getAbsolutePath() + File.separator + "upload" + File.separator;
        }
        registry.addResourceHandler("/upload/**").addResourceLocations("file:" + uploadPath);
    }
}
