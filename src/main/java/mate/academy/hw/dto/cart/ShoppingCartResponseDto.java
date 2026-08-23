package mate.academy.hw.dto.cart;

import java.util.Set;

public record ShoppingCartResponseDto(Long id, Long userId, Set<CartItemResponseDto> cartItems) {
}
