package mate.academy.hw.dto.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.ISBN;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
public class BookCreateRequestDto {
    @NotBlank(message = "Title must not be blank")
    private String title;
    @NotBlank(message = "Author must not be blank")
    private String author;
    @ISBN
    @NotBlank(message = "ISBN must not be blank")
    private String isbn;
    @Min(0)
    @NotNull(message = "Price must not be blank")
    private BigDecimal price;
    private String description;
    @URL
    private String coverImage;

}
