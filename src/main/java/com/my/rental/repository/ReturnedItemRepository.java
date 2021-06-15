package com.my.rental.repository;

import com.my.rental.domain.Rental;
import com.my.rental.domain.ReturnedItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface ReturnedItemRepository extends JpaRepository<ReturnedItem, Long> {
    ReturnedItem findByBookId(Long bookId);
    Page<ReturnedItem> findByRental(Rental rental, Pageable pageable);
}
