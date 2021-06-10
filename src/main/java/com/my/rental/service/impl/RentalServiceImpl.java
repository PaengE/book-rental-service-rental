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

    @Transactional
    public Rental rentBook(Long userId, Long bookId, String bookTitle)
        throws InterruptedException, ExecutionException, JsonProcessingException, RentUnavailableException {
        log.debug("Rent Books by : ", userId, " Book List : ", bookId + bookTitle);
        Rental rental = rentalRepository.findByUserId(userId).get();
        rental.checkRentalAvailable();

        rental = rental.rentBook(bookId, bookTitle);
        rentalRepository.save(rental);

        rentalProducer.updateBookStatus(bookId, "UNAVAILABLE"); //send to book service
        rentalProducer.updateBookCatalogStatus(bookId, "RENT_BOOK"); //send to book catalog service
        rentalProducer.savePoints(userId, pointPerBooks); //send to user service

        return rental;
    }
}
