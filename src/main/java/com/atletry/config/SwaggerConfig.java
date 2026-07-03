package com.atletry.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    private static final String BEARER = "bearerAuth";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Atletry API")
                        .version("v1.0.0")
                        .description("""
                                ### Atletry — Sports Social Mobile App Backend

                                **Auth flow:**
                                1. `POST /auth/send-otp` — request OTP
                                2. `POST /auth/verify-otp` — verify OTP → receive `accessToken`
                                3. Paste token into the 🔒 Authorize button above
                                4. If `newUser=true` → call `PUT /users/me/profile`
                                5. If `profileComplete=false` → call `POST /users/me/sports` (min 3)

                                **Skill levels:** `JUST_STARTING` · `INTERMEDIATE` · `COMPETITIVE`
                                """)
                        .contact(new Contact().name("Atletry Team").email("dev@atletry.com")))
                .servers(List.of(
                        new Server().url("http://localhost:8080/api/v1").description("Local"),
                        new Server().url("https://api.atletry.com/api/v1").description("Production")))
                .addSecurityItem(new SecurityRequirement().addList(BEARER))
                .components(new Components()
                        .addSecuritySchemes(BEARER, new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT from /auth/verify-otp")));
    }
}
