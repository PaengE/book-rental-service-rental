package com.my.rental.web.rest.mapper;

import com.my.rental.domain.*;
import com.my.rental.web.rest.dto.RentalDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Rental} and its DTO {@link RentalDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RentalMapper extends EntityMapper<RentalDTO, Rental> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RentalDTO toDtoId(Rental rental);

    @Mapping(target = "rentedItems", ignore = true)
    @Mapping(target = "removeRentedItem", ignore = true)
    @Mapping(target = "overdueItems", ignore = true)
    @Mapping(target = "removeOverdueItem", ignore = true)
    @Mapping(target = "returnedItems", ignore = true)
    @Mapping(target = "removeReturnedItem", ignore = true)
    Rental toEntity(RentalDTO rentalDTO);

    default Rental fromId(Long id) {
        if (id == null) {
            return null;
        }
        Rental rental = new Rental();
        rental.setId(id);
        return rental;
    }
}
