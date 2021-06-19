package com.my.rental.web.rest.dto;

import java.io.Serializable;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OverdueItemDTO implements Serializable {

    private Long id;

    private Long bookId;

    private LocalDate dueDate;

    private String bookTitle;

    private Long rentalId;
}
