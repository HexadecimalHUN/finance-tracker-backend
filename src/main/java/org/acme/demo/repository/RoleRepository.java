package org.acme.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.acme.demo.entity.RoleName;
import org.acme.demo.entity.Role;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository <Role, Long>{
    Optional <Role> findByName(RoleName name);
}
