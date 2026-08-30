package mate.academy.hw.dto.cart;

import java.util.Set;
import mate.academy.hw.dto.cart.item.CartItemResponseDto;

public record ShoppingCartResponseDto(Long id, Long userId, Set<CartItemResponseDto> cartItems) {
}
