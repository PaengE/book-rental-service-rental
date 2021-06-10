package com.my.rental.service.impl;

import com.my.rental.domain.Rental;
import com.my.rental.domain.RentedItem;
import com.my.rental.repository.RentedItemRepository;
import com.my.rental.service.RentalService;
import com.my.rental.service.RentedItemService;
import com.my.rental.web.rest.dto.RentedItemDTO;
import com.my.rental.web.rest.mapper.RentedItemMapper;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RentedItem}.
 */
@RequiredArgsConstructor
@Service
@Transactional
public class RentedItemServiceImpl implements RentedItemService {

    private final Logger log = LoggerFactory.getLogger(RentedItemServiceImpl.class);

    private final RentedItemRepository rentedItemRepository;
    private final RentalService rentalService;

    @Override
    public RentedItem save(RentedItem rentedItem) {
        log.debug("Request to save RentedItem : {}", rentedItem);
        return rentedItemRepository.save(rentedItem);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RentedItem> findAll(Pageable pageable) {
        log.debug("Request to get all RentedItems");
        return rentedItemRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RentedItem> findOne(Long id) {
        log.debug("Request to get RentedItem : {}", id);
        return rentedItemRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RentedItem : {}", id);
        rentedItemRepository.deleteById(id);
    }

    @Override
    public List<RentedItem> findAllForManage() {
        return rentedItemRepository.findAll();
    }

    @Override
    public Page<RentedItem> findByTitle(String title, Pageable pageable) {
        return rentedItemRepository.findByBookTitleContaining(title, pageable);
    }

    @Override
    public Page<RentedItem> findRentedItemsByRental(Long rentalId, Pageable pageable) {
        Rental rental = rentalService.findOne(rentalId).get();
        return rentedItemRepository.findByRental(rental, pageable);
    }
}
