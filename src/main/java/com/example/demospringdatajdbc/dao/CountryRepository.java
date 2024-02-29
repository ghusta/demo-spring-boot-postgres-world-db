package com.example.demospringdatajdbc.dao;

import com.example.demospringdatajdbc.domain.CountryDTO;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CountryRepository extends CrudRepository<CountryDTO, String> {

    Optional<CountryDTO> findByCode(String code);

    @Query("SELECT * FROM country c WHERE c.name ILIKE :name ")
    List<CountryDTO> findByNameLike(String name);

    List<CountryDTO> findByContinent(String continent);

}
