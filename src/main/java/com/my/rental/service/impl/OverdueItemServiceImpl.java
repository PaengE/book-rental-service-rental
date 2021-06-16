package com.my.rental.service.impl;

import com.my.rental.domain.OverdueItem;
import com.my.rental.domain.Rental;
import com.my.rental.repository.OverdueItemRepository;
import com.my.rental.service.OverdueItemService;
import com.my.rental.service.RentalService;
import com.my.rental.web.rest.mapper.OverdueItemMapper;
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
public class OverdueItemServiceImpl implements OverdueItemService {

    private final Logger log = LoggerFactory.getLogger(OverdueItemServiceImpl.class);

    private final OverdueItemRepository overdueItemRepository;
    private final OverdueItemMapper overdueItemMapper;
    private final RentalService rentalService;

    @Override
    public OverdueItem save(OverdueItem overdueItem) {
        log.debug("Request to save OverdueItem : {}", overdueItem);
        return overdueItemRepository.save(overdueItem);
    }

    @Override
    public Page<OverdueItem> findAll(Pageable pageable) {
        log.debug("Request to get all OverdueItems");
        return overdueItemRepository.findAll(pageable);
    }

    @Override
    public Optional<OverdueItem> findOne(Long id) {
        log.debug("Request to get OverdueItem : {}", id);
        return overdueItemRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OverdueItem : {}", id);
        overdueItemRepository.deleteById(id);
    }

    @Override
    public Page<OverdueItem> findOverdueItemsByRental(Long rentalId, Pageable pageable) {
        Rental rental = rentalService.findOne(rentalId).get();
        return overdueItemRepository.findByRental(rental, pageable);
    }
}
