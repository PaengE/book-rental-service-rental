package com.my.rental.service.impl;

import com.my.rental.domain.Rental;
import com.my.rental.domain.ReturnedItem;
import com.my.rental.repository.RentalRepository;
import com.my.rental.repository.ReturnedItemRepository;
import com.my.rental.service.RentalService;
import com.my.rental.service.ReturnedItemService;
import com.my.rental.web.rest.mapper.ReturnedItemMapper;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Transactional
public class ReturnedItemServiceImpl implements ReturnedItemService {

    private final Logger log = LoggerFactory.getLogger(ReturnedItemServiceImpl.class);

    private final ReturnedItemRepository returnedItemRepository;
    private final ReturnedItemMapper returnedItemMapper;
    private final RentalService rentalService;

    @Override
    public ReturnedItem save(ReturnedItem returnedItem) {
        log.debug("Request to save ReturnedItem : {}", returnedItem);
        return returnedItemRepository.save(returnedItem);
    }

    @Override
    public Page<ReturnedItem> findAll(Pageable pageable) {
        log.debug("Request to get all ReturnedItems");
        return returnedItemRepository.findAll(pageable);
    }

    @Override
    public Optional<ReturnedItem> findOne(Long id) {
        log.debug("Request to get ReturnedItem : {}", id);
        return returnedItemRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ReturnedItem : {}", id);
        returnedItemRepository.deleteById(id);
    }

    @Override
    public Page<ReturnedItem> findReturnedItemsByRental(Long rentalId, Pageable pageable) {
        Rental rental = rentalService.findOne(rentalId).get();
        return returnedItemRepository.findByRental(rental, pageable);
    }
}
