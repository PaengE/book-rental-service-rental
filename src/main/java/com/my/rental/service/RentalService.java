package com.my.rental.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.my.rental.domain.Rental;
import com.my.rental.domain.event.UserIdCreated;
import com.my.rental.web.rest.dto.RentalDTO;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.my.rental.domain.Rental}.
 */
public interface RentalService {
    /**
     * Save a rental.
     *
     * @param rental the entity to save.
     * @return the persisted entity.
     */
    Rental save(Rental rental);

    /**
     * Get all the rentals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Rental> findAll(Pageable pageable);

    /**
     * Get the "id" rental.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Rental> findOne(Long id);

    /**
     * Delete the "id" rental.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     *
     * User 생성시 Rental 생성
     *
     * **/
    Rental createRental(UserIdCreated userIdCreated);

    /****
     *
     * Business Logic
     *
     * 책 대출하기
     *
     * ****/
    Rental rentBook(Long userId, Long bookId, String bookTitle) throws InterruptedException, ExecutionException, JsonProcessingException;

    /****
     *
     * Business Logic
     *
     * 책 반납하기
     *
     * ****/
    Rental returnBook(Long userId, Long bookIds) throws ExecutionException, InterruptedException, JsonProcessingException;

    /****
     *
     * Business Logic
     *
     * 연체 도서 반납하기
     *
     * ****/
    Rental returnOverdueBook(Long userid, Long book) throws ExecutionException, InterruptedException, JsonProcessingException;

    /****
     *
     * Business Logic
     *
     * 연체 상태 해제하기
     *
     * ****/
    Rental releaseOverdue(Long userId);

    /****
     *
     * Business Logic
     *
     * 연체 상태로 변경
     *
     * ****/
    Long beOverdueBook(Long rentalId, Long bookId);

    /**
     * Rental 조회
     *
     * @param userId
     * @return
     */
    Optional<Rental> findRentalByUser(Long userId);

    /**
     * 연체료 조회
     *
     * @param userId
     * @return
     */
    int findLateFee(Long userId);
}
