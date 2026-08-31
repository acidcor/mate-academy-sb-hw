package mate.academy.hw.mapper;

import mate.academy.hw.config.MapperConfig;
import mate.academy.hw.dto.cart.ShoppingCartResponseDto;
import mate.academy.hw.dto.cart.item.CartItemResponseDto;
import mate.academy.hw.model.CartItem;
import mate.academy.hw.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "cartItems", target = "cartItems")
    ShoppingCartResponseDto toDto(ShoppingCart shoppingCart);

    @Mapping(source = "id", target = "bookId")
    CartItemResponseDto toDto(CartItem cartItem);
}
