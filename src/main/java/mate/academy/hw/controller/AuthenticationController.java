package mate.academy.hw.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import mate.academy.hw.dto.user.UserRegistrationRequestDto;
import mate.academy.hw.dto.user.UserResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthenticationController {

    @Operation(summary = "Create a new user")
    @PostMapping("/registration")
    @ResponseStatus(value = HttpStatus.CREATED)
    public UserResponseDto register(@RequestBody @Valid UserRegistrationRequestDto request) {
        return null;
    }
}
