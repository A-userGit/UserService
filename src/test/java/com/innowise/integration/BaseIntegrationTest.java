package com.innowise.integration;

import com.innowise.userservice.UserServiceApplication;
import com.redis.testcontainers.RedisContainer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

@Sql(
    scripts = {"classpath:sql/data.sql"},
    executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
@SpringBootTest(classes = UserServiceApplication.class)
public abstract class BaseIntegrationTest {

  @Container
  private static final RedisContainer redis = new RedisContainer(
      DockerImageName.parse("redis:latest")).withReuse(true);
  @Container
  private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
      DockerImageName.parse("postgres:latest")).withReuse(true);

  static {
    redis.start();
    postgres.start();
  }

  @DynamicPropertySource
  static void registerDBProperties(DynamicPropertyRegistry propertyRegistry) {
    propertyRegistry.add("integration-tests-db", postgres::getDatabaseName);
    propertyRegistry.add("spring.datasource.username", postgres::getUsername);
    propertyRegistry.add("spring.datasource.password", postgres::getPassword);
    propertyRegistry.add("spring.datasource.url", postgres::getJdbcUrl);
    propertyRegistry.add("spring.data.redis.host", redis::getHost);
    propertyRegistry.add("spring.data.redis.port", () -> redis.getMappedPort(6379).toString());
  }
}
