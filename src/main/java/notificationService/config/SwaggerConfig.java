package notificationService.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(
                        new Info()
                                .description("Эндпоинты для notification-service")
                                .title("SocialWebService - API")
                                .version("1.0.0")
                                .contact(new Contact().name("@kudamoi")
                                        .email("lihoy.sasha@gmail.com"))
                )
                .servers(List.of(
                        new Server().url("http://localhost:80")
                                .description("Notification service")
                        )
                );
    }
}
