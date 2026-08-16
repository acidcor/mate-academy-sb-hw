package mate.academy.hw.repository.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import mate.academy.hw.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(@Email @NotBlank String email);
}
