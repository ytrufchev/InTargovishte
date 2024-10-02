package eu.trufchev.intargovishte.user.repository;

import eu.trufchev.intargovishte.user.entity.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findByName(String name);
}