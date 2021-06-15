package com.my.rental.web.rest.mapper;

import com.my.rental.domain.*;
import com.my.rental.web.rest.dto.RentedItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RentedItem} and its DTO {@link RentedItemDTO}.
 */
@Mapper(componentModel = "spring", uses = { RentalMapper.class })
public interface RentedItemMapper extends EntityMapper<RentedItemDTO, RentedItem> {
    @Mapping(target = "rental", source = "rental", qualifiedByName = "id")
    RentedItemDTO toDto(RentedItem s);

    @Mapping(source = "rentalId", target = "rental")
    RentedItem toEntity(RentedItemDTO rentedItemDTO);

    default RentedItem fromId(Long id) {
        if (id == null) {
            return null;
        }
        RentedItem rentedItem = new RentedItem();
        rentedItem.setId(id);
        return rentedItem;
    }
}
