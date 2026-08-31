package mate.academy.hw.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;
import mate.academy.hw.dto.order.item.OrderItemResponseDto;

@Data
public class OrderResponseDto {
    private Long id;
    private Long userId;
    private Set<OrderItemResponseDto> orderItems;
    private LocalDateTime orderTime;
    private BigDecimal total;
    private String status;
}
