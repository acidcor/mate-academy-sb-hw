package mate.academy.hw.mapper;

import mate.academy.hw.config.MapperConfig;
import mate.academy.hw.dto.cart.item.CartItemResponseDto;
import mate.academy.hw.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    @Mapping(source = "id", target = "bookId")
    CartItemResponseDto toDto(CartItem cartItem);
}
