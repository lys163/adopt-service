package com.adopt.adopt_service.config;

import java.util.List;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.parameters.CookieParameter;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {
    @Bean 
    OpenAPI openAPI(){
        Info info = new Info()
            .version("v1.0")
            .title("Adopt Service API")
            .description("Adopt Service API 문서입니다.");
        return new OpenAPI()
            .info(info)
            .servers(List.of(
                new Server()
                    .url("http://localhost:8080")
                    .description("개발 서버")
            ));
    }
    @Bean
    public OpenApiCustomizer addJsessionIdCookie(){
        return openApi -> openApi.getPaths().values().forEach(pathItem -> {
            pathItem.readOperations().forEach(operation -> {
                Parameter jsessionParam = new CookieParameter()
                    .name("JSESSIONID")
                    .description("세션 쿠키")
                    .required(false);
                operation.addParametersItem(jsessionParam);
            });
        });
    }
    
}
