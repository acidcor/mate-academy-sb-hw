package mate.academy.hw.service.order;

import mate.academy.hw.dto.order.OrderResponseDto;
import mate.academy.hw.dto.order.OrderStatusUpdateRequestDto;
import mate.academy.hw.dto.order.OrderUserRequestDto;
import mate.academy.hw.dto.order.item.OrderItemResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface OrderService {
    void addOrder(Authentication authentication, OrderUserRequestDto request);

    Page<OrderResponseDto> findAllOrders(Authentication authentication, Pageable pageable);

    Page<OrderItemResponseDto> findOrderDetails(
            Authentication authentication,
            Long orderId,
            Pageable pageable
    );

    OrderItemResponseDto findOrderItemDetails(
            Authentication authentication,
            Long orderId,
            Long itemId
    );

    void updateStatus(Long orderId, OrderStatusUpdateRequestDto requestDto);
}
