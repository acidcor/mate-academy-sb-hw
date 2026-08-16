package mate.academy.hw.service.user;

import mate.academy.hw.dto.user.UserRegistrationRequestDto;
import mate.academy.hw.dto.user.UserResponseDto;
import mate.academy.hw.exceptrion.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto dto) throws RegistrationException;
}
