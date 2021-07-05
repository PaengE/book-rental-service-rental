package com.my.rental.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@AllArgsConstructor
public class StockChanged {

    private Long bookId;
    private String bookStatus;
}
