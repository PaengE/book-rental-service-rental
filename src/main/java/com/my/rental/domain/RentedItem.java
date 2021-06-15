package com.my.rental.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RentedItem.
 */
@Setter
@Getter
@Entity
@Table(name = "rented_item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RentedItem implements Serializable {

    private static final long serialVersionUID = 1L;

    // 대출 아이템 일련번호
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    // 대출한 도서의 일련번호
    @Column(name = "book_id")
    private Long bookId;

    // 대출 도서명
    @Column(name = "book_title")
    private String bookTitle;

    // 대출 시작 일자
    @Column(name = "rented_date")
    private LocalDate rentedDate;

    // 반납 예정 일자
    @Column(name = "due_date")
    private LocalDate dueDate;

    // 연관 Rental
    @ManyToOne
    @JsonIgnoreProperties("rentedItems")
    private Rental rental;

    // 대출 아이템 생성 메소드
    public static RentedItem createRentedItem(Long bookId, String bookTitle, LocalDate rentedDate) {
        RentedItem rentedItem = new RentedItem();
        rentedItem.setBookId(bookId);
        rentedItem.setBookTitle(bookTitle);
        rentedItem.setRentedDate(rentedDate);
        rentedItem.setDueDate(rentedDate.plusWeeks(2));
        return rentedItem;
    }

    public RentedItem bookId(Long bookId) {
        this.bookId = bookId;
        return this;
    }

    public RentedItem rentedDate(LocalDate rentedDate) {
        this.rentedDate = rentedDate;
        return this;
    }

    public RentedItem bookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
        return this;
    }

    public RentedItem dueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        return this;
    }
}
