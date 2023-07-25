package com.bilgeadam.config.security;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customize(){
        String securitySchemeNAme="bearerAuth";
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeNAme))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeNAme,new SecurityScheme()
                                .name(securitySchemeNAme)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("Bearer").bearerFormat("JWT")
                        ));

    }

}
