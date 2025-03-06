package org.acme.demo.repository;

import jakarta.annotation.Nonnull;
import org.acme.demo.entity.Icon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IconRepository extends JpaRepository<Icon, Long> {

    /**
     * Find an icon by its name.
     *
     * @param iconName the name of the icon
     * @return an Optional containing the icon if found, otherwise empty
     */
    Optional<Icon> findByIconName(@Nonnull String iconName);
}
