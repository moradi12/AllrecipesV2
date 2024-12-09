package Allrecipes.Recipesdemo.Security;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfiguration {

    /**
     * Define OpenAPI documentation bean
     *
     * @return OpenAPI object representing the API documentation
     */
    @Bean
    public OpenAPI defineOpenAPI() {
        Server server = new Server();
        server.setUrl("http://localhost:8080");
        server.setDescription("Local Server");

        Contact myContact = new Contact();
        myContact.setName("Tamir Moradi");
        myContact.setEmail("TamirMoradi@gmail.com");

        Info info = new Info()
                .title("Recipe API")
                .version("1.0")
                .description("This API exposes endpoints for the Recipe System")
                .contact(myContact);

        final String securitySchemeName = "bearerAuth";

        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        SecurityRequirement securityRequirement = new SecurityRequirement().addList(securitySchemeName);

        return new OpenAPI()
                .components(new Components().addSecuritySchemes(securitySchemeName, securityScheme))
                .info(info)
                .servers(List.of(server))
                .addSecurityItem(securityRequirement);
    }
}
