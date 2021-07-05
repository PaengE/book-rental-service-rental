package com.my.rental.domain.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class UserIdCreated {

    private Long userId;
}
