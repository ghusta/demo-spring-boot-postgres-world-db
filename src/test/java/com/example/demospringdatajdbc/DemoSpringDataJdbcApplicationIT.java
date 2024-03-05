package com.example.demospringdatajdbc;

import com.example.demospringdatajdbc.dao.CountryRepository;
import com.example.demospringdatajdbc.domain.CountryDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test.
 * <p>
 * See : <a href="https://github.com/testcontainers/testcontainers-java-spring-boot-quickstart/blob/main/README.md">testcontainers-java-spring-boot-quickstart</a>
 */
@SpringBootTest
@Testcontainers
class DemoSpringDataJdbcApplicationIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("ghusta/postgres-world-db:2.11")
            .asCompatibleSubstituteFor("postgres"));

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private DataSource dataSource;

    @Autowired
    private CountryRepository countryRepository;

    @Test
    void testQueryCountryByCode() {
        Optional<CountryDTO> swe = countryRepository.findByCode("SWE");
        assertThat(swe).isNotEmpty();
        assertThat(swe.get().getName()).isEqualTo("Sweden");
    }

}
