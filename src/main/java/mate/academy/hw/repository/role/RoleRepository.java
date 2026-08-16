package mate.academy.hw.repository.role;

import java.util.Optional;
import mate.academy.hw.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> getByName(Role.RoleName name);
}
