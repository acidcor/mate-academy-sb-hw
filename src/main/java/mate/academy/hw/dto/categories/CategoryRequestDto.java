package mate.academy.hw.dto.categories;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequestDto {
    @NotBlank(message = "Category name can't be empty")
    private String name;
    private String description;
}
