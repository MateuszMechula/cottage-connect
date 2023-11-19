package pl.cottageconnect.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import pl.cottageconnect.CottageConnectApplication;

@Configuration
public class SpringDocDocumentation {

    @Bean
    public GroupedOpenApi groupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("default")
                .pathsToMatch("/**")
                .packagesToScan(CottageConnectApplication.class.getPackageName())
                .addOperationCustomizer((operation, handlerMethod) -> {
                    if (handlerMethod.hasMethodAnnotation(PreAuthorize.class)) {
                        operation.addSecurityItem(new SecurityRequirement().addList("bearer-token"));
                    }
                    return operation;
                })
                .build();
    }

    @Bean
    public OpenAPI springDocOpenApi() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-token", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .info(new Info()
                        .title("CottageConnect")
                        .contact(contact())
                        .description("API cottageConnect")
                        .version("1.0"));
    }

    private Contact contact() {
        return new Contact()
                .name("CottageConnect")
                .url("https://github.com/MateuszMechula/cottageconnect")
                .email("mateuszmechua@gmail.com");
    }
}
