package mate.academy.hw.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import mate.academy.hw.model.Order;

@Data
public class OrderStatusUpdateRequestDto {
    @NotNull
    private Order.Status status;
}
