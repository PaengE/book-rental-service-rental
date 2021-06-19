package com.my.rental.web.rest.dto;

import com.my.rental.domain.enumeration.RentalStatus;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO for the {@link com.my.rental.domain.Rental} entity.
 */

@Getter
@Setter
public class RentalDTO implements Serializable {

    private Long id;

    private Long userId;

    private RentalStatus rentalStatus;

    private int lateFee;
}
