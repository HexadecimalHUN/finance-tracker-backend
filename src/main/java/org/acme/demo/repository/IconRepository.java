package org.acme.demo.repository;

import org.acme.demo.entity.Icon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IconRepository extends JpaRepository<Icon, Long> {
    Optional<Icon> findByIconName(String iconName);
}
