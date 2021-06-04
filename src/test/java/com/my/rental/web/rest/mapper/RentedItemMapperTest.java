package com.my.rental.web.rest.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;

class RentedItemMapperTest {

    private RentedItemMapper rentedItemMapper;

    @BeforeEach
    public void setUp() {
        rentedItemMapper = new RentedItemMapperImpl();
    }
}
