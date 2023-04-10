/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ApiDocConfig {
    @Bean(value = "dockerBean")
    public Docket dockerBean() {
        //指定使用Swagger2规范
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("后台接口文档")
                        .description("融媒体平台管理系统后台接口文档")
                        .termsOfServiceUrl("https://www.lggxz.top")
                        .contact(new Contact("小林","https://www.lggxz.top","lmy1220@126.com"))
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("后台")
                .globalResponseMessage(RequestMethod.GET, getGlobalResponseMessageList())
                .globalResponseMessage(RequestMethod.POST, getGlobalResponseMessageList())
                .globalResponseMessage(RequestMethod.PUT, getGlobalResponseMessageList())
                .globalResponseMessage(RequestMethod.DELETE, getGlobalResponseMessageList())
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.xiaolin.mpms.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private List<ResponseMessage> getGlobalResponseMessageList(){
        List<ResponseMessage> list = new ArrayList<>();
        list.add(new ResponseMessageBuilder().code(200).message("成功").build());
        list.add(new ResponseMessageBuilder().code(500).message("服务器内部错误").build());
        list.add(new ResponseMessageBuilder().code(400).message("请求参数错误").build());
        list.add(new ResponseMessageBuilder().code(401).message("权限不足").build());
        list.add(new ResponseMessageBuilder().code(403).message("禁止访问").build());
        list.add(new ResponseMessageBuilder().code(404).message("请求路径不存在").build());
        return list;
    }
}
