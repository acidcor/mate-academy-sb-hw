package mate.academy.hw.service.user.impl;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.hw.dto.user.UserRegistrationRequestDto;
import mate.academy.hw.dto.user.UserResponseDto;
import mate.academy.hw.exceptrion.RegistrationException;
import mate.academy.hw.mapper.UserMapper;
import mate.academy.hw.model.Role;
import mate.academy.hw.model.User;
import mate.academy.hw.repository.role.RoleRepository;
import mate.academy.hw.repository.user.UserRepository;
import mate.academy.hw.service.user.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Role.RoleName DEFAULT_ROLE = Role.RoleName.USER;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final UserMapper mapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto dto) throws RegistrationException {
        if (userRepo.existsByEmail(dto.getEmail())) {
            throw new RegistrationException(
                    String.format("User with this email: %s already exists", dto.getEmail())
            );
        }
        User userModel = mapper.toModel(dto);

        userModel.setPassword(passwordEncoder.encode(dto.getPassword()));

        Set<Role> roles = Set.of(
                roleRepo.getByName(DEFAULT_ROLE)
                        .orElseThrow(() -> new RegistrationException("Can't find default role "
                                + "while registration process")));
        userModel.setRoles(roles);

        return mapper.toDto(
                userRepo.save(userModel));
    }
}
