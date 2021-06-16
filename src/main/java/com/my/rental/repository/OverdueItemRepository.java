package com.my.rental.repository;

import com.my.rental.domain.OverdueItem;
import com.my.rental.domain.Rental;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface OverdueItemRepository extends JpaRepository<OverdueItem, Long> {
    Page<OverdueItem> findByRental(Rental rental, Pageable pageable);
}
