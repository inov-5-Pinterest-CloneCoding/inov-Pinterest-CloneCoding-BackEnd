package com.clonecoding.pinterest.global.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
         .info(new Info()
                .title("pinterest-clonecoding")
                .version("1.0")
                .description("API 문서"))
                .components(new Components()
                        .addSecuritySchemes("JWT",
                                new SecurityScheme()
                                        .name("JWT")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .in(SecurityScheme.In.HEADER)
                                        .description("Bearer와 공백(또는%20)을 제외한 JWT값을 넣어주세요")))
                .addSecurityItem(
                        new SecurityRequirement().addList("JWT", Arrays.asList("read", "write")));
    }
}
