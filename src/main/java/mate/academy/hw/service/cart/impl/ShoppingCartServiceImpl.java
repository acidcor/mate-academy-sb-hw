package mate.academy.hw.service.cart.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.hw.dto.cart.CartItemRequestDto;
import mate.academy.hw.dto.cart.CartItemResponseDto;
import mate.academy.hw.dto.cart.ShoppingCartResponseDto;
import mate.academy.hw.exceptrion.EntityNotFoundException;
import mate.academy.hw.mapper.CartItemMapper;
import mate.academy.hw.mapper.ShoppingCartMapper;
import mate.academy.hw.model.CartItem;
import mate.academy.hw.model.ShoppingCart;
import mate.academy.hw.repository.cart.CartItemRepository;
import mate.academy.hw.repository.cart.ShoppingCartRepository;
import mate.academy.hw.service.cart.ShoppingCartService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private static final int DEFAULT_QUANTITY = 1;
    private static final int DEFAULT_INCREMENT = 1;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper itemMapper;
    private final ShoppingCartMapper cartMapper;

    @Override
    public ShoppingCartResponseDto getCart(Authentication authentication) {
        String email = authentication.getName();
        ShoppingCart cart = shoppingCartRepository.findByUserEmail(email);
        return cartMapper.toDto(cart);
    }

    @Override
    public CartItemResponseDto saveItem(
            Authentication authentication,
            CartItemRequestDto dto
    ) {
        ShoppingCart cart = getCartInner(authentication);
        CartItem existed = cartItemRepository
                .getCartItemByBook_IdAndShoppingCart_Id(dto.getBookId(), cart.getId());

        if (existed != null) {
            existed.setQuantity(existed.getQuantity() + DEFAULT_INCREMENT);
            return itemMapper.toDto(cartItemRepository.save(existed));
        }

        CartItem item = itemMapper.toEntity(dto);
        item.setQuantity(DEFAULT_QUANTITY);
        item.setShoppingCart(cart);
        return itemMapper.toDto(cartItemRepository.save(item));
    }

    @Override
    public CartItemResponseDto updateBooks(
            Authentication authentication,
            Long id,
            int quantity
    ) {
        CartItem item = cartItemRepository
                .getCartItemByShoppingCart_User_EmailAndId(
                        authentication.getName(),
                        id
                ).orElseThrow(
                    () -> new EntityNotFoundException(
                        "Can't find cart item by ID: " + id
                )
        );

        if (quantity < 0) {
            quantity = 0;
        }

        item.setQuantity(quantity);
        return itemMapper.toDto(cartItemRepository.save(item));
    }

    @Override
    public void deleteBook(Authentication authentication, Long cartItemId) {
        CartItem item = cartItemRepository
                .getCartItemByShoppingCart_User_EmailAndId(
                        authentication.getName(),
                        cartItemId)
                .orElseThrow(
                    () -> new EntityNotFoundException(
                        "Can't find cart item by ID: " + cartItemId
                )
        );
        cartItemRepository.delete(item);
    }

    private ShoppingCart getCartInner(Authentication authentication) {
        String email = authentication.getName();
        return shoppingCartRepository.findByUserEmail(email);
    }
}
