package eu.trufchev.intargovishte.user.repository;

import eu.trufchev.intargovishte.user.entity.Roles;
import org.springframework.data.repository.CrudRepository;

public interface RolesRepository extends CrudRepository<Roles, Long> {
    boolean existsByName(String name);
    Roles findByName(String name);
}
