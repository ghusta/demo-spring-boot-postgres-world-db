package com.example.demospringdatajdbc;

import com.example.demospringdatajdbc.dao.CountryRepository;
import com.example.demospringdatajdbc.domain.CountryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.util.Assert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class DemoSpringDataJdbcApplication {

    private static final Logger log = LoggerFactory.getLogger(DemoSpringDataJdbcApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DemoSpringDataJdbcApplication.class, args);
    }

    @Bean
    ApplicationRunner demo(NamedParameterJdbcTemplate namedParameterJdbcTemplate, CountryRepository countryRepository) {
        return args -> {
            log.info("Go 🏁");

            String sql = "select * from country c where c.name ilike :name ";

//        String queryCountryName = "xxx";
            String queryCountryName = "france"; // en BDD : France
//        String queryCountryName = "FR%"; // commence par...
            SqlParameterSource sqlParameterSource = new MapSqlParameterSource(Map.of("name", queryCountryName));

            List<CountryDTO> list = namedParameterJdbcTemplate.query(sql, sqlParameterSource, beanRowMapper());

            // same with Spring Data JDBC
            // List<CountryDTO> list2 = countryRepository.findByNameLike(queryCountryName);
            // Optional<CountryDTO> brazil = countryRepository.findByCode("BRA");

            log.info("Total rows = {}", list.size());
            Assert.notNull(list, "query() never returns null");
            if (!list.isEmpty()) {
                log.info("First result : {} - {} - {}",
                        list.get(0).getName(),
                        list.get(0).getCode(),
                        list.get(0).getCode2());

                String emojiFlag = getFlagForCountry(namedParameterJdbcTemplate, list.get(0).getCode2());
                log.info("First result's flag : {}", emojiFlag);
            }

            // wrap list avec DataAccessUtils.singleResult() ?
            CountryDTO singleResult = DataAccessUtils.singleResult(list);

            log.info("Stop 🛑");
        };
    }

    public String getFlagForCountry(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                    String code2) {
        String sql = "select emoji from country_flag cf where cf.code2 = :code2 ";
        return namedParameterJdbcTemplate.queryForObject(sql,
                Map.of("code2", code2),
                (rs, rowNum) -> rs.getString(1));
    }

    private static RowMapper<CountryDTO> beanRowMapper() {
        return new BeanPropertyRowMapper<>(CountryDTO.class);
    }

    private static CountryDTO mapRowSimple(ResultSet rs, int rowNum) throws SQLException {
        String cName = rs.getString("name");
        log.info("Found row #{} ! ['{}']", rowNum, cName);
        CountryDTO countryDTO = new CountryDTO();
        countryDTO.setName(cName);
        return countryDTO;
    }
}
