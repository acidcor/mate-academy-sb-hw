package mate.academy.hw.dto.cart;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UpdateCartItemDto {
    @Min(0)
    private int quantity;
}
