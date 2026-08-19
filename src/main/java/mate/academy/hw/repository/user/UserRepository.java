package mate.academy.hw.repository.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.Optional;
import mate.academy.hw.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(@Email @NotBlank String email);

    @Query(
            "FROM User u "
                    + "LEFT JOIN FETCH u.roles"
                    + " WHERE u.email = :email"
    )
    Optional<User> getUserByEmail(@Param("email") String email);

}
