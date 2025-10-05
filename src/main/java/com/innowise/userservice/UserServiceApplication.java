package com.innowise.userservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableCaching
@EnableAspectJAutoProxy
@EnableConfigurationProperties
@OpenAPIDefinition(servers = {@Server(url = "${swagger-server}")},
    info = @Info(title = "User service API",
        version = "1.0",
        description = "User service")
)@SecurityScheme(name = "user_security",
    type = SecuritySchemeType.OAUTH2,
    flows = @OAuthFlows(authorizationCode =
    @OAuthFlow(authorizationUrl = "${springdoc.oAuthFlow.authorizationUrl}",
        tokenUrl = "${springdoc.oAuthFlow.tokenUrl}"

    ))
)
public class UserServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(UserServiceApplication.class, args);
  }
}
