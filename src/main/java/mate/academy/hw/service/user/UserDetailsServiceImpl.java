package mate.academy.hw.service.user;

import lombok.RequiredArgsConstructor;
import mate.academy.hw.repository.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return userRepo.getUserByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("Can't find user by email: " + email)
        );
    }

}
