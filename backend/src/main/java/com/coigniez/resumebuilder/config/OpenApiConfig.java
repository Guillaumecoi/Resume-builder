package com.coigniez.resumebuilder.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
    info = @Info(
        title = "Resume Builder API specification",
        version = "1.0",
        description = "API for Resume Builder",
        license = @License(name = "MIT License", url = "https://opensource.org/licenses/MIT")
    ),
    servers = {
        @Server(
            url = "http://localhost:8080/api/v1",
            description = "Local server"
        )
    },
    security = {
        @SecurityRequirement(
            name = "bearerAuth"
        )
    }
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    description = "JWT token",
    bearerFormat = "JWT",
    in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
    
}
