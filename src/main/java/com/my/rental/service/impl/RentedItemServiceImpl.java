package com.my.rental.service.impl;

import com.my.rental.domain.RentedItem;
import com.my.rental.repository.RentedItemRepository;
import com.my.rental.service.RentedItemService;
import com.my.rental.web.rest.dto.RentedItemDTO;
import com.my.rental.web.rest.mapper.RentedItemMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RentedItem}.
 */
@Service
@Transactional
public class RentedItemServiceImpl implements RentedItemService {

    private final Logger log = LoggerFactory.getLogger(RentedItemServiceImpl.class);

    private final RentedItemRepository rentedItemRepository;

    private final RentedItemMapper rentedItemMapper;

    public RentedItemServiceImpl(RentedItemRepository rentedItemRepository, RentedItemMapper rentedItemMapper) {
        this.rentedItemRepository = rentedItemRepository;
        this.rentedItemMapper = rentedItemMapper;
    }

    @Override
    public RentedItemDTO save(RentedItemDTO rentedItemDTO) {
        log.debug("Request to save RentedItem : {}", rentedItemDTO);
        RentedItem rentedItem = rentedItemMapper.toEntity(rentedItemDTO);
        rentedItem = rentedItemRepository.save(rentedItem);
        return rentedItemMapper.toDto(rentedItem);
    }

    @Override
    public Optional<RentedItemDTO> partialUpdate(RentedItemDTO rentedItemDTO) {
        log.debug("Request to partially update RentedItem : {}", rentedItemDTO);

        return rentedItemRepository
            .findById(rentedItemDTO.getId())
            .map(
                existingRentedItem -> {
                    rentedItemMapper.partialUpdate(existingRentedItem, rentedItemDTO);
                    return existingRentedItem;
                }
            )
            .map(rentedItemRepository::save)
            .map(rentedItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RentedItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RentedItems");
        return rentedItemRepository.findAll(pageable).map(rentedItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RentedItemDTO> findOne(Long id) {
        log.debug("Request to get RentedItem : {}", id);
        return rentedItemRepository.findById(id).map(rentedItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RentedItem : {}", id);
        rentedItemRepository.deleteById(id);
    }
}
