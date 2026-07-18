package mate.academy.hw.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.ISBN;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
public class CreateBookRequestDto {
    private Long id;
    @NotNull
    private String title;
    @NotNull
    private String author;
    @ISBN
    @NotNull
    private String isbn;
    @Min(0)
    @NotNull
    private BigDecimal price;
    private String description;
    @URL
    private String coverImage;

}
