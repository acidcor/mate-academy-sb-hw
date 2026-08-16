package mate.academy.hw.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import mate.academy.hw.annotation.FieldMatch;

@Getter
@Setter
@FieldMatch(first = "password",
        second = "repeatPassword",
        message = "Password mismatch"
)
public class UserRegistrationRequestDto {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    @Size(min = 8,
            max = 32,
            message = "Password size must be between 8 and 32"
    )
    private String password;
    @NotBlank
    private String repeatPassword;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private String shippingAddress;
}
