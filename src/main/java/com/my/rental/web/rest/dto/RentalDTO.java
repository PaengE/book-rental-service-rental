package com.my.rental.web.rest.dto;

import com.my.rental.domain.enumeration.RentalStatus;
import java.io.Serializable;
import java.util.Objects;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * A DTO for the {@link com.my.rental.domain.Rental} entity.
 */

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class RentalDTO implements Serializable {

    private Long id;

    private Long userId;

    private RentalStatus rentalStatus;
}
