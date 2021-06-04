package com.my.rental.web.rest.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;

class RentalMapperTest {

    private RentalMapper rentalMapper;

    @BeforeEach
    public void setUp() {
        rentalMapper = new RentalMapperImpl();
    }
}
