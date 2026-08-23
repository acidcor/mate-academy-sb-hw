package mate.academy.hw.dto.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CartItemRequestDto {
    @NotBlank
    private Long bookId;
    @Min(1)
    private int quantity;
}
