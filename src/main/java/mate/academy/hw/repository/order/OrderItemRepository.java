package mate.academy.hw.repository.order;

import mate.academy.hw.model.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Page<OrderItem> findOrderItemsByOrder_IdAndOrder_User_Id(
            Long oderId,
            Long userId,
            Pageable pageable
    );

    OrderItem findOrderItemByOrder_IdAndOrder_User_IdAndId(
            Long orderId,
            Long userId,
            Long itemId
    );
}
