package com.my.rental.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PointChanged {

    private Long userId;
    private int points;
}
