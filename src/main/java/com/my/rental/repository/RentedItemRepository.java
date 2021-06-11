package com.my.rental.repository;

import com.my.rental.domain.Rental;
import com.my.rental.domain.RentedItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RentedItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RentedItemRepository extends JpaRepository<RentedItem, Long> {
    RentedItem findByBookId(Long bookId);
    Page<RentedItem> findByRental(Rental rental, Pageable pageable);
    Page<RentedItem> findByBookTitleContaining(String bookTitle, Pageable pageable);
}
