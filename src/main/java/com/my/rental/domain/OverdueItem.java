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
@Table(name = "overdue_item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class OverdueItem implements Serializable {

    // 연체 아이템 일련번호
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    // 연체된 도서의 일련번호
    @Column(name = "book_id")
    private Long bookId;

    // 도서 반납 일자
    @Column(name = "due_date")
    private LocalDate dueDate;

    // 반납한 도서명
    @Column(name = "book_title")
    private String bookTitle;

    // 연관 Rental
    @ManyToOne
    @JsonIgnoreProperties("overdueItems")
    private Rental rental;

    // 연체 아이템 생성 메소드
    public static OverdueItem createOverdueItem(Long bookId, String bookTitle, LocalDate dueDate) {
        OverdueItem overdueItem = new OverdueItem();
        overdueItem.setBookId(bookId);
        overdueItem.setBookTitle(bookTitle);
        overdueItem.setDueDate(dueDate);

        return overdueItem;
    }

    public OverdueItem bookId(Long bookId) {
        this.bookId = bookId;
        return this;
    }

    public OverdueItem dueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public OverdueItem bookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
        return this;
    }

    public OverdueItem rental(Rental rental) {
        this.rental = rental;
        return this;
    }
}
