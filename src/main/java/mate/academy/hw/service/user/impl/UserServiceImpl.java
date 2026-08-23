package mate.academy.hw.service.user.impl;

import jakarta.transaction.Transactional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.hw.dto.user.UserRegistrationRequestDto;
import mate.academy.hw.dto.user.UserResponseDto;
import mate.academy.hw.exceptrion.EntityNotFoundException;
import mate.academy.hw.exceptrion.RegistrationException;
import mate.academy.hw.mapper.UserMapper;
import mate.academy.hw.model.Role;
import mate.academy.hw.model.ShoppingCart;
import mate.academy.hw.model.User;
import mate.academy.hw.repository.cart.ShoppingCartRepository;
import mate.academy.hw.repository.role.RoleRepository;
import mate.academy.hw.repository.user.UserRepository;
import mate.academy.hw.service.user.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepo;
    private final ShoppingCartRepository cartRepo;
    private final RoleRepository roleRepo;
    private final UserMapper mapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto dto) throws RegistrationException {
        if (userRepo.existsByEmail(dto.getEmail())) {
            throw new RegistrationException(
                    String.format("User with this email: %s already exists", dto.getEmail())
            );
        }
        User userModel = mapper.toEntity(dto);

        userModel.setPassword(passwordEncoder.encode(dto.getPassword()));

        Role role = roleRepo.getByName(Role.RoleName.USER)
                .orElseThrow(() -> new EntityNotFoundException("Can't find default role "
                + "while registration process"));

        userModel.setRoles(Set.of(role));

        User user = userRepo.save(userModel);
        addCart(user);

        return mapper.toDto(user);
    }

    private void addCart(User user) {
        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user);
        cartRepo.save(cart);
    }
}
