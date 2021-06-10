package com.my.rental.repository;

import com.my.rental.domain.OverdueItem;
import com.my.rental.domain.Rental;
import java.awt.print.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OverdueItemRepository extends JpaRepository<OverdueItem, Long> {
    Page<OverdueItem> findByRental(Rental rental, Pageable pageable);
}
