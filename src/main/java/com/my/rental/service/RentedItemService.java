package com.my.rental.service;

import com.my.rental.domain.RentedItem;
import com.my.rental.web.rest.dto.RentedItemDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.my.rental.domain.RentedItem}.
 */
public interface RentedItemService {
    /**
     * Save a rentedItem.
     *
     * @param rentedItem the entity to save.
     * @return the persisted entity.
     */
    RentedItem save(RentedItem rentedItem);

    /**
     * Get all the rentedItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RentedItem> findAll(Pageable pageable);

    /**
     * Get the "id" rentedItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RentedItem> findOne(Long id);

    /**
     * Delete the "id" rentedItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<RentedItem> findAllForManage();

    Page<RentedItem> findByTitle(String title, Pageable pageable);

    Page<RentedItem> findRentedItemsByRental(Long rentalId, Pageable pageable);
}
