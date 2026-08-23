package mate.academy.hw.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.hw.dto.cart.CartItemRequestDto;
import mate.academy.hw.dto.cart.CartItemResponseDto;
import mate.academy.hw.dto.cart.ShoppingCartResponseDto;
import mate.academy.hw.service.cart.ShoppingCartService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
@Tag(
        name = "Shopping cart",
        description = "Provide CRUD operations related "
                + "to shopping cart and cart items"
)
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @Operation(summary = "Add book to user shopping cart")
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping
    public CartItemResponseDto addBook(
            Authentication authentication,
            @Valid CartItemRequestDto dto
    ) {
        return shoppingCartService.saveItem(authentication, dto);
    }

    @Operation(summary = "Find all items in user shopping cart")
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping
    public ShoppingCartResponseDto getCart(Authentication authentication) {
        return shoppingCartService.getCart(authentication);
    }

    @Operation(summary = "Update an item quantity in user shopping cart")
    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/items/{cartItemId}")
    public CartItemResponseDto updateBooksFromCart(
            Authentication authentication,
            @PathVariable Long cartItemId,
            @RequestBody int quantity
    ) {
        return shoppingCartService.updateBooks(authentication, cartItemId, quantity);
    }

    @Operation(summary = "Delete an item from user shopping cart")
    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/items/{cartItemId}")
    public void deleteBooksFromCart(
            Authentication authentication,
            @PathVariable Long cartItemId
    ) {
        shoppingCartService.deleteBook(authentication, cartItemId);
    }
}
