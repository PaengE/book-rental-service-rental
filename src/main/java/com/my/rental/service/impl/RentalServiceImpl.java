package com.my.rental.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.my.rental.adapter.RentalProducer;
import com.my.rental.domain.Rental;
import com.my.rental.repository.RentalRepository;
import com.my.rental.service.RentalService;
import com.my.rental.web.rest.dto.RentalDTO;
import com.my.rental.web.rest.errors.RentUnavailableException;
import com.my.rental.web.rest.mapper.RentalMapper;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Rental}.
 */
@RequiredArgsConstructor
@Service
@Transactional
public class RentalServiceImpl implements RentalService {

    private final Logger log = LoggerFactory.getLogger(RentalServiceImpl.class);

    private final RentalRepository rentalRepository;

    private final RentalProducer rentalProducer;

    private int pointPerBooks = 30;

    @Override
    public Rental save(Rental rental) {
        log.debug("Request to save Rental : {}", rental);
        return rentalRepository.save(rental);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Rental> findAll(Pageable pageable) {
        log.debug("Request to get all Rentals");
        return rentalRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Rental> findOne(Long id) {
        log.debug("Request to get Rental : {}", id);
        return rentalRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Rental : {}", id);
        rentalRepository.deleteById(id);
    }

    /**
     * 도서 대출하기
     *
     * @param userId
     * @param bookId
     * @param bookTitle
     * @return
     */
    @Transactional
    public Rental rentBook(Long userId, Long bookId, String bookTitle)
        throws InterruptedException, ExecutionException, JsonProcessingException, RentUnavailableException {
        log.debug("Rent Books by : ", userId, " Book List : ", bookId + bookTitle);
        Rental rental = rentalRepository.findByUserId(userId).get(); // Rental 조회
        rental.checkRentalAvailable(); // 대출 가능 상태 조회

        rental = rental.rentBook(bookId, bookTitle); // Rental에 대출 처리 위임
        rentalRepository.save(rental); // Rental 저장

        // 도서 서비스에 도서 재고 감소를 위해 도서대출 이벤트 발송
        rentalProducer.updateBookStatus(bookId, "UNAVAILABLE");

        // 돗서 카탈로그 서비스에 대출된 도서로 상태를 변경하기 위한 이벤트 발송
        rentalProducer.updateBookCatalogStatus(bookId, "RENT_BOOK");

        // 대출로 인한 사용자 포인트 적립을 위해 사용자 서비스에 이벤트 발송
        rentalProducer.savePoints(userId, pointPerBooks);

        return rental;
    }

    /**
     * 도서 반납하기
     *
     * @param userId
     * @param bookId
     * @return
     */
    @Transactional
    public Rental returnBook(Long userId, Long bookId) throws ExecutionException, InterruptedException, JsonProcessingException {
        Rental rental = rentalRepository.findByUserId(userId).get(); // Rental 조회
        rental = rental.returnBook(bookId); // Rental에 반납 처리 위임
        rental = rentalRepository.save(rental); // Rental 저장

        // 도서 서비스에 도서재고 증가를 위해 도서반납 이벤트 발송
        rentalProducer.updateBookStatus(bookId, "AVAILABLE");

        // 도서 카탈로그 서비스에 대출 가능한 도서로 상태를 변경하기 위한 이벤트 발송
        rentalProducer.updateBookCatalogStatus(bookId, "RETURN_BOOK");

        return rental;
    }
}
