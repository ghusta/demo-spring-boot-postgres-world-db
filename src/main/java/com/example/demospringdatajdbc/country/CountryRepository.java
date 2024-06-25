package com.example.demospringdatajdbc.country;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * {@link ListCrudRepository} extends {@link org.springframework.data.repository.CrudRepository}.
 */
public interface CountryRepository extends ListCrudRepository<CountryDTO, String> {

    Optional<CountryDTO> findByCode(String code);

    @Query("SELECT * FROM country c WHERE c.name ILIKE :name ")
    List<CountryDTO> findByNameLike(String name);

    List<CountryDTO> findByContinent(String continent);

}
