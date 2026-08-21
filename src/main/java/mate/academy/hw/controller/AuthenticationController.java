package mate.academy.hw.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.hw.dto.user.UserLoginRequestDto;
import mate.academy.hw.dto.user.UserLoginResponseDto;
import mate.academy.hw.dto.user.UserRegistrationRequestDto;
import mate.academy.hw.dto.user.UserResponseDto;
import mate.academy.hw.exceptrion.RegistrationException;
import mate.academy.hw.security.AuthenticationService;
import mate.academy.hw.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
@Tag(
        name = "Authentication",
        description = "Provide authentication operations"
)
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Operation(summary = "Create a new user")
    @PostMapping("/registration")
    @ResponseStatus(value = HttpStatus.CREATED)
    public UserResponseDto register(
            @RequestBody @Valid UserRegistrationRequestDto request
    ) throws RegistrationException {
        return userService.register(request);
    }

    @Operation(summary = "User login")
    @PostMapping("/login")
    public UserLoginResponseDto login(
            @RequestBody @Valid UserLoginRequestDto request) {
        return authenticationService.authenticate(request);
    }
}


