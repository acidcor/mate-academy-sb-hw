package mate.academy.hw.repository.cart;

import java.util.Optional;
import mate.academy.hw.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem getCartItemByBook_IdAndShoppingCart_Id(
            Long bookId,
            Long shoppingCartId
    );

    Optional<CartItem> getCartItemByShoppingCart_User_EmailAndId(
            String shoppingCartUserEmail,
            Long id
    );

    CartItem getCartItemByIdAndShoppingCartId(Long id, Long shoppingCartId);
}
