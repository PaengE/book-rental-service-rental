package com.my.rental.service;

import com.my.rental.domain.OverdueItem;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OverdueItemService {
    /**
     * Save a overdueItem.
     *
     * @param overdueItem the entity to save.
     * @return the persisted entity.
     */
    OverdueItem save(OverdueItem overdueItem);

    /**
     * Get all the overdueItem.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OverdueItem> findAll(Pageable pageable);

    /**
     * Get the "id" overdueItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OverdueItem> findOne(Long id);

    /**
     * Delete the "id" overdueItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Page<OverdueItem> findOverdueItemsByRental(Long rentalId, Pageable pageable);
}
