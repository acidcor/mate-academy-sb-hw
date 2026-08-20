package mate.academy.hw.dto.user;

import org.springframework.security.core.Authentication;

public record UserLoginResponseDto(String token) {
}
