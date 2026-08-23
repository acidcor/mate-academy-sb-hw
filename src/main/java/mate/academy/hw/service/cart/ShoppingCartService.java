package mate.academy.hw.service.cart;

import mate.academy.hw.dto.cart.CartItemRequestDto;
import mate.academy.hw.dto.cart.CartItemResponseDto;
import mate.academy.hw.dto.cart.ShoppingCartResponseDto;
import org.springframework.security.core.Authentication;

public interface ShoppingCartService {
    ShoppingCartResponseDto getCart(Authentication authentication);

    CartItemResponseDto saveItem(Authentication authentication, CartItemRequestDto dto);

    CartItemResponseDto updateBooks(
            Authentication authentication,
            Long cartItemId,
            int quantity
    );

    void deleteBook(Authentication authentication, Long cartItemId);
}
