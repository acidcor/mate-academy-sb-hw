package mate.academy.hw.service.cart;

import mate.academy.hw.dto.cart.ShoppingCartResponseDto;
import mate.academy.hw.dto.cart.UpdateCartItemDto;
import mate.academy.hw.dto.cart.item.CartItemRequestDto;
import mate.academy.hw.model.User;
import org.springframework.security.core.Authentication;

public interface ShoppingCartService {
    ShoppingCartResponseDto getCart(Authentication authentication);

    ShoppingCartResponseDto saveItem(Authentication authentication, CartItemRequestDto dto);

    ShoppingCartResponseDto updateBooks(
            Authentication authentication,
            Long cartItemId,
            UpdateCartItemDto updateDto
    );

    void deleteBook(Authentication authentication, Long cartItemId);

    void addCart(User user);
}
