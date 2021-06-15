package com.my.rental.web.rest.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LateFeeDTO implements Serializable {

    private Long userId;
    private int lateFee;
}
