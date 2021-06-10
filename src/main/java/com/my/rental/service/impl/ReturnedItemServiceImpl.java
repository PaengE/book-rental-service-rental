package com.my.rental.service.impl;

import com.my.rental.domain.Rental;
import com.my.rental.domain.ReturnedItem;
import com.my.rental.service.ReturnedItemService;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ReturnedItemServiceImpl implements ReturnedItemService {

    @Override
    public ReturnedItem save(ReturnedItem returnedItem) {
        return null;
    }

    @Override
    public Page<ReturnedItem> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Optional<ReturnedItem> findOne(Long id) {
        return Optional.empty();
    }

    @Override
    public void delete(Long id) {}

    @Override
    public ReturnedItem findByBookId(Long bookId) {
        return null;
    }

    @Override
    public Page<ReturnedItem> findByRental(Rental rental, java.awt.print.Pageable pageable) {
        return null;
    }
}
