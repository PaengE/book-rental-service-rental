package com.my.rental.repository;

import com.my.rental.domain.Rental;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Rental entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    Optional<Rental> findByUserId(Long id);
}
