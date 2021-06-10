package com.my.rental.service;

import com.my.rental.domain.Rental;
import com.my.rental.domain.ReturnedItem;
import com.my.rental.web.rest.dto.RentedItemDTO;
import com.my.rental.web.rest.dto.ReturnedItemDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReturnedItemService {
    /**
     * Save a returnedItem.
     *
     * @param returnedItem the entity to save.
     * @return the persisted entity.
     */
    ReturnedItem save(ReturnedItem returnedItem);

    /**
     * Get all the returnedItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReturnedItem> findAll(Pageable pageable);

    /**
     * Get the "id" returnedItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReturnedItem> findOne(Long id);

    /**
     * Delete the "id" returnedItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    ReturnedItem findByBookId(Long bookId);

    Page<ReturnedItem> findByRental(Rental rental, java.awt.print.Pageable pageable);
}
