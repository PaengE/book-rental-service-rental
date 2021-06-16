package com.my.rental.web.rest.dto;

import java.io.Serializable;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO for the {@link com.my.rental.domain.RentedItem} entity.
 */

@Getter
@Setter
public class RentedItemDTO implements Serializable {

    private Long id;

    private Long bookId;

    private LocalDate rentedDate;

    private LocalDate dueDate;

    private RentalDTO rental;
}
