package org.acme.demo.repository;

import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.acme.demo.entity.RoleName;
import org.acme.demo.entity.Role;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository <Role, Long>{

    /**
     * Find a role by its name.
     *
     * @param name the name of the role
     * @return an Optional containing the role if found, otherwise empty
     */
    Optional <Role> findByName(@Nonnull RoleName name);
}
