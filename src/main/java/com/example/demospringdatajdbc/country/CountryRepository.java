package com.example.demospringdatajdbc.country;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * {@link ListCrudRepository} extends {@link org.springframework.data.repository.CrudRepository}.
 */
public interface CountryRepository extends JpaRepository<Country, String> {

    Optional<Country> findByCode(String code);

    //@Query("SELECT c FROM country c WHERE c.name ILIKE :name ")
    List<Country> findByNameLike(String name);

    // See : https://docs.spring.io/spring-data/jpa/reference/repositories/projections.html#projection.dynamic
    @Query("SELECT new com.example.demospringdatajdbc.country.CountryLightDTO(c.code, c.code2, c.name) FROM country c WHERE c.name ILIKE :name ")
    List<CountryLightDTO> findByNameLikeDTO(String name);

    List<CountryDTO> findByContinent(String continent);

}
