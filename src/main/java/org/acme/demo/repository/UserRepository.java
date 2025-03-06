package org.acme.demo.repository;

import jakarta.annotation.Nonnull;
import org.acme.demo.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface UserRepository extends JpaRepository<AppUser,Long>{
    /**
     * Find a user by their email.
     *
     * @param email the email of the user
     * @return an Optional containing the user if found, otherwise empty
     */
    Optional<AppUser> findByEmail(@Nonnull String email);

    /**
     * Find a user by their username.
     *
     * @param username the username of the user
     * @return an Optional containing the user if found, otherwise empty
     */
    Optional<AppUser> findByUsername(@Nonnull String username);

    /**
     * Find a user by OAuth provider and provider ID.
     *
     * @param provider the OAuth provider (e.g., Google, GitHub)
     * @param providerId the provider ID associated with the user
     * @return an Optional containing the user if found, otherwise empty
     */
    Optional<AppUser> findByProviderAndProviderId(@Nonnull String provider,@Nonnull String providerId);
}

