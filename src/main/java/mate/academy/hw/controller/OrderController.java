package mate.academy.hw.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.hw.dto.order.OrderResponseDto;
import mate.academy.hw.dto.order.OrderStatusUpdateRequestDto;
import mate.academy.hw.dto.order.OrderUserRequestDto;
import mate.academy.hw.dto.order.item.OrderItemResponseDto;
import mate.academy.hw.service.order.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
@Tag(
        name = "Orders",
        description = "Provide CRUD operations related "
                + "to orders and orders items"
)
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping
    @Operation(summary = "User add order")
    public void addOrder(
            Authentication authentication,
            @RequestBody @Valid OrderUserRequestDto request
    ) {
        orderService.addOrder(authentication, request);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping
    @Operation(summary = "Find all orders for user")
    public Page<OrderResponseDto> findAllOrders(
            Authentication authentication,
            Pageable pageable
    ) {
        return orderService.findAllOrders(authentication, pageable);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/{orderId}/items")
    @Operation(summary = "Find all items in order by ID")
    public Page<OrderItemResponseDto> findOrderDetails(
            Authentication authentication,
            @PathVariable Long orderId,
            Pageable pageable
    ) {
        return orderService.findOrderDetails(authentication, orderId, pageable);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/{orderId}/items/{itemId}")
    @Operation(summary = "Find an item by ID in order by ID")
    public OrderItemResponseDto findOrderItemDetails(
            Authentication authentication,
            @PathVariable Long orderId,
            @PathVariable Long itemId
    ) {
        return orderService.findOrderItemDetails(authentication, orderId, itemId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{orderId}")
    @Operation(summary = "Admin update order status by ID")
    public void updateStatus(
            @PathVariable Long orderId,
            @RequestBody @Valid OrderStatusUpdateRequestDto requestDto
    ) {
        orderService.updateStatus(orderId, requestDto);
    }

}
