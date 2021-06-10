package com.my.rental.web.rest.dto;

import java.io.Serializable;
import java.time.LocalDate;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ReturnedItemDTO implements Serializable {

    private Long id;

    private Long bookId;

    private String bookTitle;

    private LocalDate returnedDate;

    private RentalDTO rental;
}
