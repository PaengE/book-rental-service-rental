package com.my.rental.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Setter
@Getter
@Entity
@Table(name = "returned_item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ReturnedItem implements Serializable {

    // 반납 아이템 일련번호
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    // 반납한 도서의 일련번호
    @Column(name = "book_id")
    private Long bookId;

    // 반납한 일자
    @Column(name = "returned_date")
    private LocalDate returnedDate;

    // 반납한 도서명
    @Column(name = "book_title")
    private String bookTitle;

    // 연관 Rental
    @ManyToOne
    @JsonIgnoreProperties("returnedItems")
    private Rental rental;

    // 반납 아이템 생성 메소드
    public static ReturnedItem createReturnedItem(Long bookId, String bookTitle, LocalDate returnedDate) {
        ReturnedItem returnedItem = new ReturnedItem();
        returnedItem.setBookId(bookId);
        returnedItem.setBookTitle(bookTitle);
        returnedItem.setReturnedDate(returnedDate);
        return returnedItem;
    }

    public ReturnedItem bookId(Long bookId) {
        this.bookId = bookId;
        return this;
    }

    public ReturnedItem returnedDate(LocalDate returnedDate) {
        this.returnedDate = returnedDate;
        return this;
    }

    public ReturnedItem bookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
        return this;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public ReturnedItem rental(Rental rental) {
        this.rental = rental;
        return this;
    }
}
