package jpabasic.inspacebe.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Info;

@Configuration
//spring security 사용하는 경우
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI(){
        String jwt="JWT";
        SecurityRequirement securityRequirement=new SecurityRequirement().addList(jwt);
        Components components=new Components().addSecuritySchemes(jwt,new SecurityScheme()
                .name(jwt)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
        );

        return new OpenAPI()
                .components(new Components())
                .info(apiInfo())
                .addSecurityItem(securityRequirement)
                .components(components);
    }

    private Info apiInfo(){
        return new Info()
                .title("API test")
                .description("practice Swagger UI")
                .version("1.0.0"); //API의 버전
    }
}