package eu.trufchev.intargovishte.user.repository;

import eu.trufchev.intargovishte.user.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Boolean existsByEmail(String email);
    Optional<User> findByUsernameOrEmail(String username, String email);
    User findByUsername(String username);
    Boolean existsByUsername(String username);
    @Modifying
    @Transactional
    @Query("DELETE FROM user_roles ur WHERE ur.user_id = :userId")
    void deleteUserRolesByUserId(Long userId);
}