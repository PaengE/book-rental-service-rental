package com.my.rental.service;

import com.my.rental.web.rest.dto.RentedItemDTO;
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
     * @param rentedItemDTO the entity to save.
     * @return the persisted entity.
     */
    RentedItemDTO save(RentedItemDTO rentedItemDTO);

    /**
     * Partially updates a rentedItem.
     *
     * @param rentedItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RentedItemDTO> partialUpdate(RentedItemDTO rentedItemDTO);

    /**
     * Get all the rentedItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RentedItemDTO> findAll(Pageable pageable);

    /**
     * Get the "id" rentedItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RentedItemDTO> findOne(Long id);

    /**
     * Delete the "id" rentedItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
