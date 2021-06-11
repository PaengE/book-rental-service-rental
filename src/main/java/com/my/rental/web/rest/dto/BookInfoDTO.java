package com.my.rental.web.rest.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * 도서 정보 데이터 전송 객체
 */
@Getter
@Setter
public class BookInfoDTO implements Serializable {

    private Long id;
    private String title;
}
