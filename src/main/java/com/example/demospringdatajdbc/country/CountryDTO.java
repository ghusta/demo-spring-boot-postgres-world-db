package com.example.demospringdatajdbc.country;

import java.math.BigInteger;

public record CountryDTO(String code,
                         String code2,
                         String name,
                         String continent,
                         BigInteger population,
                         String headOfState) {
}
