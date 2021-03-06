package com.my.rental.web.rest.mapper;

import com.my.rental.domain.OverdueItem;
import com.my.rental.web.rest.dto.OverdueItemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link OverdueItem} and its DTO {@link OverdueItemDTO}.
 */
@Mapper(componentModel = "spring", uses = { RentalMapper.class })
public interface OverdueItemMapper extends EntityMapper<OverdueItemDTO, OverdueItem> {
    @Mapping(source = "rental.id", target = "rentalId")
    OverdueItemDTO toDto(OverdueItem overdueItem);

    @Mapping(source = "rentalId", target = "rental")
    OverdueItem toEntity(OverdueItemDTO overdueItemDTO);

    default OverdueItem fromId(Long id) {
        if (id == null) {
            return null;
        }
        OverdueItem overdueItem = new OverdueItem();
        overdueItem.setId(id);
        return overdueItem;
    }
}
