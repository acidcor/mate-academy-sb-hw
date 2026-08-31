package mate.academy.hw.dto.order;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderUserRequestDto {
    @NotBlank
    private String shippingAddress;
}
