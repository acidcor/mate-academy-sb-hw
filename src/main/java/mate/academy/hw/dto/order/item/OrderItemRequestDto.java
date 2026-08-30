package mate.academy.hw.dto.order.item;

import jakarta.validation.constraints.NotBlank;

public class OrderItemRequestDto {
    @NotBlank
    private String shippingAddress;
}
