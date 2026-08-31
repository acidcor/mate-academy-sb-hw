package mate.academy.hw.service.order.impl;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mate.academy.hw.dto.order.OrderResponseDto;
import mate.academy.hw.dto.order.OrderStatusUpdateRequestDto;
import mate.academy.hw.dto.order.OrderUserRequestDto;
import mate.academy.hw.dto.order.item.OrderItemResponseDto;
import mate.academy.hw.exceptrion.EntityNotFoundException;
import mate.academy.hw.mapper.OrderItemMapper;
import mate.academy.hw.mapper.OrderMapper;
import mate.academy.hw.model.CartItem;
import mate.academy.hw.model.Order;
import mate.academy.hw.model.OrderItem;
import mate.academy.hw.model.ShoppingCart;
import mate.academy.hw.model.User;
import mate.academy.hw.repository.cart.ShoppingCartRepository;
import mate.academy.hw.repository.order.OrderItemRepository;
import mate.academy.hw.repository.order.OrderRepository;
import mate.academy.hw.service.order.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;
    private final OrderMapper orderMapper;

    @Override
    public void addOrder(Authentication authentication, OrderUserRequestDto request) {
        User user = getUserInner(authentication);
        ShoppingCart cart = cartRepository.findShoppingCartByUser_Id(user.getId()).orElseThrow(
                () -> new EntityNotFoundException(
                        "Can't find cart by user e-mail: " + authentication.getName()
                )
        );

        Set<CartItem> cartItems = cart.getCartItems();
        if (cartItems.isEmpty()) {
            throw new EntityNotFoundException(
                    "Can't find any cart items by user e-mail: " + authentication.getName()
            );
        }
        Order order = new Order();
        Set<OrderItem> orderItems = getOrderItemsFromCartItems(cartItems);

        for (OrderItem item : orderItems) {
            item.setOrder(order);
        }

        order.setUser(user);
        order.setShippingAddress(request.getShippingAddress());
        order.setOrderDate(LocalDateTime.now());
        order.setTotal(getTotalPriceFromOrderItems(orderItems));
        order.setStatus(Order.Status.AWAITING_PAYMENT);
        order.setOrderItems(orderItems);

        cartItems.clear();

        cartRepository.save(cart);
        orderRepository.save(order);
    }

    @Override
    public Page<OrderResponseDto> findAllOrders(Authentication authentication, Pageable pageable) {
        Page<Order> orders = orderRepository.findOrdersByUserId(
                getUserInner(authentication).getId(), pageable
        );
        if (orders.isEmpty()) {
            throw new EntityNotFoundException(
                    "Can't find any orders by user e-mail: "
                            + authentication.getName()
            );
        }
        return orders.map(orderMapper::toDto);
    }

    @Override
    public Page<OrderItemResponseDto> findOrderDetails(
            Authentication authentication,
            Long orderId,
            Pageable pageable
    ) {
        Long userId = getUserInner(authentication).getId();
        Page<OrderItem> items = orderItemRepository
                .findOrderItemsByOrder_IdAndOrder_User_Id(orderId, userId, pageable);

        if (items.isEmpty()) {
            throw new EntityNotFoundException(String.format(
                    "Can't find any ordered items by order ID %s, for user e-mail '%s'",
                    userId, orderId)
            );
        }
        return items.map(orderItemMapper::toDto);
    }

    @Override
    public OrderItemResponseDto findOrderItemDetails(
            Authentication authentication,
            Long orderId,
            Long itemId
    ) {
        Long userId = getUserInner(authentication).getId();
        OrderItem item = orderItemRepository
                .findOrderItemByOrder_IdAndOrder_User_IdAndId(orderId, userId, itemId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(
                "Can't find ordered item at order ID %s, for user e-mail '%s' by ID %s",
                orderId, userId, itemId)));

        return orderItemMapper.toDto(item);
    }

    @Override
    public void updateStatus(Long orderId, OrderStatusUpdateRequestDto requestDto) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new EntityNotFoundException("Can't find order by id: " + orderId)
        );
        order.setStatus(requestDto.getStatus());
        orderRepository.save(order);
    }

    private User getUserInner(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        if (user == null) {
            throw new UsernameNotFoundException(
                    "Can't find user with sutch e-mail: "
                            + authentication.getName()
            );
        }
        return user;
    }

    private BigDecimal getTotalPriceFromOrderItems(Set<OrderItem> items) {
        return items.stream()
                .map(item -> item.getPrice().multiply(
                        BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Set<OrderItem> getOrderItemsFromCartItems(Set<CartItem> items) {
        return items.stream()
                .map(orderItemMapper::toEntity)
                .collect(Collectors.toSet());
    }

}
