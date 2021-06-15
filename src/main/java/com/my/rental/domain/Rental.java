package com.my.rental.domain;

import com.my.rental.domain.enumeration.RentalStatus;
import com.my.rental.web.rest.errors.RentUnavailableException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Rental.
 */
@Entity
@Table(name = "rental")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
public class Rental implements Serializable {

    private static final long serialVersionUID = 1L;

    // Rental 일련번호
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    // 사용자 일련번호
    @Column(name = "user_id")
    private Long userId;

    // 대출 가능 여부
    @Enumerated(EnumType.STRING)
    @Column(name = "rental_status")
    private RentalStatus rentalStatus;

    // 연체료
    @Column(name = "late_fee")
    private int lateFee;

    // 대출아이템 (고아 객체 제거 -> rental에서 컬렉션의 객체 삭제시, 해당 컬렉션의 entity삭제)
    @OneToMany(mappedBy = "rental", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RentedItem> rentedItems = new HashSet<>();

    // 연체아이템 (고아 객체 제거 -> rental에서 컬렉션의 객체 삭제시, 해당 컬렉션의 entity삭제)
    @OneToMany(mappedBy = "rental", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<OverdueItem> overdueItems = new HashSet<>();

    // 반납아이템 (고아 객체 제거 -> rental에서 컬렉션의 객체 삭제시, 해당 컬렉션의 entity삭제)
    @OneToMany(mappedBy = "rental", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ReturnedItem> returnedItems = new HashSet<>();

    public Rental userId(Long userId) {
        this.userId = userId;
        return this;
    }

    // Rental 엔티티 생성
    public static Rental createRental(Long userId) {
        Rental rental = new Rental();
        rental.setUserId(userId);
        rental.setRentalStatus(RentalStatus.RENT_AVAILABLE);
        rental.setLateFee(0);
        return rental;
    }

    // 대출아이템 추가
    public Rental addRentedItem(RentedItem rentedItem) {
        this.rentedItems.add(rentedItem);
        rentedItem.setRental(this);
        return this;
    }

    // 대출아이템 삭제
    public Rental removeRentedItem(RentedItem rentedItem) {
        this.rentedItems.remove(rentedItem);
        //        rentedItem.setRental(null);
        return this;
    }

    // 연체아이템 추가
    public Rental addOverdueItem(OverdueItem overdueItem) {
        this.overdueItems.add(overdueItem);
        overdueItem.setRental(this);
        return this;
    }

    // 연체아이템 삭제
    public Rental removeOverdueItem(OverdueItem overdueItem) {
        this.overdueItems.remove(overdueItem);
        //        overdueItem.setRental(null);
        return this;
    }

    // 반납아이템 추가
    public Rental addReturnedItem(ReturnedItem returnedItem) {
        this.returnedItems.add(returnedItem);
        returnedItem.setRental(this);
        return this;
    }

    // 반납아이템 삭제
    public Rental removeReturnedItem(ReturnedItem returnedItem) {
        this.returnedItems.remove(returnedItem);
        //        returnedItem.setRental(null);
        return this;
    }

    // 대출 가능 여부 체크
    public boolean checkRentalAvailable() throws RentUnavailableException {
        if (this.rentalStatus.equals(RentalStatus.RENT_UNAVAILABLE) || this.getLateFee() != 0) {
            throw new RentUnavailableException("연체 상태입니다. 연체료를 정산 후, 도서를 대출하실 수 있습니다.");
        }
        if (this.getRentedItems().size() >= 5) {
            throw new RentUnavailableException("대출 가능한 도서의 수는 " + (5 - this.getRentedItems().size()) + "권 입니다.");
        }
        return true;
    }

    // 대출 처리 메소드
    public Rental rentBook(Long bookId, String title) {
        this.addRentedItem(RentedItem.createRentedItem(bookId, title, LocalDate.now()));
        return this;
    }

    // 반납 처리 메소드
    public Rental returnBook(Long bookId) {
        RentedItem rentedItem = this.rentedItems.stream().filter(item -> item.getBookId().equals(bookId)).findFirst().get();
        this.addReturnedItem(ReturnedItem.createReturnedItem(rentedItem.getBookId(), rentedItem.getBookTitle(), LocalDate.now()));
        this.removeRentedItem(rentedItem);
        return this;
    }

    // 연체 처리 메소드
    public Rental overdueBook(Long bookId) {
        RentedItem rentedItem = this.rentedItems.stream().filter(item -> item.getBookId().equals(bookId)).findFirst().get();
        this.addOverdueItem(OverdueItem.createOverdueItem(rentedItem.getBookId(), rentedItem.getBookTitle(), rentedItem.getDueDate()));
        this.removeRentedItem(rentedItem);
        return this;
    }

    // 연체 아이템 반납 처리 메소드
    public Rental returnOverdueBook(Long bookId) {
        OverdueItem overdueItem = this.overdueItems.stream().filter(item -> item.getBookId().equals(bookId)).findFirst().get();
        this.addReturnedItem(ReturnedItem.createReturnedItem(overdueItem.getBookId(), overdueItem.getBookTitle(), LocalDate.now()));
        this.removeOverdueItem(overdueItem);
        return this;
    }

    // 대출 불가 처리 메소드
    public Rental makeRentUnable() {
        this.setRentalStatus(RentalStatus.RENT_UNAVAILABLE);
        this.setLateFee(this.getLateFee() + 30); // 도서 연체 시 연체료 + 30
        return this;
    }

    // 대출 불가 해제 메소드
    public Rental releaseOverdue() {
        this.setLateFee(0);
        this.setRentalStatus(RentalStatus.RENT_AVAILABLE);
        return this;
    }

    public Rental lateFee(int lateFee) {
        this.lateFee = lateFee;
        return this;
    }
}
