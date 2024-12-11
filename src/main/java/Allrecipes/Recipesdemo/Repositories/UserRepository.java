package Allrecipes.Recipesdemo.Repositories;

import Allrecipes.Recipesdemo.Entities.Role;
import Allrecipes.Recipesdemo.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    User findUserByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<User> findByUsernameOrEmail(String username, String email);
    List<User> findByRole(Role role);
}