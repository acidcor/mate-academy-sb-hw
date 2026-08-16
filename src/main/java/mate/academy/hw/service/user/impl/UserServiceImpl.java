package mate.academy.hw.service.user.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.hw.dto.user.UserRegistrationRequestDto;
import mate.academy.hw.dto.user.UserResponseDto;
import mate.academy.hw.exceptrion.RegistrationException;
import mate.academy.hw.mapper.UserMapper;
import mate.academy.hw.repository.user.UserRepository;
import mate.academy.hw.service.user.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto dto) throws RegistrationException {
        if (repository.existsByEmail(dto.getEmail())) {
            throw new RegistrationException(
                    String.format("User with this email: %s already exists", dto.getEmail())
            );
        }
        return mapper.toDto(
                repository.save(mapper.toModel(dto)));
    }
}
