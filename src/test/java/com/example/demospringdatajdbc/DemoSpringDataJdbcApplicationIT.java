package com.example.demospringdatajdbc;

import com.example.demospringdatajdbc.country.Country;
import com.example.demospringdatajdbc.country.CountryDTO;
import com.example.demospringdatajdbc.country.CountryLightDTO;
import com.example.demospringdatajdbc.country.CountryRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test.
 * <p>
 * See : <a href="https://github.com/testcontainers/testcontainers-java-spring-boot-quickstart/blob/main/README.md">testcontainers-java-spring-boot-quickstart</a>
 */
@SpringBootTest(properties = {"spring.jpa.show-sql=true"})
@Testcontainers
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class DemoSpringDataJdbcApplicationIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("ghusta/postgres-world-db:2.11")
            .asCompatibleSubstituteFor("postgres"));
    @Autowired
    private DataSource dataSource;
    @Autowired
    private CountryRepository countryRepository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void testQuery_countryByCode() {
        Optional<Country> swe = countryRepository.findByCode("SWE");
        assertThat(swe).isNotEmpty();
        assertThat(swe.get().getName()).isEqualTo("Sweden");
    }

    @Test
    void testQuery_countryLightDTOByNameLike() {
        List<CountryLightDTO> fra = countryRepository.findByNameLikeDTO("france");
        assertThat(fra).isNotEmpty();
        assertThat(fra.get(0).name()).isEqualTo("France");
    }

    @Test
    void testQuery_findByContinent() {
        List<CountryDTO> byContinent = countryRepository.findByContinent("Asia");
        assertThat(byContinent).hasSizeGreaterThan(10);

        CountryDTO first = byContinent.getFirst();
        System.out.println("First = " + first);
        CountryDTO last = byContinent.getLast();
        System.out.println("Last = " + last);
    }

    @Test
    void testQuery_findMaxPopulationInAsia() {
        Optional<CountryDTO> countryDTO = countryRepository.findFirstByContinentOrderByPopulationDesc("Asia");
        assertThat(countryDTO).isPresent();
        System.out.println(countryDTO.get());
    }
}
