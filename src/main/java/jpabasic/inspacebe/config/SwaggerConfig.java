package jpabasic.inspacebe.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        String jwt = "JWT";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
        Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
                .name(jwt)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT"));

        return new OpenAPI()
                .info(apiInfo())
                .addSecurityItem(securityRequirement)
                .components(components)
                .addServersItem(new Server().url("http://inspace-proxy:8000").description("Proxy Server"))  //프록시 추가
                .addServersItem(new Server().url("http://3.35.10.158:8080").description("Default Backend"));  // 기존 서버 유지
    }

    private Info apiInfo() {
        return new Info()
                .title("API Documentation")
                .description("Swagger Integration with JWT")
                .version("1.0.0");
    }
}