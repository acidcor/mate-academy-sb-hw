package mate.academy.hw.service.cart.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mate.academy.hw.dto.cart.ShoppingCartResponseDto;
import mate.academy.hw.dto.cart.UpdateCartItemDto;
import mate.academy.hw.dto.cart.item.CartItemRequestDto;
import mate.academy.hw.exceptrion.EntityNotFoundException;
import mate.academy.hw.mapper.ShoppingCartMapper;
import mate.academy.hw.model.Book;
import mate.academy.hw.model.CartItem;
import mate.academy.hw.model.ShoppingCart;
import mate.academy.hw.model.User;
import mate.academy.hw.repository.book.BookRepository;
import mate.academy.hw.repository.cart.CartItemRepository;
import mate.academy.hw.repository.cart.ShoppingCartRepository;
import mate.academy.hw.service.cart.ShoppingCartService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private static final int DEFAULT_QUANTITY = 0;
    private static final int DEFAULT_INCREMENT = 1;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartMapper cartMapper;
    private final BookRepository bookRepository;

    @Override
    public ShoppingCartResponseDto getCart(Authentication authentication) {
        return cartMapper.toDto(getCartInner(authentication));
    }

    @Override
    public ShoppingCartResponseDto saveItem(
            Authentication authentication,
            CartItemRequestDto dto
    ) {
        Long bookId = dto.getBookId();
        ShoppingCart cart = getCartInner(authentication);
        Book book = bookRepository.findById(bookId).orElseThrow(
                        () -> new EntityNotFoundException("Can't find a book by id: " + bookId)
        );

        CartItem updatedItem = cart.getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(bookId))
                .findFirst()
                .orElseGet(
                        () -> {
                            CartItem newItem = new CartItem();
                            newItem.setShoppingCart(cart);
                            newItem.setBook(book);
                            newItem.setQuantity(DEFAULT_QUANTITY);
                            cart.getCartItems().add(newItem);
                            return newItem;
                        }
                );

        updatedItem.setQuantity(updatedItem.getQuantity() + DEFAULT_INCREMENT);
        System.out.println("USER ID = " + ((User) authentication.getPrincipal()).getId());
        System.out.println("CART ID = " + cart.getId());
        return cartMapper.toDto(shoppingCartRepository.save(cart));
    }

    @Override
    public ShoppingCartResponseDto updateBooks(
            Authentication authentication,
            Long id,
            UpdateCartItemDto updateDto
    ) {
        ShoppingCart cart = getCartInner(authentication);
        Long cartId = cart.getId();
        CartItem cartItem = cartItemRepository.getCartItemByIdAndShoppingCartId(id, cartId);

        if (cartItem == null) {
            throw new EntityNotFoundException(
                    String.format("Can't item by id '%s' in shopping cart: %s",
                            id, cartId)
            );
        }

        cartItem.setQuantity(updateDto.getQuantity());
        return cartMapper.toDto(shoppingCartRepository.save(cart));
    }

    @Override
    public void deleteBook(Authentication authentication, Long cartItemId) {
        ShoppingCart cart = getCartInner(authentication);
        CartItem cartItem = cartItemRepository.getCartItemByIdAndShoppingCartId(
                cartItemId, cart.getId()
        );
        cartItemRepository.delete(cartItem);
    }

    private ShoppingCart getCartInner(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        if (user == null) {
            throw new UsernameNotFoundException(
                    "Can't find user with sutch email: "
                            + authentication.getName()
            );
        }
        ShoppingCart cart = shoppingCartRepository.findShoppingCartByUser_Id(user.getId());
        System.out.println(cart.getId());
        return cart;
    }

    @Override
    public void addCart(User user) {
        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user);
        shoppingCartRepository.save(cart);
    }
}
